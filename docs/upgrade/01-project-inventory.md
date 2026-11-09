# 01 — Project Inventory & Intelligence

## 1. Project Overview
- **Project Name**: Fomo
- **Application ID / Package**: `com.example.fomo`
- **Application Type**: Native Android Mobile Application
- **Primary Domain**: Weather Forecasting & Atmospheric Metrics
- **Current Development Maturity**: Early Prototype / Educational Template

## 2. Technology Stack Matrix

| Dimension | Specification | Verified Version / Details | Status |
| :--- | :--- | :--- | :--- |
| **Operating System** | Windows 11 (Host) / Android (Target) | Target SDK 34 (Android 14) / Min SDK 24 (Android 7.0) | Verified |
| **JDK Environment** | Microsoft OpenJDK 17 LTS | `17.0.21.0.2+1-LTS` (Path: `C:\Program Files\Microsoft\jdk-17.0.21.21-hotspot`) | Verified |
| **Android SDK** | Android 34 Platform | Build Tools `34.0.0` (Path: `C:\Users\tamim\AppData\Local\Android\Sdk`) | Verified |
| **Build System** | Gradle with Kotlin DSL | Gradle `8.9` (`gradle/wrapper/gradle-wrapper.properties`) | Verified |
| **Android Gradle Plugin** | AGP | `8.7.1` (`gradle/libs.versions.toml`) | Verified |
| **Programming Language** | Java | Java 11 bytecode compatibility | Verified |
| **Core Libraries** | AndroidX & Google Material | `appcompat:1.7.0`, `material:1.12.0`, `activity:1.9.3`, `constraintlayout:2.2.0` | Verified |
| **Image Loading** | Glide | `4.12.0` (Hardcoded in `app/build.gradle.kts`) | Verified |
| **Local Persistence** | None | Prototype uses in-memory mock `ArrayList` | Defect |
| **Networking** | None | No HTTP client configured | Defect |
| **Testing Framework** | None active | `junit:4.13.2`, `espresso:3.6.1` in TOML but not linked in Gradle | Defect |
| **Version Control** | Git | Initialized in Phase 1 with baseline commit `6b7b950` | Operational |

---

## 3. Repository & Package Structure

```text
H:\Projects\Fomo-App\
├── .agents/
│   └── skills/                       # Permanent Antigravity specialist skills
│       ├── project-intelligence-audit/
│       ├── system-architecture-review/
│       ├── mobile-engineering/
│       ├── android-ui-ux-engineering/
│       ├── security-hardening/
│       ├── database-engineering/
│       ├── performance-optimization/
│       ├── qa-verification/
│       ├── devops-reliability/
│       └── technical-documentation/
├── app/
│   ├── src/
│   │   ├── androidTest/java/com/example/ # Empty test directory
│   │   ├── test/java/com/example/        # Empty test directory
│   │   └── main/
│   │       ├── AndroidManifest.xml       # Main Android manifest
│   │       ├── java/com/example/fomo/
│   │       │   ├── Activitis/            # Typo in directory name
│   │       │   │   └── MainActivity.java # Monolithic prototype activity
│   │       │   ├── Adapters/
│   │       │   │   └── HourlyAdapters.java # RecyclerView adapter with reflection
│   │       │   └── Domains/
│   │       │       └── Hourly.java       # POJO domain model
│   │       └── res/
│   │           ├── drawable/             # XML gradients & backgrounds
│   │           ├── drawable-hdpi...xxxhdpi # PNG weather icons
│   │           ├── layout/
│   │           │   ├── activity_main.xml # Main dashboard layout (flawed nesting & duplicate IDs)
│   │           │   └── viewholder_hourly.xml # Forecast item layout
│   │           ├── values/
│   │           │   ├── colors.xml        # Color definitions
│   │           │   ├── strings.xml       # String definitions (only app_name)
│   │           │   └── themes.xml        # Material3 DayNight theme
│   │           └── xml/                  # Backup & data extraction rules
│   ├── build.gradle.kts                  # App module build script
│   └── proguard-rules.pro                # Proguard rules
├── gradle/
│   ├── wrapper/                          # Gradle wrapper jar & properties
│   └── libs.versions.toml                # Dependency version catalog
├── docs/
│   └── upgrade/                          # Upgrade Control Center documentation
├── AGENTS.md                             # Persistent engineering rules
├── build.gradle.kts                      # Root build script
├── gradle.properties                     # JVM and AndroidX settings
├── gradlew & gradlew.bat                 # Gradle wrapper scripts
└── settings.gradle.kts                   # Project repositories and module inclusion
```

---

## 4. Feature Inventory & Current Status

| Feature ID | Feature Name | Intended Workflow | Implementation Location | Current Status | Defect & Risk Notes |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **FEAT-01** | Current Weather Overview | Displays current condition, main icon, date/time, temperature, and High/Low. | `MainActivity.java`, `activity_main.xml` | Prototype (Static) | 100% hardcoded strings ("Mostly Cloudly", "25°", "Mon Dec 16 | 2.03 AM"). Typo in text. |
| **FEAT-02** | Atmospheric Metrics | Shows Rain probability (%), Wind speed (km/h), and Humidity (%). | `activity_main.xml` | Prototype (Static) | Hardcoded static values. Duplicate IDs in XML (`textView5`, `imageView2`) broke Android Lint. |
| **FEAT-03** | Hourly Forecast Carousel | Horizontal scrolling list of hourly temperatures with weather condition icons. | `HourlyAdapters.java`, `Hourly.java`, `activity_main.xml:view1` | Partially Implemented | Uses slow reflection (`getIdentifier`), crashes on `"sun"` (file is `sunny.png`), uses API 29+ `getOpPackageName()`. |
| **FEAT-04** | 7-Day Forecast Navigation | Button/Link to navigate to extended 7-day forecast. | `activity_main.xml:textView7` | Incomplete (Stub) | Unclickable TextView ("Next 7 Day >"). No target Activity, Fragment, or click listener exists. |
| **FEAT-05** | Live Weather API Integration | Real-time weather data fetching based on GPS or selected location. | None | Missing | App is completely offline and static. |
| **FEAT-06** | City Search & Selection | Allows searching for locations and saving favorites. | None | Missing | No location search or multi-city management exists. |
| **FEAT-07** | Offline Caching & Persistence | Persists weather data and user preferences locally. | None | Missing | In-memory only; data resets on app termination. |
| **FEAT-08** | Unit & UI Test Suite | Automated regression testing for models, adapters, and UI components. | None | Missing | Zero tests in `app/src/test` and `app/src/androidTest`. |
