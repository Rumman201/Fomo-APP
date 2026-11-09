# 08 — Architecture Decision Records (ADRs)

## ADR-001: Adoption of Centralized TOML Version Catalog for All Dependencies
- **Status**: Approved & Executing in Phase 1
- **Context**: `app/build.gradle.kts` contained hardcoded string dependencies (`"com.github.bumptech.glide:glide:4.12.0"`), triggering Android Lint warnings (`[UseTomlInstead]`) and fragmenting dependency version management.
- **Decision**: All dependencies must be declared in `gradle/libs.versions.toml` and referenced via type-safe accessors (`libs.*`).
- **Consequences**:
  - Positive: Single source of truth for library versions; reproducible builds; IDE autocompletion; clean dependency upgrades.
  - Negative: Requires adding alias declarations to Gradle scripts.
- **Alternatives Considered**: Direct string coordinates in `build.gradle.kts` (rejected due to maintenance overhead and Lint violations).

---

## ADR-002: Elimination of Reflection (`getIdentifier`) in Favor of Direct Resource Resolver
- **Status**: Approved & Executing in Phase 1
- **Context**: `HourlyAdapters` resolved weather icons via runtime reflection: `getResources().getIdentifier(items.get(position).getPicPath(), "drawable", ...)`. This triggered Lint `[DiscouragedApi]`, `[NewApi]` (due to `getOpPackageName()`), broke on mismatched strings (e.g. `"sun"` vs `sunny.png`), and degraded scrolling performance.
- **Decision**: Implement a static resolver mapping weather condition keys/enums directly to compiled `R.drawable.*` resource integer IDs.
- **Consequences**:
  - Positive: Zero reflection overhead; compile-time resource validation; compatible with R8 dead-code shrinking; 100% backward compatible to API 24.
  - Negative: Requires maintaining a lookup map for weather conditions.
- **Alternatives Considered**:
  - Keep `getIdentifier` with `@SuppressLint` (rejected: unsafe, slow, fails on API < 29).

---

## ADR-003: Flattening and Repairing the Main Layout Hierarchy
- **Status**: Approved & Executing in Phase 1
- **Context**: `activity_main.xml` contained 3 nested `ConstraintLayout` roots, dead unconstrained containers (`409dp x 729dp`), a stray `TextView` (`"Hello World!"`), and 9 duplicate view IDs (`textView5`, `imageView2`).
- **Decision**: Remove dead containers and stray views, assign unique semantic IDs to all views, eliminate redundant full-screen background drawables, and optimize `LinearLayout` weights.
- **Consequences**:
  - Positive: Eliminates 8 fatal Android Lint errors; resolves GPU overdraw; enables unambiguous `findViewById` and ViewBinding.
  - Negative: Requires updating any code referencing old view IDs.
- **Alternatives Considered**: Complete rewrite in Jetpack Compose (deferred to Phase 4 to maintain stability and prevent breaking existing functionality prematurely).

---

## ADR-004: Target Architecture Standard: Clean Architecture with MVVM
- **Status**: Implemented in Phase 2
- **Context**: Monolithic `MainActivity` contained mock data generation, adapter configuration, and UI management in a single file.
- **Decision**: Adopt Google-recommended Clean Architecture with MVVM: Presentation (`com.example.fomo.ui.*` + ViewModel + LiveData), Domain (`com.example.fomo.domain.*` + Models + Repository contracts), Data (`com.example.fomo.data.*` + InMemory/Remote/Room Repository).
- **Consequences**:
  - Positive: Testable business logic; offline caching support; multi-city scalability.
  - Negative: Requires intermediate in-memory repository before live API integration in Phase 3.

---

## ADR-005: Adoption of Jetpack ViewBinding for Compile-Time View Safety
- **Status**: Implemented in Phase 2
- **Context**: Codebase relied on raw `findViewById(R.id.*)`, which offered no compile-time view presence validation or type safety and increased boilerplate.
- **Decision**: Enable `viewBinding = true` in `app/build.gradle.kts` and use generated bindings (`ActivityMainBinding`, `ViewholderHourlyBinding`) across all presentation classes.
- **Consequences**:
  - Positive: Zero runtime `NullPointerException` risk; zero cast exceptions; faster compilation compared to DataBinding; zero reflection.
  - Negative: Minor increase in compiled bytecode (~30KB).
- **Alternatives Considered**: ButterKnife (deprecated), DataBinding (overly complex for simple view lookups), Kotlin Synthetics (deprecated).

---

## ADR-006: Upgrade from Raw RecyclerView Adapter to ListAdapter with DiffUtil
- **Status**: Implemented in Phase 2
- **Context**: `HourlyAdapters` directly managed an `ArrayList<Hourly>` and required full dataset reloads without item animations.
- **Decision**: Extend `androidx.recyclerview.widget.ListAdapter<Hourly, HourlyAdapter.ViewHolder>` and pass a static `DiffUtil.ItemCallback<Hourly>`.
- **Consequences**:
  - Positive: Background thread diffing; automatic item insertion/removal animations; memory-efficient list updates.
  - Negative: Requires domain models to implement proper `equals()` and `hashCode()`.

