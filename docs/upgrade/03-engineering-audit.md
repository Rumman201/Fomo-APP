# 03 — Comprehensive Specialist Engineering Audit

## Audit Overview
A multi-disciplinary specialist engineering audit was executed across all application dimensions. The findings below reflect verified repository evidence and Gradle tool execution outputs.

---

## 1. Summary of Identified Defects by Severity

| Severity Level | Count | Definition | Primary Impact |
| :--- | :---: | :--- | :--- |
| **P0 — Critical** | 3 | Build failure, runtime crash, broken core view hierarchy | Android Lint build failure, runtime API crash on API 24–28, image render failure. |
| **P1 — High** | 6 | Architecture flaws, dead layout nodes, overdraw, missing test gates | Zero unit tests, broken package naming, duplicate view IDs, hardcoded reflection. |
| **P2 — Medium** | 8 | UI/UX defects, accessibility violations, dependency versioning gaps | Hardcoded strings, missing `contentDescription`, `sp` vs `dp` unit misuses, outdated libraries. |
| **P3 — Low** | 4 | Documentation, style, naming convention inconsistencies | Missing `README.md`, PascalCase package names, dead python `.gitignore`. |

---

## 2. Detailed Findings Register

### SEC-01: Prohibited Reflection API & Incompatible Context Call (P0)
- **Specialty**: Senior Security & Mobile Architect
- **File**: `app/src/main/java/com/example/fomo/Adapters/HourlyAdapters.java#L40-L42`
- **Evidence**:
  ```java
  int drawableResourceId = holder.itemView.getResources()
          .getIdentifier(items.get(position).getPicPath(), "drawable",holder.itemView.getContext().getOpPackageName());
  ```
- **Root Cause**:
  1. `getOpPackageName()` is an API level 29+ method. The app's `minSdk` is configured as `24`. This triggers an immediate Android Lint fatal error (`[NewApi]`) and risks throwing `NoSuchMethodError` on Android 7.0–9.0 devices.
  2. `Resources.getIdentifier()` uses runtime reflection to search resource tables by string name. It is heavily discouraged by Google (`[DiscouragedApi]`) due to slow performance, lack of compile-time verification, and inability to optimize resources with R8 shrinking.
- **Remediation**:
  Replace dynamic reflection with a deterministic, type-safe drawable resolver utility (`WeatherIconMapper`) mapping string keys or enum types directly to compiled `R.drawable.*` identifiers. Use standard `Context.getPackageName()`.

---

### UI-01: Duplicate View IDs in Layout Hierarchy (P0)
- **Specialty**: Principal Frontend & UI/UX Director
- **File**: `app/src/main/res/layout/activity_main.xml#L117-L208`
- **Evidence**:
  - `@+id/textView5` is declared on lines 124, 133, 158, 167, 191, and 200 (6 times!).
  - `@+id/imageView2` is declared on lines 117, 151, and 184 (3 times!).
- **Root Cause**: Copy-pasting container XML for the Rain, Wind, and Humidity metric boxes without renaming view identifiers.
- **Impact**: Fails Android Lint (`[DuplicateIds]`). Makes view lookups via `findViewById()` unpredictable and breaks accessibility screen reader focus traversal.
- **Remediation**: Reassign distinct semantic IDs: `rainIcon`, `rainPercentageText`, `rainLabel`, `windIcon`, `windSpeedText`, `windLabel`, `humidityIcon`, `humidityPercentageText`, `humidityLabel`.

---

### UI-02: Missing Constraints and Dead Unconstrained Layout Containers (P0)
- **Specialty**: Senior UI/UX & Mobile Engineer
- **File**: `app/src/main/res/layout/activity_main.xml#L9-L36`
- **Evidence**:
  - Line 9–16: Stray `TextView` with `android:text="Hello World!"` left behind.
  - Line 18–30: Empty `ConstraintLayout` with fixed dimensions `layout_width="409dp"` and `layout_height="729dp"` with unconstrained tools coordinates.
  - Line 32–36: Second completely empty `ConstraintLayout`.
- **Root Cause**: Residual layout editor artifacts left uncleaned during initial prototyping.
- **Impact**: Fails Android Lint (`[MissingConstraints]`). Wastes memory during layout inflation and creates dead view nodes.
- **Remediation**: Completely excise the stray `TextView` and the two empty container layouts.

---

### QA-01: Zero Automated Tests & Missing Test Dependencies (P1)
- **Specialty**: Principal QA & Test Automation Engineer
- **Files**:
  - `app/src/test/` (Empty)
  - `app/src/androidTest/` (Empty)
  - `app/build.gradle.kts#L33-L39`
