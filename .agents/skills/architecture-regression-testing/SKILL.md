---
name: architecture-regression-testing
description: Architecture invariants, dependency-cycle checks, module boundary enforcement, public contract verification, characterization testing, and regression prevention.
---

# Architecture Regression Testing Skill

## Purpose
Ensures that architectural modernization does not introduce silent regressions into application behavior, lifecycle handling, view inflation, or data flow contracts.

## Activation Criteria
- Triggered whenever an architectural refactoring is executed (e.g. migrating to ViewModel, moving classes to new packages).
- Used when building unit tests for ViewModels, repositories, and adapters.

## Architecture Testing Checklist
1. **Model Contract Tests**: Verify immutability, equality, and hash code behavior for domain entities (`HourlyTest`).
2. **Repository Contract Tests**: Verify repository methods emit expected data structures and handle empty/error states deterministically (`WeatherRepositoryTest`).
3. **ViewModel State Tests**: Verify that ViewModel transitions through expected states (`Loading` -> `Success`) upon initialization or refresh (`WeatherViewModelTest`).
4. **Adapter Diff Tests**: Verify that `DiffUtil.ItemCallback` accurately computes item identity and content changes (`HourlyDiffCallbackTest`).
5. **Static Analysis Gate**: Verify zero Android Lint errors on the refactored architecture.
