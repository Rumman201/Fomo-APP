package com.example.fomo.domain.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.fomo.data.remote.dto.IotStationTelemetryDto;
import com.example.fomo.domain.model.StationReading;
import com.example.fomo.security.sanitizer.InputSanitizer;

/**
 * Service defining and enforcing the IoT Personal Weather Station (PWS) Telemetry Ingestion Contract.
 * Enforces strict physical boundary validation for temperature, humidity, pressure, and battery voltage,
 * as well as station identity format validation and timestamp freshness to prevent telemetry replay attacks.
 */
public class IotWeatherStationService {

    public static final double MIN_TEMPERATURE_C = -90.0;
    public static final double MAX_TEMPERATURE_C = 60.0;
    public static final double MIN_HUMIDITY = 0.0;
    public static final double MAX_HUMIDITY = 100.0;
    public static final double MIN_PRESSURE_HPA = 800.0;
    public static final double MAX_PRESSURE_HPA = 1100.0;
    public static final double MAX_WIND_SPEED_KMH = 350.0;

    public static final long MAX_ALLOWED_AGE_MS = 24 * 60 * 60 * 1000L; // 24 hours
    public static final long MAX_ALLOWED_FUTURE_SKEW_MS = 60 * 60 * 1000L; // 1 hour

    public enum Status {
        SUCCESS,
        CORRUPT_PAYLOAD,
        INVALID_STATION_ID,
        REPLAY_OR_EXPIRED_TIMESTAMP,
        OUT_OF_BOUNDS_TEMPERATURE,
        OUT_OF_BOUNDS_HUMIDITY,
        OUT_OF_BOUNDS_PRESSURE,
        OUT_OF_BOUNDS_WIND
    }

    public static class IngestionResult {
        private final Status status;
        private final String message;
        private final StationReading reading;

        public IngestionResult(@NonNull Status status, @NonNull String message, @Nullable StationReading reading) {
            this.status = status;
            this.message = message;
            this.reading = reading;
        }

        public Status getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public boolean isSuccess() {
            return status == Status.SUCCESS;
        }

        @Nullable
        public StationReading getReading() {
            return reading;
        }
    }

    /**
     * Validates and ingests personal weather station telemetry.
     */
    @NonNull
    public IngestionResult ingest(@Nullable IotStationTelemetryDto dto) {
        if (dto == null || dto.getStationId() == null || dto.getStationId().trim().isEmpty()) {
            return new IngestionResult(Status.CORRUPT_PAYLOAD, "Station payload is null or missing station identifier.", null);
        }

        if (!InputSanitizer.isValidStationId(dto.getStationId())) {
            return new IngestionResult(
                    Status.INVALID_STATION_ID,
                    "Station identifier '" + dto.getStationId() + "' fails strict format validation.",
                    null
            );
        }

        long now = System.currentTimeMillis();
        long telemetryTime = dto.getTimestamp() > 0 ? dto.getTimestamp() : now;

        if (telemetryTime < (now - MAX_ALLOWED_AGE_MS) || telemetryTime > (now + MAX_ALLOWED_FUTURE_SKEW_MS)) {
            return new IngestionResult(
                    Status.REPLAY_OR_EXPIRED_TIMESTAMP,
                    "Telemetry timestamp (" + telemetryTime + ") outside allowed freshness window (possible replay attack).",
                    null
            );
        }

        if (dto.getAmbientTemperatureC() < MIN_TEMPERATURE_C || dto.getAmbientTemperatureC() > MAX_TEMPERATURE_C) {
            return new IngestionResult(
                    Status.OUT_OF_BOUNDS_TEMPERATURE,
                    "Temperature (" + dto.getAmbientTemperatureC() + "°C) exceeds physical bounds [-90°C, +60°C].",
                    null
            );
        }

        if (dto.getRelativeHumidityPercent() < MIN_HUMIDITY || dto.getRelativeHumidityPercent() > MAX_HUMIDITY) {
            return new IngestionResult(
                    Status.OUT_OF_BOUNDS_HUMIDITY,
                    "Humidity (" + dto.getRelativeHumidityPercent() + "%) exceeds physical bounds [0%, 100%].",
                    null
            );
        }

        if (dto.getBarometricPressureHpa() < MIN_PRESSURE_HPA || dto.getBarometricPressureHpa() > MAX_PRESSURE_HPA) {
            return new IngestionResult(
                    Status.OUT_OF_BOUNDS_PRESSURE,
                    "Barometric pressure (" + dto.getBarometricPressureHpa() + " hPa) exceeds physical bounds [800, 1100 hPa].",
                    null
            );
        }

        if (dto.getWindSpeedKmh() < 0.0 || dto.getWindSpeedKmh() > MAX_WIND_SPEED_KMH) {
            return new IngestionResult(
                    Status.OUT_OF_BOUNDS_WIND,
                    "Wind speed (" + dto.getWindSpeedKmh() + " km/h) exceeds physical bounds [0, 350 km/h].",
                    null
            );
        }

        StationReading reading = new StationReading(
                dto.getStationId(),
                telemetryTime,
                dto.getAmbientTemperatureC(),
                dto.getRelativeHumidityPercent(),
                dto.getBarometricPressureHpa(),
                dto.getWindSpeedKmh(),
                dto.getWindDirectionDegrees(),
                dto.getBatteryVoltage()
        );

        return new IngestionResult(Status.SUCCESS, "Telemetry successfully validated and ingested.", reading);
    }

    /**
     * Deterministic simulator generating valid station telemetry for contract testing.
     */
    @NonNull
    public IotStationTelemetryDto generateSimulatedTelemetry(@NonNull String stationId) {
        return new IotStationTelemetryDto(
                stationId,
                System.currentTimeMillis(),
                23.5,
                55.0,
                1013.25,
                12.5,
                180,
                3.3
        );
    }
}
