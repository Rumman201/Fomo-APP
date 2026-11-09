---
name: iot-firmware-security
description: Device identity, protocol security, personal weather station telemetry validation, sensor boundary enforcement, and command safety.
---

# IoT & Firmware Security Skill

## Purpose & Scope
This skill governs the security assessment and boundary protection of Internet of Things (IoT) systems, personal weather station (PWS) telemetry pipelines, and edge device communications.

## When to Activate
- Validating external weather station telemetry feeds for out-of-bounds corruption or tampering.
- Enforcing timestamp freshness and anti-replay windows on incoming sensor readings.
- Assessing device identity boundaries without falsely asserting nonexistent physical microcontroller hardware.
- Hardening edge telemetry ingestion against memory corruption or denial of service.

## Engineering Invariants
1. **Architectural Truthfulness**: Explicitly document that no physical embedded hardware or microcontroller firmware is deployed in the mobile client.
2. **Strict Sensor Boundary Validation**: Validate all telemetry against terrestrial bounds ($-90^\circ\text{C}$ to $+60^\circ\text{C}$, $0-100\%$, $800-1100\text{ hPa}$, $0-350\text{ km/h}$).
3. **Replay & Freshness Protection**: Telemetry timestamps must be validated against current system time (rejecting timestamps $> 24$ hours old or $> 1$ hour in future).
4. **Safety Interlock**: Ingested station readings must never override official meteorological severe weather alerts or user safety protocols.

## Step-by-Step Procedure
1. **Pipeline Audit**: Inspect `IotWeatherStationService` and telemetry DTO schemas.
2. **Replay Guard Implementation**: Add timestamp delta validation against system epoch time.
3. **Station Identifier Whitelist/Pattern**: Validate station ID format against alphanumeric regex.
4. **Adversarial Ingestion Testing**: Author unit tests passing replayed, future, and corrupt telemetry payloads.
5. **Documentation**: Record validation results in Phase 6 documentation.

