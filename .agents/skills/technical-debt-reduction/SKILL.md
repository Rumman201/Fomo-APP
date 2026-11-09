---
name: technical-debt-reduction
description: Debt identification, root-cause analysis, risk-based prioritization, duplication removal, complexity reduction, and measurable outcomes.
---

# Technical Debt Reduction Skill

## Purpose
Provides a structured protocol for cataloging, prioritizing, and eradicating architectural, code-level, and dependency technical debt without compromising system stability.

## Activation Criteria
- Triggered when eliminating legacy workarounds, removing dead or obsolete code, cleaning up legacy packages, or replacing brittle implementations.
- Used when updating `docs/upgrade/13-phase-2-technical-debt.md`.

## Prioritization Matrix
1. **High Risk / High Friction Debt**: Prohibited reflection, NewApi calls, duplicate view IDs, package misnaming breaking tooling. (Eliminated in Phase 1 & 2).
2. **Medium Risk Debt**: Monolithic Activities holding mock data, lack of ViewBinding, raw RecyclerView adapter resetting full lists instead of using `DiffUtil`. (Targeted in Phase 2).
3. **Low Risk / Polish Debt**: Minor stylistic naming inconsistencies, unused resource drawables.

## Verification
- Measurable reduction in code complexity, warning counts, and manual view lookups.
- Zero deleted functionality or broken user flows.
