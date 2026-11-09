# Phase 6 — Application & API Security Engineering

## 1. Network API Security Architecture

### 1.1 Transport Layer Security (TLS 1.2 / TLS 1.3)
- Android API 24+ natively negotiates TLS 1.2 or TLS 1.3 with modern cipher suites (e.g., `TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256`, `TLS_AES_256_GCM_SHA384`).
- Insecure SSL/TLS protocols (SSLv3, TLS 1.0, TLS 1.1) are blocked by Android platform defaults and OkHttp 4.12.0.

### 1.2 OkHttp Interceptor Security Pipeline
- `HttpLoggingInterceptor` set to `Level.BASIC` in production to prevent leaking sensitive response payloads into logcat logs.
- Timeouts strictly bounded to 15 seconds (`connectTimeout`, `readTimeout`, `writeTimeout`) to prevent slowloris Denial of Service against the client event loop.
- `retryOnConnectionFailure(true)` enables graceful reconnection over flaky mobile radio transitions.

---

## 2. API Contract Validation & Schema Hardening

### 2.1 Open-Meteo REST Mapping Boundaries
- All network DTOs (`WeatherResponse`, `DailyForecastDto`, `GeocodingResponse`, `GeocodingResultDto`) use strongly typed Gson mappings with defensive null checks.
- When Open-Meteo returns HTTP error codes ($4xx, 5xx$) or truncated arrays, `WeatherDtoMapper` returns classified exceptions rather than throwing `NullPointerException` or causing UI crashes.

### 2.2 Input Sanitization Integration
- City searches in `CitySearchBottomSheetDialog` are sanitized through `InputSanitizer.sanitizeSearchQuery(query)` prior to network dispatch.
- Empty or whitespace-only queries are suppressed locally without network calls, eliminating unnecessary API quota consumption.
- Coordinates passed into `saveCityAsync` are validated to conform to Earth boundaries $[-90.0, +90.0]$ and $[-180.0, +180.0]$.

