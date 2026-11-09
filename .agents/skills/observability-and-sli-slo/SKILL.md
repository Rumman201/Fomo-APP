---
name: observability-and-sli-slo
description: Client-side observability architecture, HealthCheckService runtime diagnostics, AppMetricsCollector SLI/SLO tracking, and zero-allocation telemetry.
---

# Observability and SLI/SLO Engineering Skill

## Purpose
Establishes reliable client-side operational observability, Service Level Indicators (SLIs), Service Level Objectives (SLOs), and health check diagnostics.

## Core Directives
1. Implement zero-allocation atomic metric tracking (`AppMetricsCollector`) for cache hit ratios and network success rates.
2. Provide non-destructive health checks (`HealthCheckService`) evaluating database, keystore, and circuit breaker vitality.
3. Enforce strict privacy redaction: never log raw GPS coordinates, PII, or security keys.

