package com.example.fomo.data.remote.api;

import com.example.fomo.data.remote.dto.GeocodingResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit service definition for Open-Meteo Geocoding REST API endpoints.
 */
public interface GeocodingApiService {

    String BASE_URL = "https://geocoding-api.open-meteo.com/";
    String DEFAULT_LANGUAGE = "en";
    String DEFAULT_FORMAT = "json";
    int DEFAULT_SEARCH_COUNT = 10;

    @GET("v1/search")
    Call<GeocodingResponse> searchCities(
            @Query("name") String cityName,
            @Query("count") int count,
            @Query("language") String language,
            @Query("format") String format
    );
}

