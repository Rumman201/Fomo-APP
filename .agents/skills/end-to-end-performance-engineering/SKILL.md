---
name: end-to-end-performance-engineering
description: End-to-end performance strategy, critical path tracing, workload profiling, latency budget enforcement, and system bottleneck analysis for Android and mobile systems.
---

# End-to-End Performance Engineering Skill

## Purpose & Scope
Provides systematic engineering guidelines for establishing reproducible performance baselines, mapping system-wide critical execution paths, identifying CPU and I/O bottlenecks, and enforcing latency budgets across user journeys.

## Specialist Responsibilities
- **Principal Performance Architect**: Formulates performance models, traces cross-layer spans (UI -> ViewModel -> Repository -> Network/Database), and establishes quantitative latency targets.

## Methodology & Execution Steps
1. **Critical Path Tracing**: Deconstruct user workflows into discrete measurable milestones (Cold Start, Geocoding Search, Weather Refresh, Forecast Render).
2. **Span Profiling**: Measure wall-clock execution time and CPU cycles consumed across presentation, domain, and data layers.
3. **Bottleneck Classification**: Categorize bottlenecks into CPU-bound, I/O-bound, thread starvation, lock contention, or serialization overhead.
4. **Latency Budget Enforcement**: Maintain p50, p95, and p99 percentile thresholds for interactive actions.
5. **Continuous Verification**: Execute automated microbenchmarks and regression tests to prevent performance degradation.

