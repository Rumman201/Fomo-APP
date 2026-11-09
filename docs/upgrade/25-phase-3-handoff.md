# 25 — Phase 3 Completion & Phase 4 Handoff Specification

## 1. Confirmed Engineering State

Phase 3 has delivered a complete, offline-first backend and persistence engine:
- **Remote Integration**: Open-Meteo REST API queried via Retrofit 2 + OkHttp 4.
- **Local Persistence**: Jetpack Room ORM SQLite database (`fomo_weather.db`) with atomic transactions.
- **Data Invariants**: 30-minute cache TTL, graceful offline degradation, pure WMO code translation.
- **Security**: Strict HTTPS enforcement via Android Network Security Config, zero stored credentials.
- **Test Integrity**: 34 unit tests (100% pass rate).
- **Static Quality**: Android Lint 0 fatal errors.
- **Binary Health**: Debug APK assembled successfully (7.82 MB).

---

## 2. Phase 4 Objectives (Frontend Engineering & UI/UX Transformation)

Phase 4 will elevate the user experience, layout hierarchy, and visual design:
1. **Layout Modernization & Polish**:
   - Refactor `activity_main.xml` and `viewholder_hourly.xml` into modern Material Design 3 guidelines.
   - Introduce pull-to-refresh (`SwipeRefreshLayout`) linked to `WeatherViewModel.loadWeather()`.
   - Implement shimmer/skeleton loading states replacing generic blank screens.
2. **Typography & Spacing**:
   - Standardize font hierarchy, contrast ratios, and accessible touch targets (minimum 48dp x 48dp).
3. **Multi-Location Preparation**:
   - Prepare UI and ViewModel for city search and location switching.
4. **Accessibility (TalkBack)**:
   - Ensure all interactive elements declare descriptive `contentDescription` attributes and verified screen reader focus orders.

---

## 3. Invariants for Phase 4 Engineers & Agents

- **Contract Stability**: Do not modify `WeatherRepository`, `WeatherForecast`, `CurrentWeather`, or `WeatherUiState` contracts without backward-compatible adapters.
- **Quality Gates**: Maintain 0 Android Lint errors and 100% test pass rate on `./gradlew testDebugUnitTest`.
- **Unit Standards**: Never introduce hardcoded pixel (`px`) values; strictly use `dp` for dimensions and `sp` for typography.
