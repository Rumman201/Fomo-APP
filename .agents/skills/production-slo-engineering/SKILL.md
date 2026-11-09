---
name: production-slo-engineering
description: Service Level Indicators (SLIs), Service Level Objectives (SLOs), error budgets, latency percentile tracking, and performance observability design.
---

# Production SLO Engineering Skill

## Purpose & Scope
Governs the definition, calculation, and monitoring of Service Level Indicators (SLIs) and Service Level Objectives (SLOs) for mobile applications, defining acceptable error budgets and availability thresholds.

## Specialist Responsibilities
- **Principal SRE / Systems Architect**: Formalizes SLI definitions, sets realistic SLO targets, and establishes telemetry for monitoring performance health.

## Methodology & Execution Steps
1. **User-Centric SLI Identification**: Define measurable indicators reflecting user experience (e.g. Weather Refresh Latency < 1000ms, UI Frame Render < 16ms, City Search Latency < 400ms).
2. **SLO Target Definition**: Establish quantifiable compliance targets (e.g. 99% of requests meet latency threshold over 30-day window).
3. **Error Budget Tracking**: Calculate remaining allowable degraded transactions:
   $$\text{Error Budget} = 1.0 - \text{SLO Target}$$
4. **Telemetry Instrumentation**: Instrument lightweight timers and success/failure counters in repository and service layers.
5. **Operational Alerting Rules**: Establish threshold triggers when error budgets burn at an accelerated rate.

