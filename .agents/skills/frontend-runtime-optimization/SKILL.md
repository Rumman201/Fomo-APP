---
name: frontend-runtime-optimization
description: Android UI rendering optimization, layout flattening, view inflation tuning, memory leak elimination, and 60/120fps frame budget preservation.
---

# Frontend Runtime Optimization Skill

## Purpose & Scope
Provides architectural procedures for optimizing Android Activity and View inflation, minimizing main-thread layout passes, eliminating GPU overdraw, tuning RecyclerView viewholder pooling, and maintaining silky 60/120fps frame rates.

## Specialist Responsibilities
- **Principal Frontend Performance Engineer**: Owns view hierarchy depth, layout pass counts, adapter recycling behavior, and UI thread responsiveness.

## Methodology & Execution Steps
1. **Layout Inflation Profiling**: Audit XML layout hierarchies for redundant nested containers (`ConstraintLayout`, `LinearLayout`) and unnecessary background drawables.
2. **Overdraw Elimination**: Ensure window backgrounds and parent containers do not paint overlapping opaque pixels.
3. **RecyclerView Optimization**: Configure `setHasFixedSize(true)`, custom item view cache sizes (`setItemViewCacheSize`), and shared `RecycledViewPool` instances.
4. **Bitmap & Asset Optimization**: Tune Glide image loading with explicit target pixel dimensions and hardware bitmap configurations (`RGB_565` where suitable).
5. **Memory Leak Prevention**: Enforce strict lifecycle-aware observer detachment and zero static context retention.

