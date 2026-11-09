---
name: type-safety-and-contracts
description: Type safety, contract validation, null and undefined handling, boundary typing, schema consistency, public API typing, and runtime validation.
---

# Type Safety & Contracts Skill

## Purpose
Eliminates entire classes of preventable runtime defects (`NullPointerException`, `ClassCastException`, unchecked assumptions) by enforcing strict type contracts, immutability, and boundary validation.

## Activation Criteria
- Triggered when designing or refactoring domain models, repository contracts, method signatures, or view state objects.
- Used when auditing nullability annotations (`@NonNull`, `@Nullable`) and eliminating raw types.

## Engineering Invariants
1. **Model Immutability**:
   - Domain models should be immutable (`final` fields, no public setters, equals/hashCode/toString implemented).
   - Defensive copies or unmodifiable collections for list fields.
2. **Nullability Rigor**:
   - Explicitly annotate public method arguments and return types with AndroidX `@NonNull` or `@Nullable`.
   - Never rely on implicit non-null assumptions for external or async data.
3. **Sealed / Discriminated UI States**:
   - Model UI states using typed state hierarchies (e.g. `WeatherUiState`: `Loading`, `Success`, `Error`) with unambiguous payload contracts.
4. **Compile-Time View Safety**:
   - Enforce Jetpack ViewBinding to eliminate runtime `NullPointerException` risks from invalid `findViewById` type casts or missing view IDs.
