package com.example.fomo.performance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.data.remote.dto.IotStationTelemetryDto;
import com.example.fomo.domain.service.IotWeatherStationService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class IotBatchIngestionPerformanceTest {

    private IotWeatherStationService service;

    @Before
    public void setUp() {
        service = new IotWeatherStationService();
    }

    @Test
    public void testBatchIngestionProcessesAllValidItems() {
        int batchSize = 100;
        List<IotStationTelemetryDto> batch = new ArrayList<>(batchSize);
        long now = System.currentTimeMillis();

        for (int i = 0; i < batchSize; i++) {
            batch.add(new IotStationTelemetryDto(
                    "PWS-" + (1000 + i),
                    now - (i * 1000L),
                    20.0 + (i % 10),
                    50.0 + (i % 20),
                    1013.25,
                    10.0,
                    180,
                    3.3
            ));
        }

        IotWeatherStationService.BatchIngestionResult result = service.ingestBatch(batch);
        assertNotNull(result);
        assertEquals(batchSize, result.getTotalReceived());
        assertEquals(batchSize, result.getSuccessCount());
        assertEquals(0, result.getRejectedCount());
        assertEquals(batchSize, result.getAcceptedReadings().size());
        assertTrue("Batch processing duration should be positive", result.getDurationNanos() > 0);
        assertTrue("Batch throughput should exceed 1,000 items/sec (actual: " + result.getThroughputPerSecond() + ")",
                result.getThroughputPerSecond() > 1_000.0);
    }

    @Test
    public void testBatchSeparatesValidAndCorruptItems() {
        List<IotStationTelemetryDto> batch = new ArrayList<>();
        // 2 valid items
        batch.add(service.generateSimulatedTelemetry("STATION-01"));
        batch.add(service.generateSimulatedTelemetry("STATION-02"));
        // 1 corrupted item
        batch.add(new IotStationTelemetryDto("invalid;name", System.currentTimeMillis(), 20, 50, 1013, 10, 0, 3.3));

        IotWeatherStationService.BatchIngestionResult result = service.ingestBatch(batch);
        assertEquals(3, result.getTotalReceived());
        assertEquals(2, result.getSuccessCount());
        assertEquals(1, result.getRejectedCount());
        assertEquals(2, result.getAcceptedReadings().size());
    }
}

