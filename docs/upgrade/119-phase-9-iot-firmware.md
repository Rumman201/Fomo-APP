# 119 — Phase 9 IoT Personal Weather Station Telemetry Verification

## IoT & Edge Ingestion Verification

`IotWeatherStationService` validates edge telemetry from ambient and personal weather stations:

---

## 1. Physical Sensor Boundary Validation
- Temperature: $[-90^\circ\text{C}, +60^\circ\text{C}]$
- Humidity: $[0\%, 100\%]$
- Pressure: $[800\text{ hPa}, 1100\text{ hPa}]$
- Wind Speed: $[0\text{ km/h}, 350\text{ km/h}]$
- **Defect Remediation (DEF-QA-01)**: Added `Double.isNaN()` and `Double.isInfinite()` guards, ensuring corrupted sensor streams cannot bypass boundary checks.
- **Verified by**: `IotTelemetryBoundaryRedTeamTest.testCorruptedNaNTimestampAndReadingsRejected`.

---

## 2. Anti-Replay Security Window
- Rejects telemetry older than 24 hours (`MAX_ALLOWED_AGE_MS`).
- Rejects telemetry skewed more than 1 hour into the future (`MAX_ALLOWED_FUTURE_SKEW_MS`).
- Prevents replay attacks from malicious or out-of-sync edge sensor hubs.
- **Verified by**: `IotTelemetryBoundaryRedTeamTest.testReplayAttackExpiredTimestampRejected`.

