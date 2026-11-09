---
name: distributed-processing-reliability
description: Asynchronous concurrency, background executor dispatching, retry mechanisms, exponential backoff, and thread-safety standards.
---

# Distributed Processing & Reliability Skill

## Purpose & Scope
This skill governs background asynchronous execution, concurrency models, thread dispatching, retry logic, and resilience for client-server and mobile data processing pipelines.

## When to Activate
- Designing or reviewing background worker threads, executors, or coroutine dispatchers.
- Implementing retry algorithms with exponential backoff and jitter for transient network failures.
- Ensuring thread safety when writing to in-memory caches or persistent databases.
- Managing task cancellation and resource cleanup across Android lifecycle boundaries.

## Engineering Standards & Invariants
1. **Thread Confinement**:
   - Disk and network I/O operations must strictly run on background worker threads (`Executors.newSingleThreadExecutor()` or `Schedulers.io()`).
   - UI updates and LiveData state notifications must strictly run on the Main/UI thread.
2. **Safe Retries & Backoff**:
   - Only idempotent operations (e.g. GET requests) may be retried automatically.
   - Retries must be bounded (e.g., maximum 3 attempts) and implement exponential backoff with jitter to prevent thundering herd problems.
3. **Graceful Cancellation**:
   - Ensure in-flight background operations support cooperative cancellation or safe timeout disconnection to avoid resource leaks.
4. **Deterministic Testing**:
   - Injectable executors must be used across all asynchronous components so tests can execute synchronously (`Runnable::run`).

## Step-by-Step Procedure
1. **Executor Architecture**:
   - Define dedicated worker and UI executors or Coroutine dispatchers.
2. **Async Task Encapsulation**:
   - Wrap network and database operations in async pipelines with success/error callbacks.
3. **Retry Policy**:
   - Define retry conditions (e.g. `IOException`, HTTP 503) and termination boundaries.
4. **Verification**:
   - Test async completion, timeout handling, and cancellation behavior using unit tests.
