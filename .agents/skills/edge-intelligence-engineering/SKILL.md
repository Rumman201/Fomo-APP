---
name: edge-intelligence-engineering
description: On-device deterministic intelligence, thermal comfort calculation, activity suitability scoring, offline rule processing, and low-latency client heuristics.
---

# Edge Intelligence Engineering Skill

## Purpose & Scope
This skill governs the design and deployment of on-device (edge) intelligence, local algorithmic decision models, and mathematical heuristics. It enables instant, zero-latency, offline-capable insights directly on the mobile device without relying on continuous cloud or network round-trips.

## When to Activate
- Calculating thermal comfort indices (Wind Chill, Heat Index, Humidex) locally on the Android device.
- Computing multi-factor activity suitability scores (Running, Commute, Cycling, Outdoor Dining).
- Deriving clothing and attire recommendations based on temperature, precipitation, and wind.
- Optimizing battery and CPU usage during local mathematical computations.

## Engineering Invariants
1. **Mathematical Accuracy**: All edge formulas (e.g. NOAA Steadman Heat Index equation, Environment Canada Wind Chill formula) must be strictly implemented and tested.
2. **Deterministic Reproducibility**: Given the same input metrics, the edge algorithms must produce identical results every time.
3. **Zero Network Dependence**: Edge calculations must execute 100% offline with zero cloud latency and zero external API dependencies.
4. **Main Thread Safety**: Compute-intensive calculations must never block the Android UI thread.

## Step-by-Step Procedure
1. **Algorithm Selection**: Choose proven meteorological equations and decision trees.
2. **Domain Implementation**: Author pure Java/Kotlin utility classes in `domain/model` or `domain/service`.
3. **Boundary Verification**: Test formula outputs across full operational ranges (-40°C to +50°C, 0–100% RH).
4. **Performance Profiling**: Ensure execution time per calculation is sub-millisecond to prevent UI stutter.
5. **UI Integration**: Expose calculated insights through LiveData/StateFlow to ViewModels and UI adapters.

