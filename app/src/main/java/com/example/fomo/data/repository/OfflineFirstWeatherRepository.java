package com.example.fomo.data.repository;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.example.fomo.data.local.dao.WeatherDao;
import com.example.fomo.data.local.entity.HourlyEntity;
import com.example.fomo.data.local.entity.WeatherEntity;
import com.example.fomo.data.remote.api.WeatherApiService;
import com.example.fomo.data.remote.dto.OpenMeteoWeatherResponse;
import com.example.fomo.data.remote.mapper.WeatherDtoMapper;
import com.example.fomo.domain.model.CurrentWeather;
import com.example.fomo.domain.model.Hourly;
import com.example.fomo.domain.model.WeatherForecast;
import com.example.fomo.domain.repository.WeatherRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import retrofit2.Response;

/**
 * Production Single Source of Truth (SSOT) weather repository.
 * Coordinates local Room database persistence and remote Open-Meteo REST API queries
 * with stale-while-revalidate offline caching and multi-location parameterization.
 */
public class OfflineFirstWeatherRepository implements WeatherRepository {

    public static final long DEFAULT_CACHE_TTL_MILLIS = 30 * 60 * 1000L; // 30 minutes
    public static final double DEFAULT_LATITUDE = 40.7128; // New York
    public static final double DEFAULT_LONGITUDE = -74.0060;

    private final WeatherApiService apiService;
    private final WeatherDao weatherDao;
    private final Executor workerExecutor;
    private final Executor mainExecutor;
    private final long cacheTtlMillis;
    private final double defaultLatitude;
    private final double defaultLongitude;

    public OfflineFirstWeatherRepository(@NonNull WeatherApiService apiService,
                                         @NonNull WeatherDao weatherDao) {
        this(
                apiService,
                weatherDao,
                Executors.newSingleThreadExecutor(),
                new HandlerExecutor(new Handler(Looper.getMainLooper())),
                DEFAULT_CACHE_TTL_MILLIS,
                DEFAULT_LATITUDE,
                DEFAULT_LONGITUDE
        );
    }

    public OfflineFirstWeatherRepository(@NonNull WeatherApiService apiService,
                                         @NonNull WeatherDao weatherDao,
                                         @NonNull Executor workerExecutor,
                                         @NonNull Executor mainExecutor,
                                         long cacheTtlMillis,
                                         double defaultLatitude,
                                         double defaultLongitude) {
        this.apiService = Objects.requireNonNull(apiService, "apiService must not be null");
        this.weatherDao = Objects.requireNonNull(weatherDao, "weatherDao must not be null");
        this.workerExecutor = Objects.requireNonNull(workerExecutor, "workerExecutor must not be null");
        this.mainExecutor = Objects.requireNonNull(mainExecutor, "mainExecutor must not be null");
        this.cacheTtlMillis = cacheTtlMillis;
        this.defaultLatitude = defaultLatitude;
        this.defaultLongitude = defaultLongitude;
    }

    @NonNull
    @Override
    public WeatherForecast getSynchronousForecast() {
        WeatherForecast cached = loadFromCache();
        if (cached != null) {
            return cached;
        }
        try {
            Response<OpenMeteoWeatherResponse> response = apiService.getForecast(
                    defaultLatitude,
                    defaultLongitude,
                    WeatherApiService.DEFAULT_CURRENT_METRICS,
                    WeatherApiService.DEFAULT_HOURLY_METRICS,
                    WeatherApiService.DEFAULT_TIMEZONE
            ).execute();

            if (response.isSuccessful() && response.body() != null) {
                WeatherForecast networkForecast = WeatherDtoMapper.toDomain(response.body());
                saveToCache(networkForecast);
                return networkForecast;
            }
        } catch (Exception ignored) {
            // Fallback
        }
        return createFallbackForecast();
    }

    @Override
    public void getForecastAsync(@NonNull Callback<WeatherForecast> callback) {
        getForecastForLocationAsync(defaultLatitude, defaultLongitude, callback);
    }

