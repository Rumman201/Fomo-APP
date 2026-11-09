---
name: domain-logic-verification
description: Business rule validation, meteorological code mapping, state transition testing, cache TTL calculations, and domain invariant verification.
---

# Domain Logic Verification Skill

## Purpose & Scope
This skill provides standards for verifying core domain logic, business rules, meteorological calculations, and domain invariants. It ensures business policies remain isolated from framework and platform concerns.

## When to Activate
- Implementing or modifying business logic, state calculations, or domain entities.
- Translating WMO (World Meteorological Organization) weather interpretation codes into human-readable descriptions and icon resources.
- Verifying cache staleness policies (e.g., 30-minute cache TTL, stale-while-revalidate).
- Testing temperature and unit conversions (Celsius/Fahrenheit, km/h to mph).
- Validating state transitions and immutable domain constraints.

## Engineering Standards & Invariants
1. **Zero Android Framework Dependencies**:
   - Pure domain models and logic classes must avoid referencing Android SDK classes (`android.os.*`, `android.view.*`, `android.content.Context`).
2. **Defensive Validation**:
   - Validate numerical bounds, string non-emptiness, and collection invariants in constructors or factory methods.
3. **Deterministic Business Rules**:
   - Business calculations (e.g. weather condition classification, cache expiration check) must be deterministic and pure functions.
4. **Comprehensive Test Coverage**:
   - 100% of domain business rules must be verified with isolated JUnit tests.

## Step-by-Step Procedure
1. **Rule Discovery & Specification**:
   - Document domain rules (e.g., WMO code 0 = "Clear sky", WMO code 61 = "Rain: Slight").
   - Define cache expiry rules (e.g. timestamp + 1800s < now => stale).
2. **Domain Implementation**:
   - Implement pure domain functions or immutable value objects.
3. **Edge Case Analysis**:
   - Test out-of-range inputs, negative temperatures, unexpected weather codes, leap years, and daylight savings.
4. **Verification**:
   - Write parameterized unit tests asserting correct outputs for all boundary conditions.
