---
name: iot-feature-engineering
description: Evaluation of hardware footprints, ambient weather station telemetry integration contracts, edge-to-mobile data ingestion, and simulated sensor validation.
---

# IoT Feature Engineering Skill

## Purpose & Scope
This skill provides standards for evaluating Internet of Things (IoT) hardware footprints, designing telemetry ingestion pipelines, defining edge-to-mobile data contracts, and safely testing sensor integration. It enforces truth in engineering: never representing simulated devices as verified physical hardware.

## When to Activate
- Assessing project compatibility with physical sensors, microcontrollers, or edge gateways.
- Defining contracts for ingesting personal weather station (PWS) telemetry (e.g. Ambient Weather, Netatmo, Davis).
- Implementing sensor data normalization, validation, and unit conversions.
- Building deterministic device simulators for integration testing without physical hardware.

## Engineering Invariants
1. **Architectural Truthfulness**: If no physical microcontrollers or firmware are deployed, explicitly classify physical IoT as Not Applicable while providing contract specifications.
2. **Deterministic Telemetry Validation**: Sensor readings must be validated against physical bounds (e.g., temperature between -90°C and +60°C, humidity 0–100%).
3. **Safety First**: Simulated device data must never override official meteorological severe weather alerts or user life-safety protocols.
4. **Resilient Offline Ingestion**: Support edge buffering and intermittent connectivity without crashing or stalling the application.

## Step-by-Step Procedure
1. **Footprint Assessment**: Determine whether the application directly interfaces with physical hardware or consumes standard telemetry feeds.
2. **Telemetry Contract Design**: Formulate standardized JSON schemas for personal weather station telemetry.
3. **Validation & Normalization**: Implement boundary validators to detect corrupted, negative, or impossible sensor readings.
4. **Simulator Implementation**: Author realistic telemetry simulators for reproducible integration testing.
5. **Contract Verification**: Verify that the repository cleanly parses, caches, and presents station data.

