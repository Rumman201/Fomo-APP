# 04 — Baseline Execution & Quality Metrics

## Baseline Verification Protocol
All metrics were gathered through live execution in the project workspace on November 9, 2026.

---

## 1. Execution Log & Quality Gate Baselines

| Check / Task | Exact Command Executed | Exit Code | Outcome | Measured Baseline Data |
| :--- | :--- | :---: | :---: | :--- |
| **Gradle Environment** | `.\gradlew.bat -v` | `0` | **Pass** | Gradle `8.9`, Kotlin `1.9.23`, Groovy `3.0.21`, JVM `17.0.21.0.2 (Microsoft)` |
| **Debug Compilation & Assembly** | `.\gradlew.bat assembleDebug` | `0` | **Pass** | Build time: 3m 0s. 31 tasks executed. APK size: `6,823,156` bytes (`~6.82 MB`). |
| **Unit Test Execution** | `.\gradlew.bat testDebugUnitTest` | `0` | **Empty** | `compileDebugUnitTestJavaWithJavac NO-SOURCE`, `testDebugUnitTest NO-SOURCE`. 0 tests executed. |
| **Android Lint Analysis** | `.\gradlew.bat lintDebug` | `1` | **Fail** | **10 Errors, 54 Warnings**. Build aborted due to fatal Lint errors. |
| **Connected Device / ADB** | `adb.exe devices` | `0` | **Not Connected** | Daemon started at tcp:5037. Zero connected emulators or physical devices. |

---

## 2. Baseline Defect Breakdown (Android Lint)

### Fatal Errors (10 Errors):
1. `activity_main.xml:133`: Duplicate id `@+id/textView5` [DuplicateIds]
2. `activity_main.xml:151`: Duplicate id `@+id/imageView2` [DuplicateIds]
3. `activity_main.xml:158`: Duplicate id `@+id/textView5` [DuplicateIds]
4. `activity_main.xml:167`: Duplicate id `@+id/textView5` [DuplicateIds]
5. `activity_main.xml:184`: Duplicate id `@+id/imageView2` [DuplicateIds]
6. `activity_main.xml:191`: Duplicate id `@+id/textView5` [DuplicateIds]
7. `activity_main.xml:200`: Duplicate id `@+id/textView5` [DuplicateIds]
8. `activity_main.xml:22`: MissingConstraints on unconstrained layout container [MissingConstraints]
9. `HourlyAdapters.java:41`: Call requires API level 29 (current min is 24): `Context#getOpPackageName` [NewApi]
10. `AndroidManifest.xml`: NotificationPermission check triggered by transitive Glide notification target [NotificationPermission]

### Prominent Warnings (54 Warnings):
- `UseTomlInstead`: Glide dependency hardcoded in `app/build.gradle.kts`.
- `DiscouragedApi`: `Resources#getIdentifier` in `HourlyAdapters.java`.
- `SpUsage`: `android:textSize="63dp"` instead of `sp`.
- `DisableBaselineAlignment`: Nested LinearLayout without baseline alignment disabled.
- `InefficientWeight`: Weighted TextView with `wrap_content` instead of `0dp`.
- `ContentDescription`: 5 ImageViews missing accessibility descriptions.
- `HardcodedText`: 13 hardcoded strings in layout XML.
- `SetTextI18n`: String concatenation in `setText` without formatted resource string.

---

## 3. Measurable Target Baselines for Phase 1 Completion
- Android Lint Fatal Errors: **0** (from 10).
- Unit Tests Executed & Passing: **>= 3** (from 0).
- Debug APK Build: **Clean & Deterministic** (`assembleDebug` passing).
- Dependency Catalog: **100% TOML-driven** (zero hardcoded library strings in `build.gradle.kts`).
