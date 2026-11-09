package com.example.fomo.core.observability;

import androidx.annotation.NonNull;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Enterprise thread-safe metrics collector capturing Service Level Indicators (SLIs).
 * Tracks application performance indicators including cache hit rates, network request outcomes,
 * circuit breaker trip counts, and error budget consumption without memory allocation overhead.
 */
public class AppMetricsCollector {

    private static volatile AppMetricsCollector sInstance;

    private final AtomicLong totalCacheRequests = new AtomicLong(0);
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong totalNetworkRequests = new AtomicLong(0);
    private final AtomicLong networkSuccesses = new AtomicLong(0);
    private final AtomicLong networkFailures = new AtomicLong(0);
    private final AtomicLong circuitBreakerTrips = new AtomicLong(0);

    public static AppMetricsCollector getInstance() {
        if (sInstance == null) {
            synchronized (AppMetricsCollector.class) {
                if (sInstance == null) {
                    sInstance = new AppMetricsCollector();
                }
            }
        }
        return sInstance;
    }

    public void recordCacheAccess(boolean isHit) {
        totalCacheRequests.incrementAndGet();
        if (isHit) {
            cacheHits.incrementAndGet();
        }
    }

    public void recordNetworkOutcome(boolean isSuccess) {
        totalNetworkRequests.incrementAndGet();
        if (isSuccess) {
            networkSuccesses.incrementAndGet();
        } else {
            networkFailures.incrementAndGet();
        }
    }

    public void recordCircuitBreakerTrip() {
        circuitBreakerTrips.incrementAndGet();
    }

    public double getCacheHitRatio() {
        long total = totalCacheRequests.get();
        if (total == 0) {
            return 1.0;
        }
        return (double) cacheHits.get() / total;
    }

    public double getNetworkSuccessRate() {
        long total = totalNetworkRequests.get();
        if (total == 0) {
            return 1.0;
        }
        return (double) networkSuccesses.get() / total;
    }

    public long getTotalCacheRequests() {
        return totalCacheRequests.get();
    }

    public long getCacheHits() {
        return cacheHits.get();
    }

    public long getTotalNetworkRequests() {
        return totalNetworkRequests.get();
    }

    public long getNetworkSuccesses() {
        return networkSuccesses.get();
    }

    public long getNetworkFailures() {
        return networkFailures.get();
    }

    public long getCircuitBreakerTrips() {
        return circuitBreakerTrips.get();
    }

    public void reset() {
        totalCacheRequests.set(0);
        cacheHits.set(0);
        totalNetworkRequests.set(0);
        networkSuccesses.set(0);
        networkFailures.set(0);
        circuitBreakerTrips.set(0);
    }

    @NonNull
    @Override
    public String toString() {
        return "AppMetrics{" +
                "cacheHitRatio=" + String.format("%.2f%%", getCacheHitRatio() * 100) +
                ", networkSuccessRate=" + String.format("%.2f%%", getNetworkSuccessRate() * 100) +
                ", totalRequests=" + totalNetworkRequests.get() +
                ", failures=" + networkFailures.get() +
                ", breakerTrips=" + circuitBreakerTrips.get() +
                '}';
    }
}

