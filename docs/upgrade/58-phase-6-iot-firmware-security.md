# Phase 6 — IoT & Personal Weather Station (PWS) Security

## 1. Architectural Scope & Threat Boundary

In conformance with the project reality established across Phases 1–5, Fomo does not flash physical microcontroller firmware directly from the Android host. Rather, it exposes an on-device IoT Telemetry Ingestion Contract (`IotWeatherStationService`) designed to ingest readings from local personal weather stations (PWS) or edge simulated telemetry.

The threat boundary resides at the telemetry ingestion entry point, where rogue transmitters or compromised edge devices could attempt:
1. **Telemetry Injection / SQLi**: Sending station IDs containing database commands or shell metacharacters.
2. **Replay Attacks**: Re-transmitting old storm alerts or heatwave data to cause erroneous notifications.
3. **Sensor Spoofing**: Injecting physically impossible readings (e.g. 150°C, 300% humidity) to trigger algorithmic overflow or crash calculations.

---

## 2. Implemented Defense-in-Depth Controls

### 2.1 Alphanumeric Station ID Whitelist
`InputSanitizer.isValidStationId(stationId)` validates against:
$$Regex = \text{\textasciicircum}[A-Za-z0-9\_-]{3,32}\$$$
- Rejecting special characters, path traversal (`../../`), quotes, and semicolons.
- Bounding length to 32 characters.

### 2.2 Temporal Freshness & Anti-Replay Window
The ingestion service checks the telemetry timestamp against the device clock:
- Maximum age: $24 \text{ hours}$ ($86,400,000 \text{ ms}$). Replays of older observations are rejected with `Status.REPLAY_OR_EXPIRED_TIMESTAMP`.
- Maximum future skew: $1 \text{ hour}$ ($3,600,000 \text{ ms}$). Packets claiming timestamps in the far future are rejected.

### 2.3 Physical Sensor Plausibility Bounds
- Ambient Temperature: $[-90.0^\circ\text{C}, +60.0^\circ\text{C}]$.
- Relative Humidity: $[0.0\%, 100.0\%]$.
- Barometric Pressure: $[800.0\text{ hPa}, 1100.0\text{ hPa}]$.
- Wind Speed: $[0.0\text{ km/h}, 350.0\text{ km/h}]$.
- Out-of-bounds observations are dropped with explicit status classifications.

