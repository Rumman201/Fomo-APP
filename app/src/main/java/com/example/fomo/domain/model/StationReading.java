package com.example.fomo.domain.model;

import androidx.annotation.NonNull;
import java.util.Objects;

/**
 * Immutable domain model representing a validated hyper-local weather station reading.
 */
public final class StationReading {

    private final String stationId;
    private final long timestamp;
    private final double temperatureC;
    private final double humidityPercent;
    private final double pressureHpa;
    private final double windSpeedKmh;
    private final int windDirectionDegrees;
    private final double batteryVoltage;

    public StationReading(@NonNull String stationId,
                          long timestamp,
                          double temperatureC,
                          double humidityPercent,
                          double pressureHpa,
                          double windSpeedKmh,
                          int windDirectionDegrees,
                          double batteryVoltage) {
        this.stationId = Objects.requireNonNull(stationId, "stationId must not be null");
        this.timestamp = timestamp;
        this.temperatureC = temperatureC;
        this.humidityPercent = humidityPercent;
        this.pressureHpa = pressureHpa;
        this.windSpeedKmh = windSpeedKmh;
        this.windDirectionDegrees = windDirectionDegrees;
        this.batteryVoltage = batteryVoltage;
    }

    @NonNull
    public String getStationId() {
        return stationId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getTemperatureC() {
        return temperatureC;
    }

    public double getHumidityPercent() {
        return humidityPercent;
    }

    public double getPressureHpa() {
        return pressureHpa;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StationReading)) return false;
        StationReading that = (StationReading) o;
        return timestamp == that.timestamp &&
                Double.compare(that.temperatureC, temperatureC) == 0 &&
                Double.compare(that.humidityPercent, humidityPercent) == 0 &&
                Double.compare(that.pressureHpa, pressureHpa) == 0 &&
                Double.compare(that.windSpeedKmh, windSpeedKmh) == 0 &&
                windDirectionDegrees == that.windDirectionDegrees &&
                Double.compare(that.batteryVoltage, batteryVoltage) == 0 &&
                stationId.equals(that.stationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationId, timestamp, temperatureC, humidityPercent,
                pressureHpa, windSpeedKmh, windDirectionDegrees, batteryVoltage);
    }
}

