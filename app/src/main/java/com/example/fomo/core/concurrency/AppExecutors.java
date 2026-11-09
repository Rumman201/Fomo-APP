package com.example.fomo.core.concurrency;

import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Enterprise centralized concurrency management and bounded thread dispatchers.
 * Replaces unpooled rogue thread allocations with dedicated, priority-tuned execution pools
 * to eliminate thread thrashing, memory fragmentation, and ANR starvation.
 */
public class AppExecutors {

    private static volatile AppExecutors sInstance;

    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor computation;
    private final Executor mainThread;

    public AppExecutors() {
        this(
                Executors.newFixedThreadPool(2, new PriorityThreadFactory("fomo-disk-io", Process.THREAD_PRIORITY_BACKGROUND)),
                Executors.newFixedThreadPool(3, new PriorityThreadFactory("fomo-network-io", Process.THREAD_PRIORITY_BACKGROUND)),
                Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors()), new PriorityThreadFactory("fomo-compute", Process.THREAD_PRIORITY_DEFAULT)),
                new MainThreadExecutor()
        );
    }

    @VisibleForTesting
    public AppExecutors(@NonNull Executor diskIO,
                        @NonNull Executor networkIO,
                        @NonNull Executor computation,
                        @NonNull Executor mainThread) {
        this.diskIO = Objects.requireNonNull(diskIO, "diskIO must not be null");
        this.networkIO = Objects.requireNonNull(networkIO, "networkIO must not be null");
        this.computation = Objects.requireNonNull(computation, "computation must not be null");
        this.mainThread = Objects.requireNonNull(mainThread, "mainThread must not be null");
    }

    @NonNull
    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (AppExecutors.class) {
                if (sInstance == null) {
                    sInstance = new AppExecutors();
                }
            }
        }
        return sInstance;
    }

    @NonNull
    public Executor diskIO() {
        return diskIO;
    }

    @NonNull
    public Executor networkIO() {
        return networkIO;
    }

    @NonNull
    public Executor computation() {
        return computation;
    }

    @NonNull
    public Executor mainThread() {
        return mainThread;
    }

    /**
     * Main thread dispatcher backed by Android Main Looper Handler.
     * Includes graceful fallback to direct execution in host JVM test environments where Looper is unmocked.
     */
    public static class MainThreadExecutor implements Executor {
        @Nullable
        private final Handler mainThreadHandler;

        public MainThreadExecutor() {
            Handler handler = null;
            try {
                Looper looper = Looper.getMainLooper();
                if (looper != null) {
                    handler = new Handler(looper);
                }
            } catch (Throwable ignored) {
                // Host JVM test environment where Looper is not mocked
            }
            this.mainThreadHandler = handler;
        }

        public MainThreadExecutor(@Nullable Handler handler) {
            this.mainThreadHandler = handler;
        }

        @Override
        public void execute(@NonNull Runnable command) {
            Objects.requireNonNull(command, "command must not be null");
            if (mainThreadHandler == null) {
                command.run();
                return;
            }
            try {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    command.run();
                } else {
                    mainThreadHandler.post(command);
                }
            } catch (Throwable t) {
                command.run();
            }
        }
    }

    /**
     * ThreadFactory assigning deterministic thread names and Linux OS process priorities.
     */
    public static class PriorityThreadFactory implements ThreadFactory {
        private final String threadNamePrefix;
        private final int threadPriority;
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        public PriorityThreadFactory(@NonNull String prefix, int priority) {
            this.threadNamePrefix = prefix;
            this.threadPriority = priority;
        }

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            Runnable priorityRunnable = () -> {
                try {
                    Process.setThreadPriority(threadPriority);
                } catch (Throwable ignored) {
                    // Ignored on standard host JVM test environments
                }
                runnable.run();
            };
            Thread thread = new Thread(priorityRunnable, threadNamePrefix + "-" + threadNumber.getAndIncrement());
            thread.setDaemon(false);
            return thread;
        }
    }
}

