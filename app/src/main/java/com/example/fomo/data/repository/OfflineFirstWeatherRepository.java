package com.example.fomo.data.repository;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.example.fomo.core.cache.FastLruCache;
import com.example.fomo.core.concurrency.AppExecutors;
import com.example.fomo.core.resilience.ResilienceCircuitBreaker;
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
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import retrofit2.Response;

/**
 * Production Single Source of Truth (SSOT) weather repository.
 * Coordinates multi-tier caching (L1 In-Memory FastLruCache -> L2 Room SQLite -> L3 OkHttp REST API)
 * with circuit-breaker fault tolerance and bounded background execution.
 */
public class OfflineFirstWeatherRepository implements WeatherRepository {

    public static final long DEFAULT_CACHE_TTL_MILLIS = 30 * 60 * 1000L; // 30 minutes
    public static final double DEFAULT_LATITUDE = 40.7128; // New York
    public static final double DEFAULT_LONGITUDE = -74.0060;
    private static final int DEFAULT_L1_CACHE_CAPACITY = 16;

    public static class L1CacheEntry {
        public final WeatherForecast forecast;
        public final long cachedTimestamp;

        public L1CacheEntry(@NonNull WeatherForecast forecast, long cachedTimestamp) {
            this.forecast = forecast;
            this.cachedTimestamp = cachedTimestamp;
        }
    }

    private final WeatherApiService apiService;
    private final WeatherDao weatherDao;
    private final Executor workerExecutor;
    private final Executor mainExecutor;
    private final long cacheTtlMillis;
    private final double defaultLatitude;
    private final double defaultLongitude;

    private final FastLruCache<String, L1CacheEntry> l1MemoryCache;
    private final ResilienceCircuitBreaker circuitBreaker;
    private final AtomicInteger l1HitCount = new AtomicInteger(0);
    private final AtomicInteger l1MissCount = new AtomicInteger(0);

    public OfflineFirstWeatherRepository(@NonNull WeatherApiService apiService,
                                         @NonNull WeatherDao weatherDao) {
        this(
                apiService,
                weatherDao,
                AppExecutors.getInstance().diskIO(),
                AppExecutors.getInstance().mainThread(),
                DEFAULT_CACHE_TTL_MILLIS,
                DEFAULT_LATITUDE,
                DEFAULT_LONGITUDE,
                new ResilienceCircuitBreaker()
        );
    }

    public OfflineFirstWeatherRepository(@NonNull WeatherApiService apiService,
                                         @NonNull WeatherDao weatherDao,
                                         @NonNull Executor workerExecutor,
                                         @NonNull Executor mainExecutor,
                                         long cacheTtlMillis,
                                         double defaultLatitude,
                                         double defaultLongitude) {
        this(
                apiService,
                weatherDao,
                workerExecutor,
                mainExecutor,
                cacheTtlMillis,
                defaultLatitude,
                defaultLongitude,
                new ResilienceCircuitBreaker()
        );
    }

    @VisibleForTesting
    public OfflineFirstWeatherRepository(@NonNull WeatherApiService apiService,
                                         @NonNull WeatherDao weatherDao,
                                         @NonNull Executor workerExecutor,
                                         @NonNull Executor mainExecutor,
                                         long cacheTtlMillis,
                                         double defaultLatitude,
                                         double defaultLongitude,
                                         @NonNull ResilienceCircuitBreaker circuitBreaker) {
        this.apiService = Objects.requireNonNull(apiService, "apiService must not be null");
        this.weatherDao = Objects.requireNonNull(weatherDao, "weatherDao must not be null");
        this.workerExecutor = Objects.requireNonNull(workerExecutor, "workerExecutor must not be null");
        this.mainExecutor = Objects.requireNonNull(mainExecutor, "mainExecutor must not be null");
        this.cacheTtlMillis = cacheTtlMillis;
        this.defaultLatitude = defaultLatitude;
        this.defaultLongitude = defaultLongitude;
        this.circuitBreaker = Objects.requireNonNull(circuitBreaker, "circuitBreaker must not be null");
        this.l1MemoryCache = new FastLruCache<>(DEFAULT_L1_CACHE_CAPACITY);
    }

