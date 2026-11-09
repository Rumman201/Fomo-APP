---
name: android-ui-ux-engineering
description: Android layout optimization, Material Design 3 guidelines, typography, responsive scaling, TalkBack accessibility, and UI polish.
---

# Android UI/UX Experience Engineering Skill

## Purpose
Establishes principles for modern, fluid, accessible, and performant Android user interfaces following Material Design 3 and Google Web/App Interface Guidelines.

## Activation Criteria
- Triggered when designing or modifying layouts (`res/layout/`), themes (`res/values/themes.xml`), styles, drawables, or animations.
- Used when resolving layout hierarchy issues, duplicate IDs, font scaling defects, or accessibility warnings.

## Quality Rules
1. **Layout Hierarchy Flattening**:
   - Avoid deep nesting of `LinearLayout` and `ConstraintLayout`.
   - Never nest empty or unconstrained container layouts.
   - Prevent multi-layer overdraw by avoiding redundant background drawables on parent and child views.
2. **Dimension & Unit Discipline**:
   - Use `sp` exclusively for typography (`textSize`) to respect user font size scaling.
   - Use `dp` for margins, padding, elevation, and corner radii (`corners android:radius`).
   - Use `0dp` with layout weights in `LinearLayout` (`InefficientWeight`).
3. **Typography & Content Integrity**:
   - Zero hardcoded strings in XML layouts or Java/Kotlin code; extract all user-facing copy to `strings.xml`.
   - Ensure grammatical correctness, proper capitalization, and localization readiness.
4. **Accessibility (a11y)**:
   - Provide meaningful `android:contentDescription` for informative images; set `@null` or `importantForAccessibility="no"` for purely decorative elements.
   - Minimum touch target size: 48dp x 48dp.
5. **ID Integrity**:
   - Every view ID within a layout must be unique, descriptive, and camelCase or snake_case consistently.
