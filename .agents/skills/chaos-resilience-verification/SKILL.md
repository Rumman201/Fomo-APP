---
name: chaos-resilience-verification
description: Fault injection, network flapping, upstream outage simulation, circuit breaker state verification, and graceful offline degradation.
---

# Chaos & Resilience Verification Skill

## Purpose
Establishes controlled fault injection hypotheses to verify client-side resilience under network failures, corrupted payloads, and API degradation.

## Core Directives
1. Inject network packet loss, DNS resolution failures, and HTTP 5xx responses.
2. Verify `ResilienceCircuitBreaker` transitions (`CLOSED` -> `OPEN` -> `HALF_OPEN` -> `CLOSED`).
3. Ensure the UI gracefully renders cached L1/L2 data with clear status indicators, never showing blank views or crashing.

