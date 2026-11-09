---
name: backend-resilience-and-observability
description: HTTP client resilience, OkHttp timeout enforcement, connection pooling, offline-first fallback, logging interceptors, and operational diagnostics.
---

# Backend Resilience & Observability Skill

## Purpose & Scope
This skill addresses runtime diagnosability, logging hygiene, connection management, network failure tolerance, and offline degradation strategies for mobile and distributed systems.

## When to Activate
- Configuring HTTP clients (OkHttp, Retrofit) with timeout and interceptor pipelines.
- Establishing logging standards that sanitize sensitive data and preserve diagnostic clarity.
- Implementing offline-first fallback (Single Source of Truth pattern).
- Monitoring network connectivity changes and triggering graceful UI state degradation.

## Engineering Standards & Invariants
1. **Zero Secret Leakage**:
   - HTTP headers, tokens, and sensitive query parameters must never be printed to raw device logs.
   - Use `HttpLoggingInterceptor.Level.BASIC` or custom sanitizers in release builds.
2. **Explicit Timeouts**:
   - Every network connection must have deterministic connect, read, and write timeouts (default 15s).
3. **Graceful Offline Degradation**:
   - If a network request fails due to no internet or timeout, the system must transparently fall back to valid cached database entries before propagating a fatal error to the user.
4. **Structured Error Reporting**:
   - Provide non-empty, actionable diagnostic error messages and status codes without dumping raw Java stack traces to user-facing screens.

## Step-by-Step Procedure
1. **Client Setup**:
   - Configure `OkHttpClient.Builder` with timeouts, cache policies, and logging interceptors.
2. **Offline Fallback Pipeline**:
   - Query local database -> Emit cached data -> Dispatch network request in background -> On network success, persist to DB -> On network failure, retain cached data with a warning flag.
3. **Diagnostics & Logging**:
   - Implement structured tags and sanitized log formats.
4. **Resilience Verification**:
   - Simulate network disconnection and verify cache retrieval without application crash.
