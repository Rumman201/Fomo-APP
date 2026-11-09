---
name: premium-interaction-design
description: Touch feedback, material ripples, bottom sheet dialogs, pull-to-refresh interactions, and state transition modeling.
---

# Premium Interaction Design Skill

## Purpose & Scope
This skill addresses interactive ergonomics, tactile feedback, state transitions, and interactive sheets/dialogs for Android mobile interfaces.

## When to Activate
- Implementing interactive user flows such as Pull-to-Refresh (`SwipeRefreshLayout`).
- Designing modal bottom sheets (`BottomSheetDialogFragment`) for detailed multi-day forecasts.
- Adding ripple effects (`?attr/selectableItemBackground`) and elevation changes on click/press.
- Designing responsive error and loading interaction states.

## Engineering Standards & Invariants
1. **Immediate Touch Feedback**:
   - Every tappable component must provide instant visual feedback (ripple effect, slight elevation lift, or scale feedback) within 16ms of touch down.
2. **Predictable Navigation**:
   - Secondary workflows (e.g. 7-day forecast breakdown) should open non-destructively in modal bottom sheets, allowing users to swipe down to dismiss without losing their primary context.
3. **Pull-to-Refresh Feedback**:
   - Provide visual feedback during manual refresh, updating UI data seamlessly without flickering or abrupt layout jumps.
4. **Resilient Error Recovery**:
   - When network or data errors occur, provide an actionable "Retry" button that re-triggers data loading without requiring an app restart.

## Step-by-Step Procedure
1. **Flow Specification**:
   - Map user gestures (pull down, tap, swipe) to system actions.
2. **Component Integration**:
   - Wire `SwipeRefreshLayout` and `BottomSheetDialog`.
3. **Touch Feedback**:
   - Attach Material ripple drawables to clickable cards and buttons.
4. **Verification**:
   - Verify that gestures do not conflict with parent scroll views.
