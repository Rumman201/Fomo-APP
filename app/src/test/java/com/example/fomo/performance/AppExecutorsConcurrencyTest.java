package com.example.fomo.performance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.core.concurrency.AppExecutors;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;

public class AppExecutorsConcurrencyTest {

    @Test
    public void testDiskIoExecutesOnBackgroundThreadWithNamingPrefix() throws InterruptedException {
        AppExecutors executors = AppExecutors.getInstance();
        assertNotNull(executors.diskIO());
        assertNotNull(executors.networkIO());
        assertNotNull(executors.computation());

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> threadNameRef = new AtomicReference<>();

        executors.diskIO().execute(() -> {
            threadNameRef.set(Thread.currentThread().getName());
            latch.countDown();
        });

        assertTrue(latch.await(3, TimeUnit.SECONDS));
        assertNotNull(threadNameRef.get());
        assertTrue("Thread name must carry deterministic prefix", threadNameRef.get().startsWith("fomo-disk-io"));
    }

    @Test
    public void testNetworkIoExecutesWithNamingPrefix() throws InterruptedException {
        AppExecutors executors = AppExecutors.getInstance();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> threadNameRef = new AtomicReference<>();

        executors.networkIO().execute(() -> {
            threadNameRef.set(Thread.currentThread().getName());
            latch.countDown();
        });

        assertTrue(latch.await(3, TimeUnit.SECONDS));
        assertNotNull(threadNameRef.get());
        assertTrue("Network thread must carry deterministic prefix", threadNameRef.get().startsWith("fomo-network-io"));
    }

    @Test
    public void testConcurrentExecutionAcrossDispatchers() throws InterruptedException {
        AppExecutors executors = AppExecutors.getInstance();
        int tasks = 20;
        CountDownLatch latch = new CountDownLatch(tasks);

        for (int i = 0; i < tasks; i++) {
            final int index = i;
            if (index % 2 == 0) {
                executors.diskIO().execute(latch::countDown);
            } else {
                executors.computation().execute(latch::countDown);
            }
        }

        assertTrue("All bounded concurrent tasks must complete without deadlock", latch.await(5, TimeUnit.SECONDS));
        assertEquals(0, latch.getCount());
    }
}

