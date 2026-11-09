---
name: responsive-experience-engineering
description: Android adaptive layouts, multi-density support, tablet and foldable scaling, orientation handling, and touch target ergonomics.
---

# Responsive Experience Engineering Skill

## Purpose & Scope
This skill addresses responsive layout scaling across diverse Android screen sizes, aspect ratios, orientations, and display densities (sw320dp small handsets to sw600dp tablets and foldables).

## When to Activate
- Designing layouts that adapt across compact, medium, and expanded window size classes.
- Ensuring content does not clip or overflow on smaller screens (320dp width).
- Optimizing horizontal RecyclerView and card carousel padding for different display widths.
- Testing landscape orientation reflow and keyboard appearance scenarios.

## Engineering Standards & Invariants
1. **Window Size Class Awareness**:
   - Compact (< 600dp): Single-column scrollable stream with horizontal hourly carousel.
   - Expanded (>= 600dp): Balanced horizontal split or multi-column metric dashboard.
2. **Touch Target Size**:
   - Interactive elements must maintain a minimum touch target of 48dp x 48dp.
3. **No Hardcoded Screen Sizes**:
   - Never assume fixed 1080x1920 or fixed aspect ratios. Use `match_parent`, `wrap_content`, and `0dp` (ConstraintLayout / LinearLayout weights).
4. **Scroll Container Protection**:
   - Long vertical content must be wrapped in `ScrollView` or `NestedScrollView` to prevent vertical clipping on small screens or large font scale settings.

## Step-by-Step Procedure
1. **Layout Constraints Audit**:
   - Verify all views have appropriate horizontal and vertical constraints.
2. **Density Qualification**:
   - Provide density-appropriate assets in `drawable-hdpi`, `xhdpi`, `xxhdpi`.
3. **Multi-Window & Orientation**:
   - Ensure state is preserved across configuration changes via ViewModel.
