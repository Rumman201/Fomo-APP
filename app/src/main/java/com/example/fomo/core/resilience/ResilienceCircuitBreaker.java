package com.example.fomo.core.resilience;

import androidx.annotation.NonNull;

/**
 * Enterprise thread-safe Circuit Breaker implementing the standard state pattern:
 * CLOSED -> OPEN -> HALF-OPEN -> CLOSED.
 * Protects downstream mobile resources and prevents battery-draining request storms
 * when upstream APIs experience outages or consecutive HTTP 5xx failures.
 */
public class ResilienceCircuitBreaker {

    public enum State {
        CLOSED,
        OPEN,
        HALF_OPEN
    }

    public static final int DEFAULT_FAILURE_THRESHOLD = 3;
    public static final long DEFAULT_COOLDOWN_MILLIS = 30_000L; // 30 seconds

    private final int failureThreshold;
    private final long cooldownMillis;

    private State state;
    private int consecutiveFailures;
    private long lastFailureTimestamp;

    public ResilienceCircuitBreaker() {
        this(DEFAULT_FAILURE_THRESHOLD, DEFAULT_COOLDOWN_MILLIS);
    }

    public ResilienceCircuitBreaker(int failureThreshold, long cooldownMillis) {
        this.failureThreshold = Math.max(1, failureThreshold);
        this.cooldownMillis = Math.max(1L, cooldownMillis);
        this.state = State.CLOSED;
        this.consecutiveFailures = 0;
        this.lastFailureTimestamp = 0L;
    }

    /**
     * Determines whether an outbound execution is permitted or must fail fast.
     */
    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();

        if (state == State.OPEN) {
            if (now - lastFailureTimestamp >= cooldownMillis) {
                state = State.HALF_OPEN;
                return true; // Permit single probe request
            }
            return false; // Circuit is open: fast-fail immediately
        }

        return true;
    }

    /**
     * Records a successful upstream transaction, resetting the breaker to CLOSED.
     */
    public synchronized void recordSuccess() {
        consecutiveFailures = 0;
        state = State.CLOSED;
    }

    /**
     * Records an upstream error, incrementing failure count and tripping to OPEN if threshold exceeded.
     */
    public synchronized void recordFailure() {
        consecutiveFailures++;
        lastFailureTimestamp = System.currentTimeMillis();

        if (state == State.HALF_OPEN || consecutiveFailures >= failureThreshold) {
            state = State.OPEN;
        }
    }

    /**
     * Returns current state of the circuit breaker.
     */
    @NonNull
    public synchronized State getState() {
        long now = System.currentTimeMillis();
        if (state == State.OPEN && (now - lastFailureTimestamp >= cooldownMillis)) {
            return State.HALF_OPEN;
        }
        return state;
    }

    public synchronized int getConsecutiveFailures() {
        return consecutiveFailures;
    }

    public synchronized boolean isOpen() {
        return getState() == State.OPEN;
    }

    /**
     * Resets breaker to pristine CLOSED state.
     */
    public synchronized void reset() {
        state = State.CLOSED;
        consecutiveFailures = 0;
        lastFailureTimestamp = 0L;
    }
}
