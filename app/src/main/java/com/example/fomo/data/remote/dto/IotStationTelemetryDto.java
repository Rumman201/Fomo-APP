package com.example.fomo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Data Transfer Object representing incoming telemetry from a personal weather station (PWS).
 */
public class IotStationTelemetryDto {

    @SerializedName("station_id")
    private String stationId;

    @SerializedName("timestamp")
    private long timestamp;

    @SerializedName("ambient_temperature_c")
    private double ambientTemperatureC;

    @SerializedName("relative_humidity_percent")
    private double relativeHumidityPercent;

    @SerializedName("barometric_pressure_hpa")
    private double barometricPressureHpa;

    @SerializedName("wind_speed_kmh")
    private double windSpeedKmh;

    @SerializedName("wind_direction_degrees")
    private int windDirectionDegrees;

    @SerializedName("battery_voltage")
    private double batteryVoltage;

    public IotStationTelemetryDto() {
    }

    public IotStationTelemetryDto(String stationId, long timestamp, double ambientTemperatureC,
                                 double relativeHumidityPercent, double barometricPressureHpa,
                                 double windSpeedKmh, int windDirectionDegrees, double batteryVoltage) {
        this.stationId = stationId;
        this.timestamp = timestamp;
        this.ambientTemperatureC = ambientTemperatureC;
        this.relativeHumidityPercent = relativeHumidityPercent;
        this.barometricPressureHpa = barometricPressureHpa;
        this.windSpeedKmh = windSpeedKmh;
        this.windDirectionDegrees = windDirectionDegrees;
        this.batteryVoltage = batteryVoltage;
    }

    public String getStationId() {
        return stationId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getAmbientTemperatureC() {
        return ambientTemperatureC;
    }

    public double getRelativeHumidityPercent() {
        return relativeHumidityPercent;
    }

    public double getBarometricPressureHpa() {
        return barometricPressureHpa;
    }

    public double getWindSpeedKmh() {
        return windSpeedKmh;
    }

    public int getWindDirectionDegrees() {
        return windDirectionDegrees;
    }

    public double getBatteryVoltage() {
        return batteryVoltage;
    }
}

