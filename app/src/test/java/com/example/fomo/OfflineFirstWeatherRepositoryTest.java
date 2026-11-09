package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;
import com.example.fomo.data.local.dao.WeatherDao;
import com.example.fomo.data.local.entity.HourlyEntity;
import com.example.fomo.data.local.entity.WeatherEntity;
import com.example.fomo.data.remote.api.WeatherApiService;
import com.example.fomo.data.remote.dto.CurrentWeatherDto;
import com.example.fomo.data.remote.dto.HourlyForecastDto;
import com.example.fomo.data.remote.dto.OpenMeteoWeatherResponse;
import com.example.fomo.data.repository.OfflineFirstWeatherRepository;
import com.example.fomo.domain.model.WeatherForecast;
import com.example.fomo.domain.repository.WeatherRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.Request;
import okio.Timeout;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfflineFirstWeatherRepositoryTest {

    private FakeWeatherDao fakeDao;
    private FakeWeatherApiService fakeApi;
    private OfflineFirstWeatherRepository repository;

    @Before
    public void setUp() {
        fakeDao = new FakeWeatherDao();
        fakeApi = new FakeWeatherApiService();
        repository = new OfflineFirstWeatherRepository(
                fakeApi,
                fakeDao,
                Runnable::run, // Synchronous worker executor
                Runnable::run, // Synchronous main executor
                30 * 60 * 1000L, // 30 min cache TTL
                40.7128,
                -74.0060
        );
    }

    @Test
    public void testCacheHitReturnsImmediatelyWithoutApiCall() {
        // Populate fresh cache
        WeatherEntity cached = new WeatherEntity(
                WeatherEntity.SINGLETON_CACHE_ID,
                System.currentTimeMillis() - 5000, // 5 seconds ago
                "Clear sky",
                25,
                "Today | 12:00 PM",
                "H:28  L:18",
                "0%",
                "10 Km/h",
                "50%",
                "sunny"
        );
        HourlyEntity hourly = new HourlyEntity(0, "12 pm", 25, "sunny");
        fakeDao.replaceCache(cached, Collections.singletonList(hourly));

        AtomicReference<WeatherForecast> successResult = new AtomicReference<>();
        repository.getForecastAsync(new WeatherRepository.Callback<WeatherForecast>() {
            @Override
            public void onSuccess(@NonNull WeatherForecast data) {
                successResult.set(data);
            }

            @Override
            public void onError(@NonNull Throwable error) {
            }
        });

        assertNotNull(successResult.get());
        assertEquals("Clear sky", successResult.get().getCurrentWeather().getCondition());
        assertEquals(25, successResult.get().getCurrentWeather().getTemperature());
        assertEquals(0, fakeApi.getCallCount()); // Network never touched
    }

    @Test
    public void testCacheMissCallsNetworkAndSavesToCache() {
        // Cache is empty
        CurrentWeatherDto current = new CurrentWeatherDto(
                "2026-09-01T12:00", 22.0, 55, 22.0, 0.0, 2, 8.0
        );
        HourlyForecastDto hourly = new HourlyForecastDto(
                Collections.singletonList("2026-09-01T12:00"),
                Collections.singletonList(22.0),
                Collections.singletonList(2)
        );
        OpenMeteoWeatherResponse apiResponse = new OpenMeteoWeatherResponse(
                40.7128, -74.0060, "America/New_York", current, hourly
        );
        fakeApi.setResponse(Response.success(apiResponse));

        AtomicReference<WeatherForecast> successResult = new AtomicReference<>();
        repository.getForecastAsync(new WeatherRepository.Callback<WeatherForecast>() {
            @Override
            public void onSuccess(@NonNull WeatherForecast data) {
                successResult.set(data);
            }

            @Override
            public void onError(@NonNull Throwable error) {
            }
        });

        assertNotNull(successResult.get());
        assertEquals("Partly cloudy", successResult.get().getCurrentWeather().getCondition());
        assertEquals(22, successResult.get().getCurrentWeather().getTemperature());
        assertEquals(1, fakeApi.getCallCount());

        // Verify saved to local database
        assertNotNull(fakeDao.getWeatherCache());
        assertEquals("Partly cloudy", fakeDao.getWeatherCache().condition);
        assertEquals(22, fakeDao.getWeatherCache().temperature);
        assertEquals(1, fakeDao.getHourlyForecasts().size());
    }

    @Test
    public void testGetForecastForLocationCallsApiWithExactCoordinates() {
        CurrentWeatherDto current = new CurrentWeatherDto("2026-09-01T12:00", 18.0, 60, 18.0, 0.0, 1, 12.0);
        HourlyForecastDto hourly = new HourlyForecastDto(
                Collections.singletonList("2026-09-01T12:00"),
                Collections.singletonList(18.0),
                Collections.singletonList(1)
        );
        OpenMeteoWeatherResponse netResponse = new OpenMeteoWeatherResponse(48.8566, 2.3522, "Europe/Paris", current, hourly);
        fakeApi.setResponse(Response.success(netResponse));

        AtomicReference<WeatherForecast> resultRef = new AtomicReference<>();
        repository.getForecastForLocationAsync(48.8566, 2.3522, new WeatherRepository.Callback<WeatherForecast>() {
            @Override
            public void onSuccess(@NonNull WeatherForecast data) {
                resultRef.set(data);
            }

            @Override
            public void onError(@NonNull Throwable error) {
            }
        });

        assertNotNull(resultRef.get());
        assertEquals(1, fakeApi.getCallCount());
        assertEquals(18, resultRef.get().getCurrentWeather().getTemperature());
    }

    @Test
    public void testNetworkFailureFallsBackToExistingCache() {
        // Expired cache
        WeatherEntity staleCache = new WeatherEntity(
                WeatherEntity.SINGLETON_CACHE_ID,
                System.currentTimeMillis() - (60 * 60 * 1000L), // 1 hour old
                "Old Rain",
                18,
                "Today | 11:00 AM",
                "H:20  L:15",
                "80%",
                "15 Km/h",
                "85%",
                "rainy"
        );
        fakeDao.replaceCache(staleCache, Collections.emptyList());

        // Network fails
        fakeApi.setException(new IOException("Connection timed out"));

        AtomicReference<WeatherForecast> successResult = new AtomicReference<>();
        AtomicReference<Throwable> errorResult = new AtomicReference<>();
        repository.getForecastAsync(new WeatherRepository.Callback<WeatherForecast>() {
            @Override
            public void onSuccess(@NonNull WeatherForecast data) {
                successResult.set(data);
            }

            @Override
            public void onError(@NonNull Throwable error) {
                errorResult.set(error);
            }
        });

        assertNotNull("Should gracefully fall back to cached forecast", successResult.get());
        assertEquals("Old Rain", successResult.get().getCurrentWeather().getCondition());
        assertTrue(errorResult.get() == null);
    }

    @Test
    public void testNetworkFailureWithoutCacheReportsError() {
        // No cache, network fails
        fakeApi.setException(new IOException("DNS resolution failed"));

        AtomicReference<WeatherForecast> successResult = new AtomicReference<>();
        AtomicReference<Throwable> errorResult = new AtomicReference<>();
        repository.getForecastAsync(new WeatherRepository.Callback<WeatherForecast>() {
            @Override
            public void onSuccess(@NonNull WeatherForecast data) {
                successResult.set(data);
            }

            @Override
            public void onError(@NonNull Throwable error) {
                errorResult.set(error);
            }
        });

        assertTrue(successResult.get() == null);
        assertNotNull(errorResult.get());
        assertTrue(errorResult.get().getMessage().contains("DNS resolution failed"));
    }

    // Fake in-memory DAO for deterministic testing
    private static class FakeWeatherDao implements WeatherDao {
        private WeatherEntity cache;
        private final List<HourlyEntity> hourlyList = new ArrayList<>();

        @Override
        public WeatherEntity getWeatherCache() {
            return cache;
        }

        @Override
        public List<HourlyEntity> getHourlyForecasts() {
            return new ArrayList<>(hourlyList);
        }

        @Override
        public void insertWeatherCache(WeatherEntity entity) {
            this.cache = entity;
        }

        @Override
        public void insertHourlyForecasts(List<HourlyEntity> entities) {
            this.hourlyList.addAll(entities);
        }

        @Override
        public void clearHourlyForecasts() {
            hourlyList.clear();
        }

        @Override
        public void clearWeatherCache() {
            cache = null;
        }
    }

    // Fake API service for deterministic testing
    private static class FakeWeatherApiService implements WeatherApiService {
        private Response<OpenMeteoWeatherResponse> response;
        private IOException exception;
        private int callCount = 0;

        void setResponse(Response<OpenMeteoWeatherResponse> response) {
            this.response = response;
            this.exception = null;
        }

        void setException(IOException exception) {
            this.exception = exception;
            this.response = null;
        }

        int getCallCount() {
            return callCount;
        }

        @Override
        public Call<OpenMeteoWeatherResponse> getForecast(double latitude, double longitude,
                                                          String currentMetrics, String hourlyMetrics,
                                                          String timezone) {
            callCount++;
            return new FakeCall<>(response, exception);
        }
    }

    private static class FakeCall<T> implements Call<T> {
        private final Response<T> response;
        private final IOException exception;

        FakeCall(Response<T> response, IOException exception) {
            this.response = response;
            this.exception = exception;
        }

        @Override
        public Response<T> execute() throws IOException {
            if (exception != null) {
                throw exception;
            }
            return response;
        }

        @Override
        public void enqueue(Callback<T> callback) {
            if (exception != null) {
                callback.onFailure(this, exception);
            } else {
                callback.onResponse(this, response);
            }
        }

        @Override public boolean isExecuted() { return true; }
        @Override public void cancel() {}
        @Override public boolean isCanceled() { return false; }
        @Override public Call<T> clone() { return this; }
        @Override public Request request() { return new Request.Builder().url("https://api.open-meteo.com/").build(); }
        @Override public Timeout timeout() { return Timeout.NONE; }
    }
}
