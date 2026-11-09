package com.example.fomo.qa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.core.resilience.ResilienceCircuitBreaker;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Before;
import org.junit.Test;

/**
 * Phase 9 Chaos and Fault Injection verification suite.
 * Simulates upstream API degradation, network flapping, thread concurrency stress,
 * and verifies rapid fast-fail gating and recovery under failure conditions.
 */
public class ChaosFaultInjectionTest {

    private ResilienceCircuitBreaker breaker;

    @Before
    public void setUp() {
        breaker = new ResilienceCircuitBreaker(3, 150L); // 3 failures, 150ms cooldown
    }

    @Test
    public void testClosedToOpenTransitionOnConsecutiveFailures() {
        assertEquals(ResilienceCircuitBreaker.State.CLOSED, breaker.getState());
        assertTrue(breaker.allowRequest());

        breaker.recordFailure();
        assertEquals(ResilienceCircuitBreaker.State.CLOSED, breaker.getState());
        assertEquals(1, breaker.getConsecutiveFailures());

        breaker.recordFailure();
        assertEquals(ResilienceCircuitBreaker.State.CLOSED, breaker.getState());
        assertEquals(2, breaker.getConsecutiveFailures());

        breaker.recordFailure(); // Exceeds threshold of 3
        assertEquals(ResilienceCircuitBreaker.State.OPEN, breaker.getState());
        assertFalse(breaker.allowRequest());
    }

    @Test
    public void testSuccessResetsFailureCounterInClosedState() {
        breaker.recordFailure();
        breaker.recordFailure();
        assertEquals(2, breaker.getConsecutiveFailures());

        breaker.recordSuccess();
        assertEquals(0, breaker.getConsecutiveFailures());
        assertEquals(ResilienceCircuitBreaker.State.CLOSED, breaker.getState());
        assertTrue(breaker.allowRequest());
    }

    @Test
    public void testHalfOpenProbeSuccessTransitionsBackToClosed() throws InterruptedException {
        // Trip to OPEN
        breaker.recordFailure();
        breaker.recordFailure();
        breaker.recordFailure();
        assertEquals(ResilienceCircuitBreaker.State.OPEN, breaker.getState());
        assertFalse(breaker.allowRequest());

        // Wait for cooldown
        Thread.sleep(180L);

        // First request after cooldown permits single probe and enters HALF_OPEN
        assertTrue(breaker.allowRequest());
        assertEquals(ResilienceCircuitBreaker.State.HALF_OPEN, breaker.getState());

        // Successful probe resets circuit to CLOSED
        breaker.recordSuccess();
        assertEquals(ResilienceCircuitBreaker.State.CLOSED, breaker.getState());
        assertEquals(0, breaker.getConsecutiveFailures());
        assertTrue(breaker.allowRequest());
    }

    @Test
    public void testHalfOpenProbeFailureTripsBackToOpen() throws InterruptedException {
        // Trip to OPEN
        breaker.recordFailure();
        breaker.recordFailure();
        breaker.recordFailure();

        Thread.sleep(180L);

        // Probe request transitions to HALF_OPEN
        assertTrue(breaker.allowRequest());
        assertEquals(ResilienceCircuitBreaker.State.HALF_OPEN, breaker.getState());

        // Failed probe immediately trips back to OPEN
        breaker.recordFailure();
        assertEquals(ResilienceCircuitBreaker.State.OPEN, breaker.getState());
        assertFalse(breaker.allowRequest());
    }

    @Test
    public void testConcurrentChaosUnderExtremeThreadContention() throws InterruptedException {
        int threadCount = 16;
        int operationsPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger allowedCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.execute(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        if (breaker.allowRequest()) {
                            allowedCount.incrementAndGet();
                            if (threadId % 2 == 0) {
                                breaker.recordSuccess();
                            } else {
                                breaker.recordFailure();
                            }
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertNotNull(breaker.getState());
        assertTrue(allowedCount.get() > 0);
    }
}