- **Evidence**:
  `libs.versions.toml` declares `junit = "4.13.2"`, `junitVersion = "1.2.1"`, and `espressoCore = "3.6.1"`, but `app/build.gradle.kts` does not declare `testImplementation(libs.junit)` or `androidTestImplementation(...)`. Running `./gradlew testDebugUnitTest` reports `NO-SOURCE`.
- **Impact**: Zero regression protection. Every refactoring carries a risk of undetected regressions.
- **Remediation**: Declare test dependencies in `app/build.gradle.kts` and implement unit tests covering domain models, data formatting, and icon resolution.

---

### ARCH-01: Package Misnaming & Violation of Java Conventions (P1)
- **Specialty**: Principal Software Architect
- **Files**:
  - `app/src/main/java/com/example/fomo/Activitis/`
  - `app/src/main/java/com/example/fomo/Adapters/`
  - `app/src/main/java/com/example/fomo/Domains/`
- **Evidence**: The directory and package names use PascalCase (`Activitis`, `Adapters`, `Domains`), and `Activitis` is misspelled.
- **Impact**: Violates Java and Android naming conventions (all package segments must be lowercase); damages maintainability and onboarding clarity.
- **Remediation**: Plan a safe migration in Phase 2 to `com.example.fomo.ui.main`, `com.example.fomo.ui.adapter`, `com.example.fomo.domain.model`.

---

### DEV-01: Hardcoded Dependencies & Missing Version Catalog Usage (P1)
- **Specialty**: Senior DevOps / SRE Engineer
- **File**: `app/build.gradle.kts#L38`
- **Evidence**:
  ```kotlin
  implementation("com.github.bumptech.glide:glide:4.12.0")
  ```
- **Impact**: Triggers Android Lint warning `[UseTomlInstead]`. Fragmented dependency management creates supply chain drift.
- **Remediation**: Add `glide = "4.16.0"` to `gradle/libs.versions.toml` and reference `implementation(libs.glide)` in `app/build.gradle.kts`.

---

### PERF-01: GPU Overdraw & Nested Layout Baseline Inefficiencies (P1)
- **Specialty**: Senior Performance Engineer
- **Files**:
  - `app/src/main/res/layout/activity_main.xml#L7`, `L41`
  - `app/src/main/res/layout/activity_main.xml#L103`, `L218`
- **Evidence**:
  - Root `ConstraintLayout` sets `android:background="@drawable/background2"`, while child `ConstraintLayout` sets `android:background="@drawable/background"`. Both occupy the entire screen (`match_parent`), forcing the GPU to redraw every pixel twice.
  - LinearLayout with weights triggers `[DisableBaselineAlignment]` and `[InefficientWeight]` (using `wrap_content` instead of `0dp`).
- **Remediation**: Remove redundant background on child container, set `android:baselineAligned="false"`, and set `layout_width="0dp"` with weights.

---

### UI-03: Dimension & Unit Inconsistencies (P2)
- **Specialty**: Senior UI/UX Design Director
- **Files**:
  - `app/src/main/res/layout/activity_main.xml#L91` (`android:textSize="63dp"`)
  - `app/src/main/res/drawable/background2.xml#L6` (`<corners android:radius="20sp"/>`)
- **Evidence**: Text size uses `dp` instead of `sp` (`[SpUsage]`); drawable corner radius uses `sp` instead of `dp`.
- **Impact**: Text does not adapt to accessibility font size changes; drawable corners distort when users increase system font size.
- **Remediation**: Standardize text size to `63sp` and corner radius to `20dp`.

---

### A11Y-01: Missing Content Descriptions on Images (P2)
- **Specialty**: Accessibility Specialist
- **Files**:
  - `activity_main.xml` (4 ImageViews)
  - `viewholder_hourly.xml` (1 ImageView)
- **Evidence**: Android Lint flags 5 `[ContentDescription]` warnings. Screen readers cannot describe the weather state or metric icons to visually impaired users.
- **Remediation**: Add localized content descriptions for weather state icons and mark decorative icons appropriately.

---

### I18N-01: 13 Hardcoded Layout Strings with Spelling & Grammatical Errors (P2)
- **Specialty**: Senior Technical Documentation & UI Director
- **File**: `app/src/main/res/layout/activity_main.xml`
- **Evidence**:
  - Line 61: `android:text="Mostly Cloudly"` (Typo in "Cloudly")
  - Line 235: `android:text="Next 7 Day >"` (Grammar error: should be "Next 7 Days >")
  - 11 additional strings hardcoded directly in XML (`25°`, `H:27 L:18`, `22%`, `Rain`, etc.).
- **Impact**: Cannot localize the application to other languages; triggers Android Lint `[HardcodedText]` warnings.
- **Remediation**: Extract all strings into `app/src/main/res/values/strings.xml` with corrected spelling and grammar.
