package com.example.fomo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Data transfer object (DTO) representing current weather metrics from Open-Meteo.
 */
public class CurrentWeatherDto {

    @SerializedName("time")
    private String time;

    @SerializedName("temperature_2m")
    private Double temperature2m;

    @SerializedName("relative_humidity_2m")
    private Integer relativeHumidity2m;

    @SerializedName("apparent_temperature")
    private Double apparentTemperature;

    @SerializedName("precipitation")
    private Double precipitation;

    @SerializedName("weather_code")
    private Integer weatherCode;

    @SerializedName("wind_speed_10m")
    private Double windSpeed10m;

    public CurrentWeatherDto() {
    }

    public CurrentWeatherDto(String time, Double temperature2m, Integer relativeHumidity2m,
                           Double apparentTemperature, Double precipitation,
                           Integer weatherCode, Double windSpeed10m) {
        this.time = time;
        this.temperature2m = temperature2m;
        this.relativeHumidity2m = relativeHumidity2m;
        this.apparentTemperature = apparentTemperature;
        this.precipitation = precipitation;
        this.weatherCode = weatherCode;
        this.windSpeed10m = windSpeed10m;
    }

    public String getTime() {
        return time;
    }

    public Double getTemperature2m() {
        return temperature2m;
    }

    public Integer getRelativeHumidity2m() {
        return relativeHumidity2m;
    }

    public Double getApparentTemperature() {
        return apparentTemperature;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public Integer getWeatherCode() {
        return weatherCode;
    }

    public Double getWindSpeed10m() {
        return windSpeed10m;
    }
}
