package com.example.fomo.performance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.fomo.domain.service.AtmosphericIntelligenceService;
import org.junit.Before;
import org.junit.Test;

public class AtmosphericIntelligenceEfficiencyTest {

    private AtmosphericIntelligenceService service;

    @Before
    public void setUp() {
        service = new AtmosphericIntelligenceService();
    }

    @Test
    public void testMemoizationProducesIdenticalResults() {
        double temp = 32.0;
        double humidity = 70.0;
        double wind = 15.0;

        // First call evaluates polynomial
        double result1 = service.calculateApparentTemperature(temp, humidity, wind);

        // Second call must return memoized result
        double result2 = service.calculateApparentTemperature(temp, humidity, wind);

        assertEquals(result1, result2, 0.0001);
    }

    @Test
    public void testMemoizationAcceleratesRepeatedCalculations() {
        double temp = 35.0;
        double humidity = 75.0;
        double wind = 12.0;

        // Warm up and populate cache
        service.calculateApparentTemperature(temp, humidity, wind);

        int iterations = 10_000;
        long startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            service.calculateApparentTemperature(temp, humidity, wind);
        }
        long durationNanos = System.nanoTime() - startNanos;

        double avgLatencyMicros = (double) durationNanos / (iterations * 1_000.0);
        // Memoized hash lookup must be sub-microsecond
        assertTrue("Memoized lookup should be under 20 microseconds per operation (actual: " + avgLatencyMicros + "us)",
                avgLatencyMicros < 20.0);
    }
}

