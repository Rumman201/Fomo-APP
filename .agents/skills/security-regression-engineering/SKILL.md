---
name: security-regression-engineering
description: Negative tests, authorization matrices, secure defaults, CI validation, vulnerability reproduction-to-regression conversion, and release gates.
---

# Security Regression Engineering Skill

## Purpose & Scope
This skill governs the systematic conversion of security findings and threat scenarios into permanent, automated regression tests. It ensures that once a vulnerability is remediated, it can never silently recur in subsequent feature phases.

## When to Activate
- Authoring negative security test cases in JUnit.
- Creating automated tests for certificate pinning, SQL injection immunity, prompt injection rejection, and input sanitization.
- Wiring security verification gates into Gradle and CI build pipelines.
- Verifying that security hardening fixes remain intact across refactoring cycles.

## Engineering Invariants
1. **Permanent Regression Coverage**: Every resolved security vulnerability must be paired with an automated test reproducing the threat vector.
2. **Zero Test Flakiness**: Security tests must execute deterministically without network flakiness, using mock servers or in-memory fixtures.
3. **Release Gate Enforcement**: A release artifact cannot be produced if any security regression test fails or Android Lint produces fatal errors.
4. **Transparent Verification**: Document exact test execution logs and commands in project documentation.

## Step-by-Step Procedure
1. **Threat Translation**: Convert each identified security flaw into an executable test case.
2. **Test Implementation**: Author JUnit 4 test classes under `app/src/test/java/com/example/fomo/`.
3. **Verification Run**: Run `./gradlew testDebugUnitTest` and confirm 100% pass rate.
4. **Static Lint Gate**: Run `./gradlew lintDebug` and confirm zero fatal errors.
5. **Continuous Protection**: Document test results in `docs/upgrade/` and commit test suites to version control.

