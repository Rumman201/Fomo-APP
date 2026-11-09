---
name: system-architecture-review
description: Architecture evaluation, modular boundary definition, technical design decisions, and modernization planning for Android and client-server systems.
---

# System Architecture Review Skill

## Purpose
Evaluates system organization, component cohesion, coupling, data flow, and separation of concerns. Designs evolutionary modernization strategies (e.g., migrating monolithic Activities to Clean Architecture with MVVM/MVI).

## Activation Criteria
- Triggered when reviewing high-level application structure, component layering, or planning refactoring.
- Used when evaluating package hierarchies, ViewModel implementations, or state management architectures.

## Architecture Guidelines (Android Modern Standards)
1. **Clean Architecture Separation**:
   - **Presentation Layer**: UI Views, Compose/XML, ViewModels, ViewStates.
   - **Domain Layer**: Models, UseCases/Interactors, Repository interfaces.
   - **Data Layer**: Repository implementations, Local Data Source (Room/DataStore), Remote Data Source (Retrofit/Ktor).
2. **Unidirectional Data Flow (UDF)**:
   - UI emits user intents/events to ViewModel.
   - ViewModel updates StateFlow/LiveData.
   - UI observes state and renders deterministically.
3. **Dependency Inversion**: High-level modules must not depend on low-level modules; both depend on abstractions.

## Verification
- Architectural decisions documented as Architecture Decision Records (ADRs).
- Zero circular package dependencies.
