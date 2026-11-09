---
name: visual-regression-and-browser-qa
description: Mobile layout inspection, multi-viewport verification, interactive state testing, screenshot baselines, and Android Lint static UI quality assurance.
---

# Visual Regression & Mobile UI QA Skill

## Purpose & Scope
This skill governs the systematic visual, functional, and responsive verification of mobile layouts, UI component states, and rendering integrity across varying screen densities and form factors.

## When to Activate
- Verifying UI visual fidelity after layout refactoring or design system upgrades.
- Testing interactive states: Loading skeletons, Success layouts, Error banners, Empty states.
- Checking layout rendering across compact handsets, large screens, and foldables.
- Validating Android Lint layout rules (`DisableBaselineAlignment`, `Overdraw`, `UselessParent`).

## Engineering Standards & Invariants
1. **Multi-State Testing**:
   - Every screen must be verified in all operational states: Initial Loading, Content Loaded (Success), and Network Failure (Error with Retry).
2. **Deterministic Visual Baselines**:
   - Layout elements must align consistently with the 8dp grid without stray pixel offsets or clipped text strings.
3. **Automated Regression Quality**:
   - Android Lint must have zero errors and zero fatal layout warnings.
4. **Interactive Integrity**:
   - All interactive controls (buttons, cards, swipe gestures) must trigger the intended state transitions.

## Step-by-Step Procedure
1. **Layout Static Audit**:
   - Run `gradlew lintDebug` on layout resources.
2. **State Simulation**:
   - Test UI with mock states in unit and integration tests.
3. **Binary Verification**:
   - Build debug APK (`assembleDebug`) and verify APK integrity.
