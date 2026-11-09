---
name: safe-large-scale-refactoring
description: Characterization testing, incremental code extraction, behavior-preserving refactors, compatibility adapters, regression verification, risk isolation, and rollback procedures.
---

# Safe Large-Scale Refactoring Skill

## Purpose
Enforces disciplined, behavior-preserving refactoring workflows that protect existing features, prevent regressions, and isolate structural changes during major migrations.

## Activation Criteria
- Triggered when moving classes across packages, breaking up god classes, modifying public interfaces, or swapping foundational adapters.
- Used when extracting business logic from Activities or replacing legacy patterns.

## Refactoring Protocol
1. **Characterization First**: Write unit tests capturing existing behavior, valid inputs, boundary conditions, and error cases before altering production code.
2. **Parallel Run & Compatibility Adapters**: Introduce new interfaces or modern adapters without immediately deleting legacy callers if callers exist elsewhere.
3. **Small Bounded Commits**: Keep edits scoped. Never mix cosmetic reformatting with structural package moves or architectural refactoring.
4. **Compile & Verification Loop**: Execute `./gradlew testDebugUnitTest` and `./gradlew lintDebug` after every incremental step.
5. **Rollback Safety**: Maintain clear git branch points for instant reversion if unanticipated circular dependencies or regressions emerge.
