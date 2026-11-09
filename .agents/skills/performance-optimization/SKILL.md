---
name: performance-optimization
description: Android app startup optimization, memory leak prevention, layout rendering efficiency, image caching, and battery footprint reduction.
---

# Android Performance Optimization Skill

## Purpose
Establishes benchmarking, profiling, and optimization procedures for 60/120fps frame rendering, fast cold startup, minimal memory footprint, and low battery consumption.

## Activation Criteria
- Triggered when addressing UI jank, frame drops in RecyclerView scrolling, slow app startup, or high memory usage.
- Used when optimizing image loading (Glide/Coil), layout inflation, or background sync intervals.

## Performance Checkpoints
1. **Layout & Overdraw**:
   - Inspect GPU Overdraw in Developer Options (aim for true 1x/no overdraw on background areas).
   - Eliminate redundant background declarations on nested layout containers.
   - Use `ViewStub` or lazy inflation for conditionally displayed UI.
2. **List / RecyclerView Rendering**:
   - Never perform expensive operations (reflection, formatting heavy dates, complex regex) inside `onBindViewHolder`.
   - Set fixed size `setHasFixedSize(true)` when item bounds do not change dynamically.
   - Cache pre-resolved resource IDs and formatted strings in model objects.
3. **Image Loading**:
   - Configure Glide with appropriate disk and memory caching policies.
   - Resize images to match target view dimensions (`override(width, height)`) to prevent bitmap memory bloat.
4. **Memory Leak Mitigation**:
   - Never pass Activity Context into background threads or static fields; use Application Context if long-lived reference is required.
   - Clean up ViewBinding or listeners in `onDestroyView()` for Fragments.
