package com.example.fomo.performance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.fomo.core.resilience.ResilienceCircuitBreaker;
import org.junit.Before;
import org.junit.Test;

public class ResilienceCircuitBreakerTest {

    private ResilienceCircuitBreaker circuitBreaker;

    @Before
    public void setUp() {
        // Threshold: 3 failures, Cooldown: 100ms for fast testing
        circuitBreaker = new ResilienceCircuitBreaker(3, 100L);
    }

    @Test
    public void testInitialStateIsClosed() {
        assertEquals(ResilienceCircuitBreaker.State.CLOSED, circuitBreaker.getState());
        assertTrue(circuitBreaker.allowRequest());
        assertFalse(circuitBreaker.isOpen());
    }

    @Test
    public void testTripsToOpenAfterThresholdFailures() {
        circuitBreaker.recordFailure();
        assertEquals(ResilienceCircuitBreaker.State.CLOSED, circuitBreaker.getState());
        assertTrue(circuitBreaker.allowRequest());

        circuitBreaker.recordFailure();
        assertEquals(ResilienceCircuitBreaker.State.CLOSED, circuitBreaker.getState());

        // 3rd failure trips the circuit
        circuitBreaker.recordFailure();
        assertEquals(ResilienceCircuitBreaker.State.OPEN, circuitBreaker.getState());
        assertFalse("Circuit must fast-fail and block request in OPEN state", circuitBreaker.allowRequest());
        assertTrue(circuitBreaker.isOpen());
    }

    @Test
    public void testCooldownTransitionsToHalfOpenAndRecoversOnSuccess() throws InterruptedException {
        // Trip to OPEN
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();
        assertTrue(circuitBreaker.isOpen());

        // Wait for cooldown window (100ms)
        Thread.sleep(120L);

        // First call after cooldown must transition to HALF_OPEN and permit probe request
        assertTrue("Breaker must allow probe request after cooldown", circuitBreaker.allowRequest());
        assertEquals(ResilienceCircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());

        // Probe succeeds
        circuitBreaker.recordSuccess();
        assertEquals(ResilienceCircuitBreaker.State.CLOSED, circuitBreaker.getState());
        assertFalse(circuitBreaker.isOpen());
    }

    @Test
    public void testHalfOpenRetripsToOpenOnFailure() throws InterruptedException {
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();
        assertTrue(circuitBreaker.isOpen());

        Thread.sleep(120L);
        assertTrue(circuitBreaker.allowRequest());
        assertEquals(ResilienceCircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());

        // Probe fails
        circuitBreaker.recordFailure();
        assertEquals(ResilienceCircuitBreaker.State.OPEN, circuitBreaker.getState());
    }

    @Test
    public void testManualReset() {
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();
        circuitBreaker.recordFailure();
        assertTrue(circuitBreaker.isOpen());

        circuitBreaker.reset();
        assertEquals(ResilienceCircuitBreaker.State.CLOSED, circuitBreaker.getState());
        assertTrue(circuitBreaker.allowRequest());
    }
}

