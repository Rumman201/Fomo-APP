package com.example.fomo.performance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.core.observability.AppMetricsCollector;
import com.example.fomo.core.observability.HealthCheckService;
import com.example.fomo.core.resilience.ResilienceCircuitBreaker;
import org.junit.Before;
import org.junit.Test;

public class ObservabilityHealthTest {

    private AppMetricsCollector metricsCollector;
    private ResilienceCircuitBreaker circuitBreaker;
    private HealthCheckService healthCheckService;

    @Before
    public void setUp() {
        metricsCollector = AppMetricsCollector.getInstance();
        metricsCollector.reset();
        circuitBreaker = new ResilienceCircuitBreaker(3, 1000L);
        healthCheckService = new HealthCheckService(circuitBreaker);
    }

    @Test
    public void testMetricsCollectorCacheCalculations() {
        metricsCollector.recordCacheAccess(true);
        metricsCollector.recordCacheAccess(true);
        metricsCollector.recordCacheAccess(false);

        assertEquals(3, metricsCollector.getTotalCacheRequests());
        assertEquals(2, metricsCollector.getCacheHits());
        assertEquals(2.0 / 3.0, metricsCollector.getCacheHitRatio(), 0.001);
    }

    @Test
    public void testMetricsCollectorNetworkSuccessRate() {
        metricsCollector.recordNetworkOutcome(true);
        metricsCollector.recordNetworkOutcome(true);
        metricsCollector.recordNetworkOutcome(false);

        assertEquals(3, metricsCollector.getTotalNetworkRequests());
        assertEquals(2, metricsCollector.getNetworkSuccesses());
        assertEquals(1, metricsCollector.getNetworkFailures());
        assertEquals(2.0 / 3.0, metricsCollector.getNetworkSuccessRate(), 0.001);
    }

    @Test
    public void testHealthCheckServiceHealthyState() {
        HealthCheckService.HealthReport report = healthCheckService.evaluateHealth(true, true);
        assertNotNull(report);
        assertTrue(report.isHealthy());
        assertEquals(HealthCheckService.HealthStatus.HEALTHY, report.getStatus());
        assertEquals("UP (Room SQLite v3)", report.getDetails().get("database"));
        assertEquals("UP (AndroidKeyStore AES-256-GCM)", report.getDetails().get("keystore"));
        assertEquals("CLOSED", report.getDetails().get("circuit_breaker"));
    }

    @Test
    public void testHealthCheckServiceDegradedOnCircuitBreakerOpen() {
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure(); // Trips to OPEN

        HealthCheckService.HealthReport report = healthCheckService.evaluateHealth(true, true);
        assertFalse(report.isHealthy());
        assertEquals(HealthCheckService.HealthStatus.DEGRADED, report.getStatus());
        assertEquals("OPEN", report.getDetails().get("circuit_breaker"));
    }

    @Test
    public void testHealthCheckServiceUnhealthyOnDatabaseFailure() {
        HealthCheckService.HealthReport report = healthCheckService.evaluateHealth(false, true);
        assertFalse(report.isHealthy());
        assertEquals(HealthCheckService.HealthStatus.UNHEALTHY, report.getStatus());
        assertEquals("DOWN (SQLite connection failure)", report.getDetails().get("database"));
    }
}

