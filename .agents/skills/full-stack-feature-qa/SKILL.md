---
name: full-stack-feature-qa
description: End-to-end feature verification, automated unit tests, network contract tests, Room persistence tests, edge case validation, and regression suites.
---

# Full-Stack Feature QA Skill

## Purpose & Scope
This skill governs the complete quality assurance, testing strategy, and regression protection for newly implemented features across all application layers. It requires authoring deterministic, repeatable test suites that validate data contracts, local database persistence, edge calculation accuracy, and UI state flows.

## When to Activate
- Authoring unit tests for new domain models, calculators, and mappers.
- Writing DAO and Room database integration tests.
- Testing Retrofit network service integration and JSON deserialization.
- Verifying ViewModel state emission and UI edge cases.
- Running regression test suites before declaring phase completion.

## Engineering Invariants
1. **Zero Flakiness**: Tests must produce 100% deterministic results across all environments without thread sleeps or race conditions.
2. **Comprehensive Coverage**: Every new feature must be backed by unit tests covering nominal paths, edge cases, and failure scenarios.
3. **No Fabricated Evidence**: Test passes, counts, and measurements must reflect actual executed test runs.
4. **Android Lint Cleanliness**: Code under test and test classes must maintain 0 fatal Android Lint errors.

## Step-by-Step Procedure
1. **Test Plan Formulation**: Identify key scenarios (nominal input, null/empty input, network failure, out-of-bounds metrics).
2. **Unit Test Implementation**: Write JUnit 4 test classes in `app/src/test/java`.
3. **Repository & DAO Testing**: Test Room CRUD operations and in-memory database queries.
4. **Adversarial & Edge Testing**: Test corrupted responses, negative temperatures, and invalid coordinate inputs.
5. **Quality Gate Execution**: Execute `./gradlew testDebugUnitTest` and verify 100% pass rate.

