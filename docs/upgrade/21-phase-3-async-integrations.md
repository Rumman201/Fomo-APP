# 21 — Phase 3 Async Processing & Integration Reliability

## 1. Concurrency Architecture & Thread Confinement

- **Main Thread Invariant**: Android Main (UI) thread is strictly protected from disk and network operations.
- **Worker Executor**: Single-threaded background worker pool (`Executors.newSingleThreadExecutor()`) serializes all database queries and HTTP executions in production.
- **Main Executor**: Injected `HandlerExecutor` dispatches result callbacks back to `Looper.getMainLooper()` so UI state mutations happen safely on the main thread.
- **Test Inversion**: Both worker and main executors are injectable in [`OfflineFirstWeatherRepository`](file:///h:/Projects/Fomo-App/app/src/main/java/com/example/fomo/data/repository/OfflineFirstWeatherRepository.java), allowing unit tests to pass synchronous `Runnable::run` executors for instantaneous, deterministic execution.

---

## 2. Network Client Resilience

Configured in [`ApiClient`](file:///h:/Projects/Fomo-App/app/src/main/java/com/example/fomo/data/remote/api/ApiClient.java):
1. **Timeouts**:
   - `connectTimeout`: 15 seconds
   - `readTimeout`: 15 seconds
   - `writeTimeout`: 15 seconds
2. **Connection Pooling & Retry**:
   - `retryOnConnectionFailure(true)`: Automatic retry on transient TCP dropouts.
3. **Logging Sanitation**:
   - `HttpLoggingInterceptor.Level.BASIC` logs request URLs and response codes without exposing user coordinates or private device identifiers.
