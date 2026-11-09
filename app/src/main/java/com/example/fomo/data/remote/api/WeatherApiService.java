package com.example.fomo.data.remote.api;

import com.example.fomo.data.remote.dto.OpenMeteoWeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit contract for Open-Meteo meteorological API endpoints.
 */
public interface WeatherApiService {

    String BASE_URL = "https://api.open-meteo.com/";

    String DEFAULT_CURRENT_METRICS = "temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m";
    String DEFAULT_HOURLY_METRICS = "temperature_2m,weather_code";
    String DEFAULT_TIMEZONE = "auto";

    @GET("v1/forecast")
    Call<OpenMeteoWeatherResponse> getForecast(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("current") String currentMetrics,
            @Query("hourly") String hourlyMetrics,
            @Query("timezone") String timezone
    );
}
