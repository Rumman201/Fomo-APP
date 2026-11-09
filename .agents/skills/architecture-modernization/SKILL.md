---
name: architecture-modernization
description: Architecture assessment, boundary definition, safe architecture evolution, target architecture design, dependency direction, migration sequencing, and ADR creation.
---

# Architecture Modernization Skill

## Purpose
Governs the evolutionary transformation of legacy or monolithic Android architectures into modern, modular, testable systems following Google's official Android Architecture Guide and Clean Architecture principles.

## Activation Criteria
- Triggered when restructuring application layers, designing module boundaries, or migrating from monolithic Activities/Fragments to MVVM/MVI.
- Used when authoring Architecture Decision Records (ADRs) or evaluating architectural tradeoffs.

## Core Architectural Layers (Clean Android MVVM)
1. **Presentation Layer (`com.example.fomo.ui` / `presentation`)**:
   - Activities/Fragments observe immutable UI state. Zero direct business or mock data creation.
   - Jetpack ViewBinding for zero-overhead, compile-time view lookup safety.
   - `ViewModel` with `LiveData` or `StateFlow` managing asynchronous UI state transitions (`Loading`, `Success`, `Error`).
2. **Domain Layer (`com.example.fomo.domain`)**:
   - Pure, framework-agnostic models (`WeatherForecast`, `HourlyForecast`, `WeatherCondition`).
   - Repository interfaces (`WeatherRepository`) defining contracts without knowing implementation details.
   - Use cases / Interactors encapsulating business logic when complexity warrants.
3. **Data Layer (`com.example.fomo.data`)**:
   - Repository implementations orchestrating local cache and remote data sources.
   - Single Source of Truth (SSOT) pattern.

## Verification
- Architectural fitness verification (no UI imports in domain; no direct database/HTTP access in UI).
- Every major structural decision documented as an ADR in `docs/upgrade/08-architecture-decisions.md`.
