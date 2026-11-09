# 16 — Phase 2 Completion & Phase 3 Handoff Specification

## 1. Confirmed Engineering State
Phase 2 has successfully established a production-grade Clean Architecture and MVVM foundation:
- **Clean Architecture Hierarchy**:
  - Presentation: `com.example.fomo.ui.main`, `.ui.viewmodel`, `.ui.adapter`
  - Domain: `com.example.fomo.domain.model`, `.domain.repository`
  - Data: `com.example.fomo.data.repository`
- **View Safety**: 100% of views bound via Jetpack ViewBinding (`ActivityMainBinding`, `ViewholderHourlyBinding`).
- **Reactive State Flow**: UI observes `WeatherViewModel`'s `LiveData<WeatherUiState>`.
- **List Optimization**: `HourlyAdapter` uses `ListAdapter` and `DiffUtil`.
- **Unit Test Coverage**: 18 passing unit tests (100% pass rate).
- **Zero Android Lint Errors**: Sustained clean lint report.

---

## 2. Phase 3 Objectives (Backend, APIs, and Data Engineering)
Phase 3 will upgrade the data layer from in-memory mock data to live meteorological data with offline caching:
1. **Open-Meteo REST API Integration**:
   - Add Retrofit 2 and OkHttp 3 to `gradle/libs.versions.toml`.
   - Add Kotlinx Serialization or Moshi JSON converter.
   - Define `WeatherApiService` mapping to Open-Meteo's free forecast endpoint (`/v1/forecast?latitude=...&longitude=...&current=...&hourly=...`).
2. **Room ORM Offline Persistence**:
   - Add AndroidX Room runtime and compiler (KSP or annotation processor) to `libs.versions.toml`.
   - Create `WeatherDatabase`, `WeatherEntity`, and `WeatherDao`.
   - Implement single-source-of-truth caching: emit cached data immediately, then update cache with network response.
3. **`WeatherRepositoryImpl`**:
   - Replace `InMemoryWeatherRepository` with `WeatherRepositoryImpl` orchestrating `WeatherApiService` and `WeatherDao`.
4. **Network Security Configuration**:
   - Configure HTTPS enforcement in `res/xml/network_security_config.xml`.

---

## 3. Instructions for Subsequent Engineers / Agents
1. **Skills to Activate**: `database-engineering`, `security-hardening`, `devops-reliability`, and `qa-verification`.
2. **Dependency Sequencing**:
   - Add Retrofit, OkHttp, and Room in `libs.versions.toml`.
   - Update `app/build.gradle.kts`.
3. **Maintain Invariants**:
   - Do not alter `MainActivity` or layout files unnecessarily; `WeatherViewModel` and domain models provide a stable contract.
   - Run `./gradlew testDebugUnitTest` and `./gradlew lintDebug` after every integration.
