package com.example.fomo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Top-level response data transfer object (DTO) returned by the Open-Meteo REST API.
 */
public class OpenMeteoWeatherResponse {

    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("longitude")
    private Double longitude;

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("current")
    private CurrentWeatherDto current;

    @SerializedName("hourly")
    private HourlyForecastDto hourly;

    public OpenMeteoWeatherResponse() {
    }

    public OpenMeteoWeatherResponse(Double latitude, Double longitude, String timezone,
                                  CurrentWeatherDto current, HourlyForecastDto hourly) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.current = current;
        this.hourly = hourly;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public CurrentWeatherDto getCurrent() {
        return current;
    }

    public HourlyForecastDto getHourly() {
        return hourly;
    }
}
