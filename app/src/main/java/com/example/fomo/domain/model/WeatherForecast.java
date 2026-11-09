package com.example.fomo.domain.model;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Composite domain model representing complete weather intelligence:
 * current observations and hourly forecast projections.
 */
public final class WeatherForecast {
    @NonNull
    private final CurrentWeather currentWeather;
    @NonNull
    private final List<Hourly> hourlyForecasts;

    public WeatherForecast(
            @NonNull CurrentWeather currentWeather,
            @NonNull List<Hourly> hourlyForecasts
    ) {
        this.currentWeather = Objects.requireNonNull(currentWeather, "currentWeather must not be null");
        Objects.requireNonNull(hourlyForecasts, "hourlyForecasts must not be null");
        this.hourlyForecasts = Collections.unmodifiableList(new ArrayList<>(hourlyForecasts));
    }

    @NonNull
    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    @NonNull
    public List<Hourly> getHourlyForecasts() {
        return hourlyForecasts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherForecast that = (WeatherForecast) o;
        return currentWeather.equals(that.currentWeather) &&
                hourlyForecasts.equals(that.hourlyForecasts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentWeather, hourlyForecasts);
    }

    @NonNull
    @Override
    public String toString() {
        return "WeatherForecast{" +
                "currentWeather=" + currentWeather +
                ", hourlyForecastsCount=" + hourlyForecasts.size() +
                '}';
    }
}