    @NonNull
    @Override
    public WeatherForecast getSynchronousForecast() {
        String cacheKey = toCacheKey(defaultLatitude, defaultLongitude);
        L1CacheEntry l1 = l1MemoryCache.get(cacheKey);
        if (l1 != null && (System.currentTimeMillis() - l1.cachedTimestamp < cacheTtlMillis)) {
            l1HitCount.incrementAndGet();
            return l1.forecast;
        }

        WeatherForecast cached = loadFromCache();
        if (cached != null) {
            l1MemoryCache.put(cacheKey, new L1CacheEntry(cached, System.currentTimeMillis()));
            return cached;
        }

        if (!circuitBreaker.allowRequest()) {
            return createFallbackForecast();
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
                circuitBreaker.recordSuccess();
                WeatherForecast networkForecast = WeatherDtoMapper.toDomain(response.body());
                saveToCache(networkForecast);
                l1MemoryCache.put(cacheKey, new L1CacheEntry(networkForecast, System.currentTimeMillis()));
                return networkForecast;
            } else {
                circuitBreaker.recordFailure();
            }
        } catch (Exception e) {
            circuitBreaker.recordFailure();
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

        String cacheKey = toCacheKey(latitude, longitude);

        // Tier 1: Instant L1 In-Memory Cache Check (< 0.1ms)
        L1CacheEntry l1Entry = l1MemoryCache.get(cacheKey);
        if (l1Entry != null) {
            long age = System.currentTimeMillis() - l1Entry.cachedTimestamp;
            if (age < cacheTtlMillis) {
                l1HitCount.incrementAndGet();
                mainExecutor.execute(() -> callback.onSuccess(l1Entry.forecast));
                return;
            }
        }
        l1MissCount.incrementAndGet();

        workerExecutor.execute(() -> {
            WeatherForecast cachedForecast = loadFromCache();

            // Tier 2: L2 Room SQLite Persistence Cache Check (~5ms)
            if (cachedForecast != null && isCoordinatesMatchingDefault(latitude, longitude)) {
                WeatherEntity cacheMeta = weatherDao.getWeatherCache();
                if (cacheMeta != null) {
                    long age = System.currentTimeMillis() - cacheMeta.cachedTimestamp;
                    if (age < cacheTtlMillis) {
                        l1MemoryCache.put(cacheKey, new L1CacheEntry(cachedForecast, cacheMeta.cachedTimestamp));
                        mainExecutor.execute(() -> callback.onSuccess(cachedForecast));
                        return;
                    }
                }
            }

            // Circuit Breaker Fast-Fail Check
            if (!circuitBreaker.allowRequest()) {
                WeatherForecast fallback = (l1Entry != null) ? l1Entry.forecast :
                        (cachedForecast != null ? cachedForecast : createFallbackForecast());
                mainExecutor.execute(() -> callback.onSuccess(fallback));
                return;
            }

            // Tier 3: Upstream Open-Meteo REST API Query
            try {
                Response<OpenMeteoWeatherResponse> response = apiService.getForecast(
                        latitude,
                        longitude,
                        WeatherApiService.DEFAULT_CURRENT_METRICS,
                        WeatherApiService.DEFAULT_HOURLY_METRICS,
                        WeatherApiService.DEFAULT_TIMEZONE
                ).execute();

                if (response.isSuccessful() && response.body() != null) {
                    circuitBreaker.recordSuccess();
                    WeatherForecast networkForecast = WeatherDtoMapper.toDomain(response.body());
                    saveToCache(networkForecast);
                    l1MemoryCache.put(cacheKey, new L1CacheEntry(networkForecast, System.currentTimeMillis()));
                    mainExecutor.execute(() -> callback.onSuccess(networkForecast));
                } else {
                    circuitBreaker.recordFailure();
                    handleFetchFailure(cachedForecast, new IOException("HTTP error: " + response.code()), callback);
                }
            } catch (Exception e) {
                circuitBreaker.recordFailure();
                handleFetchFailure(cachedForecast, e, callback);
            }
        });
    }

    private String toCacheKey(double lat, double lon) {
        return String.format(Locale.US, "%.4f,%.4f", lat, lon);
    }

    private boolean isCoordinatesMatchingDefault(double lat, double lon) {
        return Math.abs(lat - defaultLatitude) < 0.001 && Math.abs(lon - defaultLongitude) < 0.001;
    }

    private void handleFetchFailure(WeatherForecast cachedForecast, Exception error,
                                    Callback<WeatherForecast> callback) {
        if (cachedForecast != null) {
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
        }
    }

    private WeatherForecast createFallbackForecast() {
        CurrentWeather cur = new CurrentWeather("Partly Cloudy", 22, "Today | 12:00 PM",
                "H:26  L:17", "10%", "12 Km/h", "60%", "cloudy_sunny");
        List<Hourly> hourly = new ArrayList<>();
        hourly.add(new Hourly("12 pm", 22, "cloudy_sunny"));
        return new WeatherForecast(cur, hourly);
    }

    @NonNull
    public ResilienceCircuitBreaker getCircuitBreaker() {
        return circuitBreaker;
    }

    public int getL1HitCount() {
        return l1HitCount.get();
    }

    public int getL1MissCount() {
        return l1MissCount.get();
    }

    public void clearL1MemoryCache() {
        l1MemoryCache.evictAll();
    }

    public static class HandlerExecutor implements Executor {
        private final Handler handler;

        public HandlerExecutor(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}