---

## ADR-007: Package Taxonomy Restructuring to Domain-Driven Lowercase Standards
- **Status**: Implemented in Phase 2
- **Context**: Original project used non-standard PascalCase and misspelled package names (`Activitis`, `Adapters`, `Domains`), violating Java package naming conventions and confusing developer navigation.
- **Decision**: Restructure packages into lowercase, layer-aligned hierarchies: `com.example.fomo.ui.main`, `com.example.fomo.ui.adapter`, `com.example.fomo.ui.viewmodel`, `com.example.fomo.domain.model`, `com.example.fomo.domain.repository`, `com.example.fomo.data.repository`.
- **Consequences**:
  - Positive: Standard Java and Android compliant; clean separation of concerns; intuitive developer onboarding.
  - Negative: Requires updating `AndroidManifest.xml` and all package import statements.

---

## ADR-008: Selection of Open-Meteo REST API for Live Meteorological Intelligence
- **Status**: Implemented in Phase 3
- **Context**: Hardcoded mock weather data prevented realistic application usage. Commercial weather APIs (OpenWeatherMap, WeatherAPI) require API keys, increasing secret leakage risks and credential management overhead.
- **Decision**: Integrate Open-Meteo REST API (`https://api.open-meteo.com/v1/forecast`).
- **Consequences**:
  - Positive: Keyless public endpoint eliminates API secret leakage risk; provides high-resolution hourly and current forecasts with WMO meteorological standardization; zero rate limit blocks during testing.
  - Negative: Requires mapping WMO interpretation codes (0–99) to local condition names and icon assets.

---

## ADR-009: Adoption of Android Jetpack Room ORM for SQLite Offline Caching
- **Status**: Implemented in Phase 3
- **Context**: Relying exclusively on remote API calls leads to blank screens or failures in offline or flight mode.
- **Decision**: Introduce Android Jetpack Room ORM (`FomoDatabase`, `WeatherEntity`, `HourlyEntity`, `WeatherDao`) to persist the latest weather snapshot.
- **Consequences**:
  - Positive: Compile-time SQL verification; clean schema migrations; atomic transactions (`@Transaction`); instant startup rendering.
  - Negative: Minor increase in compiled APK size (~800 KB).

---

## ADR-010: Implementation of Stale-While-Revalidate Caching Policy with 30-Minute TTL
- **Status**: Implemented in Phase 3
- **Context**: Weather data changes slowly; querying remote APIs on every activity resume wastes user mobile bandwidth and device battery.
- **Decision**: Enforce a 30-minute Cache Time-To-Live (TTL) in `OfflineFirstWeatherRepository`. If cache is fresh (< 30 min), return immediately. If expired or network fails, fall back gracefully to cached data.
- **Consequences**:
  - Positive: Near-zero latency UI loading; resilient offline operation; reduced network requests.
  - Negative: Users viewing the app frequently see cached observations for up to 30 minutes unless manual refresh is invoked.

---

## ADR-011: Strict HTTPS Enforcement via Android Network Security Configuration
- **Status**: Implemented in Phase 3
- **Context**: Mobile applications must protect user network traffic and prevent cleartext man-in-the-middle interception.
- **Decision**: Configure `res/xml/network_security_config.xml` with `cleartextTrafficPermitted="false"` and reference it in `AndroidManifest.xml`.
- **Consequences**:
  - Positive: Guaranteed secure transport for all external network endpoints; zero cleartext vulnerabilities.
  - Negative: Prohibits unencrypted HTTP traffic during local testing without explicit test overrides.

---

## ADR-012: Adoption of Material Design 3 Design Token Architecture
- **Status**: Implemented in Phase 4
- **Context**: Hardcoded colors and arbitrary layout dimensions created visual fragmentation, made theming difficult, and violated AGENTS.md engineering rules.
- **Decision**: Formalize design tokens in `res/values/colors.xml` and `res/values/dimens.xml` adhering to an 8dp spatial grid and semantic naming (`fomo_surface_card`, `fomo_accent_gold`, `spacing_m`).
- **Consequences**:
  - Positive: Centralized design system; uniform spatial harmony; seamless dark mode support; compliance with WCAG AA contrast rules.
  - Negative: Requires layout refactoring to reference semantic tokens.

---

## ADR-013: Integration of AndroidX SwipeRefreshLayout for Pull-to-Refresh
- **Status**: Implemented in Phase 4
- **Context**: Users had no gesture mechanism to manually reload weather intelligence when viewing cached observations.
- **Decision**: Wrap the main weather scroll container in `androidx.swiperefreshlayout.widget.SwipeRefreshLayout` wired to `WeatherViewModel.loadWeather()`.
- **Consequences**:
  - Positive: Standard Android pull-to-refresh UX; branded spinner colors; clear feedback during network revalidation.
  - Negative: Added lightweight `swiperefreshlayout` AndroidX library (~40 KB).

