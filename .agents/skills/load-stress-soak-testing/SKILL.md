---
name: load-stress-soak-testing
description: Synthetic load generation, concurrency stress tests, memory leak soak testing, cache hit ratio benchmarking, and capacity boundary evaluation.
---

# Load, Stress, and Soak Testing Skill

## Purpose & Scope
Provides automated testing methodologies for evaluating system stability under synthetic concurrent loads, stress conditions, sustained soak durations, and resource pressure.

## Specialist Responsibilities
- **Principal Performance QA Engineer**: Designs reproducible benchmark harnesses, executes high-concurrency simulation scripts, and measures tail latencies (p95/p99).

## Methodology & Execution Steps
1. **Workload Modeling**: Model realistic user interaction patterns (rapid city switching, continuous pull-to-refresh, concurrent background evaluations).
2. **Microbenchmarking**: Implement repeatable JUnit test harnesses measuring execution time across hundreds of iterations.
3. **Soak & Leak Testing**: Execute repeated cycles of data fetching, parsing, and caching to detect memory leaks, unclosed cursors, or unreleased thread allocations.
4. **Saturation & Degradation Testing**: Verify system behavior when thread pools or cache capacities are saturated.
5. **Statistical Metrics Reporting**: Report min, mean, median, p95, and max execution times across benchmarks.