    @Override
    public void getForecastForLocationAsync(double latitude, double longitude, @NonNull Callback<WeatherForecast> callback) {
        Objects.requireNonNull(callback, "callback must not be null");

        workerExecutor.execute(() -> {
            WeatherForecast cachedForecast = loadFromCache();
            if (cachedForecast != null && isCoordinatesMatchingDefault(latitude, longitude)) {
                WeatherEntity cacheMeta = weatherDao.getWeatherCache();
                if (cacheMeta != null) {
                    long age = System.currentTimeMillis() - cacheMeta.cachedTimestamp;
                    if (age < cacheTtlMillis) {
                        // Serve fresh cache immediately
                        mainExecutor.execute(() -> callback.onSuccess(cachedForecast));
                        return;
                    }
                }
            }

            // Fetch from remote API
            try {
                Response<OpenMeteoWeatherResponse> response = apiService.getForecast(
                        latitude,
                        longitude,
                        WeatherApiService.DEFAULT_CURRENT_METRICS,
                        WeatherApiService.DEFAULT_HOURLY_METRICS,
                        WeatherApiService.DEFAULT_TIMEZONE
                ).execute();

                if (response.isSuccessful() && response.body() != null) {
                    WeatherForecast networkForecast = WeatherDtoMapper.toDomain(response.body());
                    saveToCache(networkForecast);
                    mainExecutor.execute(() -> callback.onSuccess(networkForecast));
                } else {
                    handleFetchFailure(cachedForecast, new IOException("HTTP error: " + response.code()), callback);
                }
            } catch (Exception e) {
                handleFetchFailure(cachedForecast, e, callback);
            }
        });
    }

    private boolean isCoordinatesMatchingDefault(double lat, double lon) {
        return Math.abs(lat - defaultLatitude) < 0.001 && Math.abs(lon - defaultLongitude) < 0.001;
    }

    private void handleFetchFailure(WeatherForecast cachedForecast, Exception error,
                                    Callback<WeatherForecast> callback) {
        if (cachedForecast != null) {
            // Graceful degradation: fall back to cached data when network fails
            mainExecutor.execute(() -> callback.onSuccess(cachedForecast));
        } else {
            mainExecutor.execute(() -> callback.onError(error));
        }
    }

    private WeatherForecast loadFromCache() {
        try {
            WeatherEntity cacheEntity = weatherDao.getWeatherCache();
            if (cacheEntity == null) {
                return null;
            }
            List<HourlyEntity> hourlyEntities = weatherDao.getHourlyForecasts();
            List<Hourly> hourlyList = new ArrayList<>();
            if (hourlyEntities != null) {
                for (HourlyEntity h : hourlyEntities) {
                    hourlyList.add(new Hourly(h.hour, h.temp, h.picPath));
                }
            }

            CurrentWeather currentWeather = new CurrentWeather(
                    cacheEntity.condition,
                    cacheEntity.temperature,
                    cacheEntity.dateTime,
                    cacheEntity.highLow,
                    cacheEntity.rainPercentage,
                    cacheEntity.windSpeed,
                    cacheEntity.humidityPercentage,
                    cacheEntity.iconKey
            );
            return new WeatherForecast(currentWeather, Collections.unmodifiableList(hourlyList));
        } catch (Exception e) {
            return null;
        }
    }

    private void saveToCache(WeatherForecast forecast) {
        if (forecast == null) return;
        try {
            CurrentWeather cur = forecast.getCurrentWeather();
            WeatherEntity weatherEntity = new WeatherEntity(
                    WeatherEntity.SINGLETON_CACHE_ID,
                    System.currentTimeMillis(),
                    cur.getCondition(),
                    cur.getTemperature(),
                    cur.getDateTime(),
                    cur.getHighLow(),
                    cur.getRainPercentage(),
                    cur.getWindSpeed(),
                    cur.getHumidityPercentage(),
                    cur.getIconKey()
            );

            List<HourlyEntity> hourlyEntities = new ArrayList<>();
            List<Hourly> hourlyList = forecast.getHourlyForecasts();
            if (hourlyList != null) {
                for (int i = 0; i < hourlyList.size(); i++) {
                    Hourly h = hourlyList.get(i);
                    hourlyEntities.add(new HourlyEntity(i, h.getHour(), h.getTemp(), h.getPicPath()));
                }
            }
            weatherDao.replaceCache(weatherEntity, hourlyEntities);
        } catch (Exception ignored) {
            // Ignore cache storage failure
        }
    }

    private WeatherForecast createFallbackForecast() {
        CurrentWeather cur = new CurrentWeather("Partly Cloudy", 22, "Today | 12:00 PM",
                "H:26  L:17", "10%", "12 Km/h", "60%", "cloudy_sunny");
        List<Hourly> hourly = new ArrayList<>();
        hourly.add(new Hourly("12 pm", 22, "cloudy_sunny"));
        return new WeatherForecast(cur, hourly);
    }

    private static class HandlerExecutor implements Executor {
        private final Handler handler;

        HandlerExecutor(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}
