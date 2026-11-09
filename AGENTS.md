# AGENTS.md — Persistent Engineering Invariants & Conventions

## Project Context
**Fomo** is an Android mobile application providing real-time and forecasted weather intelligence. It uses Gradle with Kotlin DSL, Android Gradle Plugin (AGP), and AndroidX/Material Design 3 libraries.

---

## Non-Negotiable Engineering Invariants

1. **Preserve Working Functionality**:
   - Never remove or break existing user features without explicit justification and evidence.
2. **Understand Before Modifying**:
   - Always inspect surrounding code, dependencies, and lifecycles before applying changes.
3. **Evidence-Based Engineering**:
   - Support every finding, decision, and claim with concrete code references, tool executions, and measurements. Never fabricate test passes or build results.
4. **Android Platform Discipline**:
   - Strictly adhere to `minSdk` (API 24) compatibility. Never call newer APIs without appropriate runtime SDK checks.
   - Never perform disk or network operations on the Android Main (UI) thread.
   - Prevent memory leaks: never hold long-lived references to `Activity` or `View` contexts.
5. **UI & Accessibility Invariants**:
   - Zero duplicate view IDs within any layout hierarchy.
   - Text sizes must use `sp` units; view dimensions and corner radii must use `dp` units.
   - All user-facing strings must be extracted into `res/values/strings.xml`.
   - All interactive or informative image elements must provide meaningful `contentDescription` attributes; purely decorative elements must declare `@null` or `importantForAccessibility="no"`.
6. **Dependency Management**:
   - All library coordinates and version numbers must be declared in `gradle/libs.versions.toml`. Never hardcode library versions inside module `build.gradle.kts`.
7. **Quality Gates & Regression Protection**:
   - Every modification must be verified via actual Gradle execution (`./gradlew testDebugUnitTest`, `./gradlew lintDebug`, and `./gradlew assembleDebug`).
   - Android Lint must have zero errors.
8. **Security & Secrets**:
   - Never commit sensitive API keys, credentials, or signing keys to source control.
   - Enforce HTTPS and secure transport configuration across all external endpoints.
9. **Documentation Alignment**:
   - Keep architectural documentation, ADRs, and the 10-phase transformation records in `docs/upgrade/` updated with implementation changes.
