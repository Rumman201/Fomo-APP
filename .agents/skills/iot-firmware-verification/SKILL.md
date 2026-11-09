---
name: iot-firmware-verification
description: Ambient and personal weather station telemetry protocol testing, physical boundary bounds, replay window validation, and batch limits.
---

# IoT & Weather Station Verification Skill

## Purpose
Governs protocol contract testing, edge telemetry ingestion, timestamp freshness boundaries, and sensor outlier shedding.

## Core Directives
1. Validate strict physical sensor bounds ($[-60^\circ\text{C}, +60^\circ\text{C}]$, $0-100\%$, $800-1100\text{ hPa}$).
2. Test anti-replay security window (reject telemetry > 24h old or > 1h future).
3. Validate batch caps (max 500 items) and sanitize station identifiers.

