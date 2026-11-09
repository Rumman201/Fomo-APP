package com.example.fomo.qa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.data.remote.dto.IotStationTelemetryDto;
import com.example.fomo.domain.service.IotWeatherStationService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Phase 9 IoT Telemetry Boundary and Protocol Red Teaming test suite.
 * Tests anti-replay timestamp expiration, corrupted NaN/Infinity sensor payloads,
 * alphanumeric station identifier sanitization, and batch size caps.
 */
public class IotTelemetryBoundaryRedTeamTest {

    private IotWeatherStationService stationService;

    @Before
    public void setUp() {
        stationService = new IotWeatherStationService();
    }

    @Test
    public void testReplayAttackExpiredTimestampRejected() {
        long now = System.currentTimeMillis();
        long expiredTime = now - (25 * 60 * 60 * 1000L); // 25 hours ago (> 24h limit)

        IotStationTelemetryDto dto = new IotStationTelemetryDto(
                "STATION_NYC_01", expiredTime, 22.0, 50.0, 1013.25, 15.0, 180, 3.7
        );

        IotWeatherStationService.IngestionResult result = stationService.ingest(dto);
        assertFalse(result.isSuccess());
        assertEquals(IotWeatherStationService.Status.REPLAY_OR_EXPIRED_TIMESTAMP, result.getStatus());
    }

    @Test
    public void testFutureSkewBeyondOneHourRejected() {
        long now = System.currentTimeMillis();
        long futureSkewedTime = now + (2 * 60 * 60 * 1000L); // 2 hours in future (> 1h limit)

        IotStationTelemetryDto dto = new IotStationTelemetryDto(
                "STATION_NYC_01", futureSkewedTime, 22.0, 50.0, 1013.25, 15.0, 180, 3.7
        );

        IotWeatherStationService.IngestionResult result = stationService.ingest(dto);
        assertFalse(result.isSuccess());
        assertEquals(IotWeatherStationService.Status.REPLAY_OR_EXPIRED_TIMESTAMP, result.getStatus());
    }

    @Test
    public void testCorruptedNaNTimestampAndReadingsRejected() {
        long now = System.currentTimeMillis();

        // NaN Temperature (Regression verification for DEF-QA-01)
        IotStationTelemetryDto nanTempDto = new IotStationTelemetryDto(
                "STATION_NYC_01", now, Double.NaN, 50.0, 1013.25, 15.0, 180, 3.7
        );
        IotWeatherStationService.IngestionResult tempResult = stationService.ingest(nanTempDto);
        assertFalse("NaN temperature must be rejected", tempResult.isSuccess());
        assertEquals(IotWeatherStationService.Status.OUT_OF_BOUNDS_TEMPERATURE, tempResult.getStatus());

        // NaN Humidity
        IotStationTelemetryDto nanHumidityDto = new IotStationTelemetryDto(
                "STATION_NYC_01", now, 22.0, Double.NaN, 1013.25, 15.0, 180, 3.7
        );
        IotWeatherStationService.IngestionResult humidityResult = stationService.ingest(nanHumidityDto);
        assertFalse("NaN humidity must be rejected", humidityResult.isSuccess());
        assertEquals(IotWeatherStationService.Status.OUT_OF_BOUNDS_HUMIDITY, humidityResult.getStatus());

        // Infinity Pressure
        IotStationTelemetryDto infPressureDto = new IotStationTelemetryDto(
                "STATION_NYC_01", now, 22.0, 50.0, Double.POSITIVE_INFINITY, 15.0, 180, 3.7
        );
        IotWeatherStationService.IngestionResult pressureResult = stationService.ingest(infPressureDto);
        assertFalse("Infinite pressure must be rejected", pressureResult.isSuccess());
        assertEquals(IotWeatherStationService.Status.OUT_OF_BOUNDS_PRESSURE, pressureResult.getStatus());
    }

    @Test
    public void testMaliciousStationIdFormatRejected() {
        long now = System.currentTimeMillis();

        // XSS script injection in station ID
        IotStationTelemetryDto scriptDto = new IotStationTelemetryDto(
                "<script>bad()</script>", now, 22.0, 50.0, 1013.25, 15.0, 180, 3.7
        );
        IotWeatherStationService.IngestionResult scriptResult = stationService.ingest(scriptDto);
        assertFalse(scriptResult.isSuccess());
        assertEquals(IotWeatherStationService.Status.INVALID_STATION_ID, scriptResult.getStatus());

        // SQL injection in station ID
        IotStationTelemetryDto sqliDto = new IotStationTelemetryDto(
                "STN'; DROP TABLE--", now, 22.0, 50.0, 1013.25, 15.0, 180, 3.7
        );
        IotWeatherStationService.IngestionResult sqliResult = stationService.ingest(sqliDto);
        assertFalse(sqliResult.isSuccess());
        assertEquals(IotWeatherStationService.Status.INVALID_STATION_ID, sqliResult.getStatus());
    }

    @Test
    public void testBatchIngestionBoundaryCap() {
        List<IotStationTelemetryDto> batch = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (int i = 0; i < 50; i++) {
            batch.add(new IotStationTelemetryDto(
                    "STN_VALID_01", now, 20.0, 50.0, 1013.25, 10.0, 180, 3.7
            ));
        }

        IotWeatherStationService.BatchIngestionResult result = stationService.ingestBatch(batch);
        assertNotNull(result);
        assertEquals(50, result.getTotalReceived());
        assertEquals(50, result.getSuccessCount());
        assertEquals(0, result.getRejectedCount());
    }
}
