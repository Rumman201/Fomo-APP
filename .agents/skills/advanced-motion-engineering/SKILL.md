---
name: advanced-motion-engineering
description: Android animation architecture, subtle entrance transitions, recycler item choreography, microinteractions, and reduced-motion compliance.
---

# Advanced Motion Engineering Skill

## Purpose & Scope
This skill governs the motion design, transitions, physics-based springs, and microinteractions of Android user interfaces. Motion must inform and guide the user rather than distract or cause interface lag.

## When to Activate
- Implementing entrance and exit transitions for views and state updates (Loading -> Success -> Error).
- Designing interactive microinteractions on buttons, cards, and pull-to-refresh actions.
- Choreographing RecyclerView item insertions and scroll feedback.
- Supporting Android accessibility motion settings (`ANIMATOR_DURATION_SCALE` / reduced motion).

## Engineering Standards & Invariants
1. **Purposeful Motion**:
   - Every animation must communicate state change, spatial hierarchy, or action confirmation. Never add purely decorative animation loops that drain battery.
2. **Durations & Easing**:
   - Short microinteractions: 150–200ms with fast-out-slow-in easing (`@android:interpolator/fast_out_slow_in`).
   - Content entrances / transitions: 250–350ms.
   - Screen cross-fades: 200ms.
3. **Compositor Efficiency**:
   - Prefer animating composite-only properties (`alpha`, `translationX`, `translationY`, `scaleX`, `scaleY`) via `ViewPropertyAnimator`.
   - Never animate layout dimensions (`width`, `height`, `layout_margin`) continuously, as this triggers expensive layout recalculations per frame.
4. **Reduced-Motion Respect**:
   - Check system animator duration settings. If animations are disabled, snap directly to the final state.

## Step-by-Step Procedure
1. **Motion Design**:
   - Map each state transition to appropriate alpha and translation offsets.
2. **Implementation**:
   - Use Android `ViewPropertyAnimator` or `TransitionManager.beginDelayedTransition()`.
3. **Performance Profiling**:
   - Verify 60fps frame rate without dropped frames during scrolling or state swaps.
4. **Verification**:
   - Verify that all animated views cleanly retain their final state and remove listeners on view detach.
