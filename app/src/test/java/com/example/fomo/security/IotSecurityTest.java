package com.example.fomo.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.data.remote.dto.IotStationTelemetryDto;
import com.example.fomo.domain.service.IotWeatherStationService;
import org.junit.Before;
import org.junit.Test;

public class IotSecurityTest {

    private IotWeatherStationService service;

    @Before
    public void setUp() {
        service = new IotWeatherStationService();
    }

    @Test
    public void testRejectMaliciousStationIdPattern() {
        IotStationTelemetryDto maliciousDto = new IotStationTelemetryDto(
                "pws;DROP TABLE cities;--",
                System.currentTimeMillis(),
                22.0, 50.0, 1013.0, 10.0, 180, 3.3
        );

        IotWeatherStationService.IngestionResult result = service.ingest(maliciousDto);
        assertFalse(result.isSuccess());
        assertEquals(IotWeatherStationService.Status.INVALID_STATION_ID, result.getStatus());
        assertNull(result.getReading());
    }

    @Test
    public void testRejectReplayAttackWithExpiredTimestamp() {
        // 48 hours in past
        long expiredTimestamp = System.currentTimeMillis() - (48 * 60 * 60 * 1000L);
        IotStationTelemetryDto replayedDto = new IotStationTelemetryDto(
                "PWS-8842",
                expiredTimestamp,
                22.0, 50.0, 1013.0, 10.0, 180, 3.3
        );

        IotWeatherStationService.IngestionResult result = service.ingest(replayedDto);
        assertFalse(result.isSuccess());
        assertEquals(IotWeatherStationService.Status.REPLAY_OR_EXPIRED_TIMESTAMP, result.getStatus());
        assertNull(result.getReading());
    }

    @Test
    public void testRejectFutureSkewedTimestamp() {
        // 3 hours in future
        long futureTimestamp = System.currentTimeMillis() + (3 * 60 * 60 * 1000L);
        IotStationTelemetryDto skewedDto = new IotStationTelemetryDto(
                "PWS-8842",
                futureTimestamp,
                22.0, 50.0, 1013.0, 10.0, 180, 3.3
        );

        IotWeatherStationService.IngestionResult result = service.ingest(skewedDto);
        assertFalse(result.isSuccess());
        assertEquals(IotWeatherStationService.Status.REPLAY_OR_EXPIRED_TIMESTAMP, result.getStatus());
    }

    @Test
    public void testAcceptValidTelemetryWithinFreshnessWindow() {
        // 5 minutes ago
        long validTimestamp = System.currentTimeMillis() - (5 * 60 * 1000L);
        IotStationTelemetryDto validDto = new IotStationTelemetryDto(
                "PWS-8842",
                validTimestamp,
                21.5, 62.0, 1012.8, 8.4, 90, 3.29
        );

        IotWeatherStationService.IngestionResult result = service.ingest(validDto);
        assertTrue(result.isSuccess());
        assertEquals(IotWeatherStationService.Status.SUCCESS, result.getStatus());
    }
}

