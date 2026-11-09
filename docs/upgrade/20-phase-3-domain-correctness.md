# 20 — Phase 3 Domain Invariants & Business Logic Verification

## 1. Domain Invariants Verified

1. **Meteorological Code Interpretation**:
   - Implemented via pure domain utility [`WmoWeatherCodeMapper`](file:///h:/Projects/Fomo-App/app/src/main/java/com/example/fomo/domain/model/WmoWeatherCodeMapper.java).
   - All standard WMO codes (0 to 99) map deterministically to condition strings and icon assets.
   - Verified via [`WmoWeatherCodeMapperTest`](file:///h:/Projects/Fomo-App/app/src/test/java/com/example/fomo/WmoWeatherCodeMapperTest.java) across clear, cloudy, rainy, snowy, and thunderstorm conditions.

2. **Cache Freshness & Expiration (TTL)**:
   - TTL invariant: 30 minutes (`1,800,000 ms`).
   - If `System.currentTimeMillis() - cachedTimestamp < TTL`, the local cache is considered fresh and returned immediately.
   - If `System.currentTimeMillis() - cachedTimestamp >= TTL`, fresh data is fetched from the network while retaining the cached forecast as fallback in case of network outages.

3. **Time Formatting Discipline**:
   - ISO timestamps (`2026-09-01T14:00`) are parsed deterministically into 12-hour strings (`"2 pm"`).
   - Edge cases (midnight `00:00` -> `"12 am"`, noon `12:00` -> `"12 pm"`) are verified with unit tests.

4. **Aggregate Domain Immutability**:
   - [`CurrentWeather`](file:///h:/Projects/Fomo-App/app/src/main/java/com/example/fomo/domain/model/CurrentWeather.java), [`Hourly`](file:///h:/Projects/Fomo-App/app/src/main/java/com/example/fomo/domain/model/Hourly.java), and [`WeatherForecast`](file:///h:/Projects/Fomo-App/app/src/main/java/com/example/fomo/domain/model/WeatherForecast.java) enforce defensive copies and unmodifiable collections (`Collections.unmodifiableList`), preventing concurrent mutation bugs.
