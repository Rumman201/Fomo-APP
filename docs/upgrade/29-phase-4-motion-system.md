# 29 — Phase 4 Advanced Motion & Microinteraction System

## 1. Motion Principles

1. **Subtle & Informative**:
   - Motion is used exclusively to signal state changes (data loaded, refreshing, error reported) and interactive affordances.
   - Durations are strictly budgeted between 150ms and 300ms to maintain snappy responsiveness.
2. **Compositor Efficiency**:
   - All dynamic view transitions animate composite-only properties (`alpha`, `translationY`) using `ViewPropertyAnimator`.
   - Never animate layout geometry (`width`, `height`, margins) continuously.
3. **Accessibility (Reduced-Motion Compliance)**:
   - Evaluated via `Settings.Global.ANIMATOR_DURATION_SCALE`.
   - If animations are turned off or scaled to 0 in device accessibility settings, views snap immediately to their terminal state with zero duration.

---

## 2. Implemented Motion Sequences

### Entrance Choreography (`MainActivity.animateContentEntrance()`)
- **Trigger**: New weather intelligence received from `WeatherViewModel` (Success state).
- **Interpolation**: `DecelerateInterpolator` over 300ms.
- **Properties**:
  - Initial: `alpha = 0.6f`, `translationY = 30f`.
  - Final: `alpha = 1.0f`, `translationY = 0f`.
- **Cleanup**: `AnimatorListenerAdapter` resets alpha and translation to terminal values upon animation completion or cancellation.

### Microinteractions & Touch Feedback
- **Pull-to-Refresh**: Material `SwipeRefreshLayout` with brand colors (`fomo_accent_electric_violet`, `fomo_accent_gold`).
- **Hourly Card Ripple**: `bg_hourly_card.xml` provides Material ripple on touch down and subtle gold border highlight on press.
- **7-Day Outlook Button**: Material ripple feedback (`?attr/selectableItemBackground`) on tap before launching the bottom sheet.
