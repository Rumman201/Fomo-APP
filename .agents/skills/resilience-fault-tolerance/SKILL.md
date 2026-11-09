---
name: resilience-fault-tolerance
description: Circuit breaker pattern, exponential backoff with jitter, graceful offline degradation, fallback telemetry serving, and operational resilience for mobile systems.
---

# Resilience and Fault Tolerance Skill

## Purpose & Scope
Provides architectural mechanisms for surviving network outages, upstream 5xx errors, rate limiting, and intermittent connectivity failures safely and gracefully.

## Specialist Responsibilities
- **Principal Site Reliability Engineer (SRE)**: Designs circuit breakers, implements retry budgets, defines fallback strategies, and monitors error budgets.

## Methodology & Execution Steps
1. **Circuit Breaker Architecture**: Implement a state machine (`CLOSED`, `OPEN`, `HALF_OPEN`) tracking failure rates over rolling request windows.
2. **Fast-Failure in OPEN State**: Instantly trip to offline cached data when upstream APIs fail repeatedly, preventing slow timeouts and battery drain.
3. **Exponential Backoff with Full Jitter**: Prevent synchronized retry storms by randomizing retry intervals:
   $$t_{retry} = \text{random}(0, \min(t_{max}, t_{base} \cdot 2^{attempt}))$$
4. **Graceful UI Degradation**: Present stale cached forecasts with informative timestamp badges rather than blank screens or disruptive error dialogs.
5. **Circuit Reset Verification**: Transition to `HALF_OPEN` after a cooldown period (e.g. 30s) to probe upstream service recovery.

