---
name: qa-verification
description: Automated test architecture, unit testing with JUnit 4/5, mock frameworks, Robolectric, Espresso UI testing, and regression verification.
---

# QA & Test Automation Engineering Skill

## Purpose
Establishes a robust testing pyramid for Android, ensuring high test coverage, deterministic execution, and zero regression tolerance across refactors and upgrades.

## Activation Criteria
- Triggered whenever code is modified or new business logic, domain models, ViewModels, or repositories are introduced.
- Used when configuring test dependencies, writing unit tests, running Android Lint, or validating build artifacts.

## Testing Standards
1. **Unit Testing (app/src/test)**:
   - Target pure logic: domain models, use cases, mappers, formatters, and ViewModels.
   - Fast, deterministic, independent of Android framework whenever possible; use Robolectric when Android context is required.
   - Run via `./gradlew testDebugUnitTest`.
2. **Instrumentation Testing (app/src/androidTest)**:
   - Verify UI component rendering, user interactions, and end-to-end integration using Espresso.
   - Run via `./gradlew connectedAndroidTest` (when emulator or device is available).
3. **Static Analysis & Lint Gates**:
   - Run `./gradlew lintDebug` on every significant modification.
   - Zero errors policy: All Android Lint errors must be resolved before phase completion.
4. **Verification Protocol**:
   - Always run the actual test command and capture exit code, test counts, and execution duration. Never claim a test passed without running it.
