---
name: iot-fleet-operations
description: Ambient and personal weather station fleet telemetry operations, rate-limiting caps, sensor data quarantine, and stream diagnostics.
---

# IoT Fleet Operations Skill

## Purpose
Governs telemetry ingestion pipelines, sensor data stream monitoring, out-of-bounds quarantine, and high-throughput batch operations.

## Core Directives
1. Enforce batch caps (500 records/batch) to prevent memory allocation spikes.
2. Shed out-of-bounds telemetry records without failing the entire stream.
3. Track telemetry throughput and error rates via `BatchIngestionResult`.

