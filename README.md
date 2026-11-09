# Fomo — Android Weather Application

A modern Android weather application delivering real-time weather forecasts, hourly projections, and atmospheric metrics.

---

## 1. Prerequisites

- **Java Development Kit (JDK)**: OpenJDK 17 LTS (Microsoft OpenJDK 17 or Eclipse Temurin recommended).
- **Android SDK**: API Level 34 Platform with Build Tools 34.0.0.
- **Gradle**: 8.9 (included via Gradle Wrapper).

---

## 2. Local Setup & Environment

Ensure `JAVA_HOME` and `ANDROID_HOME` are configured in your development environment:

```powershell
# Windows PowerShell
$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-17.0.21.21-hotspot"
$env:ANDROID_HOME = "C:\Users\<username>\AppData\Local\Android\Sdk"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

To configure the Android SDK directory for Gradle, create `local.properties` in the project root:

```properties
sdk.dir=C:\\Users\\<username>\\AppData\\Local\\Android\\Sdk
```

---

## 3. Development Commands

### Build Debug APK
```bash
./gradlew assembleDebug
```
The compiled APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`

### Run Unit Tests
```bash
./gradlew testDebugUnitTest
```

### Run Android Lint
```bash
./gradlew lintDebug
```

---

## 4. Architecture Overview

Fomo implements Google's official Android Architecture Guide and Clean Architecture principles:
- **Presentation Layer**:
  - `com.example.fomo.ui.main`: UI controllers (`MainActivity`) using Jetpack ViewBinding.
  - `com.example.fomo.ui.viewmodel`: Reactive `WeatherViewModel` exposing immutable `WeatherUiState` via `LiveData`.
  - `com.example.fomo.ui.adapter`: `HourlyAdapter` extending `ListAdapter` with asynchronous `DiffUtil` calculation and `WeatherIconMapper`.
- **Domain Layer**:
  - `com.example.fomo.domain.model`: Pure, immutable domain entities (`Hourly`, `CurrentWeather`, `WeatherForecast`).
  - `com.example.fomo.domain.repository`: Abstract `WeatherRepository` contracts.
- **Data Layer**:
  - `com.example.fomo.data.repository`: `InMemoryWeatherRepository` with clean background/main executor dispatching (preparing for Phase 3's Open-Meteo REST API and Room offline DB).
- **Dependencies**: 100% centralized in `gradle/libs.versions.toml`.

For full transformation documentation and architecture decision records, see [`docs/upgrade/`](docs/upgrade/).
