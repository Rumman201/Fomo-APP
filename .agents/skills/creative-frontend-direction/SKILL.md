---
name: creative-frontend-direction
description: Visual identity direction, art composition, typography hierarchy, cohesive brand expression, and anti-template UI design for Android and mobile platforms.
---

# Creative Frontend Direction Skill

## Purpose & Scope
This skill governs the visual personality, art direction, aesthetic harmony, and brand identity of the mobile application. It ensures the user interface feels deliberate, premium, and purpose-built rather than assembled from generic templates or arbitrary AI-generated components.

## When to Activate
- Setting or revising the visual language, color palette, or typography system.
- Designing high-impact weather overview cards, hero headers, or status visuals.
- Evaluating visual balance, negative space, contrast, and visual rhythm.
- Eliminating generic design antipatterns (overused purple gradients, repetitive cards, meaningless ornamentation).

## Engineering Standards & Invariants
1. **Deliberate Visual Personality**:
   - Palette and typography must evoke calm, reliable atmospheric intelligence.
   - Maintain the established atmospheric indigo/violet twilight aesthetic while introducing rich depth, contrast, and clarity.
2. **Typography Hierarchy**:
   - Enforce distinct font scales: Hero display (>50sp), prominent temperature, semantic labels (14–16sp), captions (12sp).
   - Use `sp` units exclusively for typography to honor system font scaling.
3. **Contrast & Legibility**:
   - Maintain a minimum 4.5:1 contrast ratio for all body and metric text against background surfaces (WCAG AA).
4. **Cohesive Spatial Rhythm**:
   - Enforce a strict 8dp spatial baseline grid (8dp, 16dp, 24dp, 32dp).

## Step-by-Step Procedure
1. **Visual Audit**:
   - Identify visual noise, poor contrast, and inconsistent spacing across existing layouts.
2. **Palette & Tokens Formulation**:
   - Define semantic colors in `colors.xml` and spatial dimensions in `dimens.xml`.
3. **Composition & Hierarchy**:
   - Establish hero focal points (current condition, temperature) followed by contextual metrics and chronological hourly forecasts.
4. **Review & Critique**:
   - Verify that the layout communicates essential weather data effortlessly at a single glance.
