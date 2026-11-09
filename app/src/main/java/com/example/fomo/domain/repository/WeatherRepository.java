package com.example.fomo.domain.repository;

import androidx.annotation.NonNull;
import com.example.fomo.domain.model.WeatherForecast;

/**
 * Domain contract for retrieving weather intelligence data.
 * Decouples presentation from data origin (memory cache, local database, or remote API).
 */
public interface WeatherRepository {

    interface Callback<T> {
        void onSuccess(@NonNull T data);
        void onError(@NonNull Throwable error);
    }

    /**
     * Retrieves the current forecast synchronously from the available cache.
     *
     * @return Cached or default WeatherForecast
     */
    @NonNull
    WeatherForecast getSynchronousForecast();

    /**
     * Asynchronously fetches fresh weather forecast data for the default location.
     *
     * @param callback Callback notified upon completion or error
     */
    void getForecastAsync(@NonNull Callback<WeatherForecast> callback);

    /**
     * Asynchronously fetches fresh weather forecast data for specific geographic coordinates.
     *
     * @param latitude Target latitude in decimal degrees
     * @param longitude Target longitude in decimal degrees
     * @param callback Callback notified upon completion or error
     */
    void getForecastForLocationAsync(double latitude, double longitude, @NonNull Callback<WeatherForecast> callback);
}
