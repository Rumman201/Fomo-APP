---
name: frontend-performance-profiling
description: Android rendering performance profiling, GPU overdraw elimination, view hierarchy flattening, layout pass optimization, and memory efficiency.
---

# Frontend Performance Profiling Skill

## Purpose & Scope
This skill provides standards for measuring and optimizing Android layout rendering, view inflation times, GPU overdraw, memory allocation, and 60fps scrolling smoothness.

## When to Activate
- Measuring frame render times and detecting dropped frames (jank).
- Auditing view hierarchies for unnecessary layout nesting.
- Detecting and eliminating GPU overdraw (overlapping solid backgrounds).
- Verifying RecyclerView view holder recycling and image caching efficiency.

## Engineering Standards & Invariants
1. **60fps Frame Budget (16.6ms)**:
   - Layout measurement and draw passes must complete in under 16.6ms per frame to prevent stutter.
2. **Zero Redundant Overdraw**:
   - The root window background must not be redundantly redrawn beneath full-screen opaque child views.
3. **Flat View Hierarchy**:
   - Maximize shallow hierarchies using `ConstraintLayout` or single-level weighted `LinearLayouts` over deep multi-nested layouts.
4. **Efficient Recycling**:
   - `HourlyAdapter` must reuse view holders via `RecyclerView.setHasFixedSize(true)` and `ListAdapter` DiffUtil diffing.

## Step-by-Step Procedure
1. **Hierarchy Inspection**:
   - Count view tree depth from root to leaf nodes.
2. **Overdraw Audit**:
   - Remove redundant `android:background` declarations where parent containers already draw the background.
3. **Recycling Verification**:
   - Verify that fast horizontal scrolling does not cause GC pauses or dropped frames.
