---
name: frontend-design-system
description: Design tokens, Material Design 3 theme architecture, semantic colors, typography scales, component styles, and design governance.
---

# Frontend Design System Skill

## Purpose & Scope
This skill provides standards for structuring, maintaining, and scaling design tokens, styles, and reusable component definitions across Android layouts and theme resources.

## When to Activate
- Defining or refining `colors.xml`, `dimens.xml`, `styles.xml`, and `themes.xml`.
- Creating reusable Material Design 3 styles (e.g. Card, Button, MetricItem, Badge).
- Enforcing semantic naming conventions across design tokens.
- Auditing layouts to eliminate hardcoded hex colors, arbitrary margins, and inline font sizes.

## Engineering Standards & Invariants
1. **Semantic Token Architecture**:
   - Primitive tokens (e.g. `#59469D`) must map to semantic tokens (e.g. `@color/fomo_surface_card`).
   - Zero hardcoded colors (e.g. `android:textColor="#DCA900"`) inside layout XML files.
2. **Dimension Scales**:
   - All padding, margins, corner radii, and view dimensions must reference `@dimen/*` resources based on an 8dp grid (4dp, 8dp, 16dp, 24dp, 32dp).
3. **Typography Tokens**:
   - Map text appearances to Material Design 3 styles: `TextAppearance.Material3.DisplayLarge`, `TitleMedium`, `BodyMedium`, etc.
4. **Theme Inheritance**:
   - All styles must inherit from `Theme.Material3.DayNight.NoActionBar` or standard Material3 component styles.

## Step-by-Step Procedure
1. **Token Extraction**:
   - Extract colors to `res/values/colors.xml`, dimensions to `res/values/dimens.xml`.
2. **Component Style Definition**:
   - Define card, text, and container styles in `res/values/styles.xml`.
3. **Layout Binding**:
   - Replace raw inline attributes in layout XMLs with `style="@style/..."` references.
4. **Validation**:
   - Run Android Lint to verify zero unresolved resources and no duplicate styles.
