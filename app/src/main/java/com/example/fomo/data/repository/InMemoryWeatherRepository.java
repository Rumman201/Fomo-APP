package com.example.fomo.data.repository;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.example.fomo.domain.model.CurrentWeather;
import com.example.fomo.domain.model.Hourly;
import com.example.fomo.domain.model.WeatherForecast;
import com.example.fomo.domain.repository.WeatherRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * In-memory repository implementation providing baseline weather data.
 * Decouples the UI layer from data generation and serves as the bridge until Phase 3 remote integration.
 */
public class InMemoryWeatherRepository implements WeatherRepository {

    private final Executor workerExecutor;
    private final Executor mainExecutor;

    public InMemoryWeatherRepository() {
        this(
                Executors.newSingleThreadExecutor(),
                command -> new Handler(Looper.getMainLooper()).post(command)
        );
    }

    public InMemoryWeatherRepository(@NonNull Executor workerExecutor, @NonNull Executor mainExecutor) {
        this.workerExecutor = Objects.requireNonNull(workerExecutor, "workerExecutor must not be null");
        this.mainExecutor = Objects.requireNonNull(mainExecutor, "mainExecutor must not be null");
    }

    @NonNull
    @Override
    public WeatherForecast getSynchronousForecast() {
        CurrentWeather current = new CurrentWeather(
                "Mostly Cloudy",
                25,
                "Mon Dec 16 | 2:03 AM",
                "H:27 L:18",
                "22%",
                "12 km/h",
                "18%",
                "cloudy_sunny"
        );

        List<Hourly> hourlyList = new ArrayList<>();
        hourlyList.add(new Hourly("9 PM", 28, "cloudy"));
        hourlyList.add(new Hourly("11 PM", 29, "sun"));
        hourlyList.add(new Hourly("12 AM", 30, "wind"));
        hourlyList.add(new Hourly("1 AM", 29, "rainy"));
        hourlyList.add(new Hourly("2 AM", 27, "storm"));

        return new WeatherForecast(current, hourlyList);
    }

    @Override
    public void getForecastAsync(@NonNull Callback<WeatherForecast> callback) {
        Objects.requireNonNull(callback, "callback must not be null");
        workerExecutor.execute(() -> {
            try {
                WeatherForecast forecast = getSynchronousForecast();
                mainExecutor.execute(() -> callback.onSuccess(forecast));
            } catch (Exception e) {
                mainExecutor.execute(() -> callback.onError(e));
            }
        });
    }

    @Override
    public void getForecastForLocationAsync(double latitude, double longitude, @NonNull Callback<WeatherForecast> callback) {
        // In-memory returns standard forecast
        getForecastAsync(callback);
    }
}