---

## ADR-014: Modal Bottom Sheet Pattern for Extended Multi-Day Forecast
- **Status**: Implemented in Phase 4
- **Context**: The "Next 7 Days" UI element was previously a static, non-functional text label. Navigating to a separate full-screen Activity would disrupt the current context.
- **Decision**: Implement `WeeklyForecastBottomSheetDialog` extending `BottomSheetDialogFragment` with `DailyForecastAdapter` and DiffUtil.
- **Consequences**:
  - Positive: Non-destructive contextual sheet; intuitive swipe-to-dismiss gesture; rich 7-day weather outlook.
  - Negative: Requires maintaining `DailyForecast` model and adapter.

---

## ADR-015: Compositor-Only Motion Architecture with Reduced-Motion Compliance
- **Status**: Implemented in Phase 4
- **Context**: Abrupt view state transitions feel unpolished, while heavy layout animations cause dropped frames (jank).
- **Decision**: Implement a 300ms `ViewPropertyAnimator` entrance transition animating exclusively composite-friendly properties (`alpha`, `translationY`) with runtime verification of `Settings.Global.ANIMATOR_DURATION_SCALE`.
- **Consequences**:
  - Positive: Silky 60fps frame rate; zero main-thread layout recalculations; full respect for user accessibility motion preferences.
  - Negative: Motion limited to translation, alpha, and scale.

---

## ADR-016: Multi-City Management via Open-Meteo Geocoding REST API & Room v2 Migration
- **Status**: Implemented in Phase 5
- **Context**: The app was statically bound to New York coordinates (`40.7128, -74.0060`), preventing worldwide search or multi-location lookup.
- **Decision**: Integrate Open-Meteo Geocoding REST API (`GeocodingApiService`), add `saved_cities` table to Room with `MIGRATION_1_2`, and implement `CitySearchBottomSheetDialog` with 350ms search debounce.
- **Consequences**:
  - Positive: Worldwide city search; local favorites persistence; seamless multi-city switching from header.
  - Negative: Requires database migration management.

---

## ADR-017: Edge Intelligence & Thermal Comfort Engine (NOAA Steadman / Wind Chill)
- **Status**: Implemented in Phase 5
- **Context**: Raw metrics (temperatures, wind, humidity) impose high cognitive load without practical attire or activity guidance.
- **Decision**: Author `AtmosphericIntelligenceService` computing thermal comfort via NOAA Steadman Heat Index and Environment Canada Wind Chill equations, generating attire advice and 4-factor activity scores (0–100) 100% on-device.
- **Consequences**:
  - Positive: Zero cloud latency; 100% offline capability; practical user utility.
  - Negative: Requires mathematical unit test coverage.

---

## ADR-018: Structured Production AI Synthesis with Dual-Tier Guardrails & Deterministic Fallback
- **Status**: Implemented in Phase 5
- **Context**: LLM text generation is probabilistic and risks hallucinating temperatures, weather warnings, or being manipulated via prompt injection.
- **Decision**: Implement `WeatherAiAdvisor` enforcing strict schema `AiWeatherAdvisory`, with prompt injection rejection, temperature disparity guardrail ($\Delta T > 6^\circ\text{C}$), and a deterministic rule-based natural language fallback engine.
- **Consequences**:
  - Positive: Guaranteed truthfulness; zero hallucination leakage; robust offline fallback.
  - Negative: Requires schema validation step.

---

## ADR-019: Event-Driven Weather Automation Engine with Cooldown Deduplication
- **Status**: Implemented in Phase 5
- **Context**: Weather hazards (storms, black ice, high winds, rain) require proactive notification without spamming the user on repeated app opens.
- **Decision**: Implement `WeatherAutomationEngine` evaluating meteorological triggers, enforcing 60-minute cooldown suppression per trigger type, and logging audit history in Room `automation_logs`.
- **Consequences**:
  - Positive: Proactive alerting; zero notification fatigue; full audit traceability.
  - Negative: Requires managing last-triggered timestamps.

---

## ADR-020: Personal Weather Station (PWS) Telemetry Ingestion Contract & Boundary Validation
- **Status**: Implemented in Phase 5
- **Context**: The project contains zero physical microcontroller hardware, yet hyper-local weather station integration is an important capability.
- **Decision**: Define a standardized PWS telemetry contract (`IotStationTelemetryDto`, `IotWeatherStationService`) with strict physical bounds checks ($[-90^\circ\text{C}, +60^\circ\text{C}]$, $0-100\%$, $800-1100\text{ hPa}$) and a deterministic simulator, without falsely claiming physical hardware connectivity.
- **Consequences**:
  - Positive: Adheres to Non-Negotiable Rule 01 and Rule 14; ready for future IoT station feeds.
  - Negative: Confined to contract and simulation layer.
