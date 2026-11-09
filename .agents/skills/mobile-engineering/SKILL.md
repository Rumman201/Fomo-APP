---
name: mobile-engineering
description: Android mobile development best practices, lifecycle management, API level compatibility, background tasks, and networking standards.
---

# Mobile Engineering Skill (Android Specialist)

## Purpose
Enforces high-quality Android development patterns, respecting Android platform lifecycles, threading policies, network resilience, and backward compatibility.

## Activation Criteria
- Triggered when implementing or modifying Android Activities, Fragments, Services, Adapters, or ViewModels.
- Used when integrating networking, background work (WorkManager), or asynchronous processing (Coroutines/Flows).

## Core Android Invariants
1. **Lifecycle Safety**:
   - Never retain View or Context references in static fields or long-lived singletons.
   - Use `LifecycleOwner` aware observers (`lifecycleScope`, `repeatOnLifecycle`).
2. **Threading & Concurrency**:
   - Never perform disk I/O, database access, or network calls on the Main (UI) thread.
   - Use `Dispatchers.IO` for I/O operations and `Dispatchers.Main` for UI rendering.
3. **API Level Compatibility**:
   - Strictly honor `minSdk` (e.g. API 24).
   - Guard newer APIs (`SDK_INT >= Build.VERSION_CODES.*`) or use AndroidX backward-compatible wrappers.
   - Avoid restricted internal APIs (e.g. `getOpPackageName()`).
4. **Adapter Best Practices**:
   - Use `ListAdapter` with `DiffUtil.ItemCallback` instead of raw `RecyclerView.Adapter` with full data reset.
   - Avoid reflection calls like `getResources().getIdentifier()` inside `onBindViewHolder`.

## Verification Steps
- Build debug variant cleanly: `./gradlew assembleDebug`.
- Verify Android Lint reports zero `NewApi` errors.
