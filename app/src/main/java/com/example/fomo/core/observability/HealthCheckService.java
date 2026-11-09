package com.example.fomo.core.observability;

import androidx.annotation.NonNull;
import com.example.fomo.core.resilience.ResilienceCircuitBreaker;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Enterprise operational health check diagnostics.
 * Evaluates operational subsystem viability (storage, hardware keystore, network circuit breaker)
 * to support telemetry collection, automated monitoring, and incident diagnostics.
 */
public class HealthCheckService {

    public enum HealthStatus {
        HEALTHY,
        DEGRADED,
        UNHEALTHY
    }

    public static class HealthReport {
        private final HealthStatus status;
        private final Map<String, String> details;
        private final long timestamp;

        public HealthReport(@NonNull HealthStatus status, @NonNull Map<String, String> details) {
            this.status = Objects.requireNonNull(status);
            this.details = Collections.unmodifiableMap(new HashMap<>(details));
            this.timestamp = System.currentTimeMillis();
        }

        @NonNull
        public HealthStatus getStatus() {
            return status;
        }

        @NonNull
        public Map<String, String> getDetails() {
            return details;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public boolean isHealthy() {
            return status == HealthStatus.HEALTHY;
        }
    }

    private final ResilienceCircuitBreaker circuitBreaker;

    public HealthCheckService(@NonNull ResilienceCircuitBreaker circuitBreaker) {
        this.circuitBreaker = Objects.requireNonNull(circuitBreaker);
    }

    /**
     * Conducts a non-destructive multi-system health assessment.
     */
    @NonNull
    public HealthReport evaluateHealth(boolean isDatabaseAccessible, boolean isKeystoreFunctional) {
        Map<String, String> details = new HashMap<>();
        HealthStatus overallStatus = HealthStatus.HEALTHY;

        // 1. Database check
        if (isDatabaseAccessible) {
            details.put("database", "UP (Room SQLite v3)");
        } else {
            details.put("database", "DOWN (SQLite connection failure)");
            overallStatus = HealthStatus.UNHEALTHY;
        }

        // 2. Keystore check
        if (isKeystoreFunctional) {
            details.put("keystore", "UP (AndroidKeyStore AES-256-GCM)");
        } else {
            details.put("keystore", "DOWN (Keystore authentication error)");
            overallStatus = (overallStatus == HealthStatus.UNHEALTHY) ? HealthStatus.UNHEALTHY : HealthStatus.DEGRADED;
        }

        // 3. Circuit breaker check
        ResilienceCircuitBreaker.State state = circuitBreaker.getState();
        details.put("circuit_breaker", state.name());
        if (state == ResilienceCircuitBreaker.State.OPEN) {
            overallStatus = (overallStatus == HealthStatus.UNHEALTHY) ? HealthStatus.UNHEALTHY : HealthStatus.DEGRADED;
        }

        // 4. Metrics collector summary
        AppMetricsCollector metrics = AppMetricsCollector.getInstance();
        details.put("cache_hit_ratio", String.format("%.2f%%", metrics.getCacheHitRatio() * 100));
        details.put("network_success_rate", String.format("%.2f%%", metrics.getNetworkSuccessRate() * 100));

        return new HealthReport(overallStatus, details);
    }
}

