package com.example.fomo.data.remote.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Data transfer object (DTO) representing hourly forecast parallel time-series arrays.
 */
public class HourlyForecastDto {

    @SerializedName("time")
    private List<String> time;

    @SerializedName("temperature_2m")
    private List<Double> temperature2m;

    @SerializedName("weather_code")
    private List<Integer> weatherCode;

    public HourlyForecastDto() {
    }

    public HourlyForecastDto(List<String> time, List<Double> temperature2m, List<Integer> weatherCode) {
        this.time = time;
        this.temperature2m = temperature2m;
        this.weatherCode = weatherCode;
    }

    public List<String> getTime() {
        return time;
    }

    public List<Double> getTemperature2m() {
        return temperature2m;
    }

    public List<Integer> getWeatherCode() {
        return weatherCode;
    }
}
