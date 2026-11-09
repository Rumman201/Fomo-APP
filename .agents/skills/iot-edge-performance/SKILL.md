---
name: iot-edge-performance
description: IoT weather station telemetry batching, high-throughput micro-ingestion, sensor data sliding windows, and memory allocation optimization.
---

# IoT Edge Performance Skill

## Purpose & Scope
Governs the optimization of high-frequency telemetry ingestion pipelines, sensor observation batching, memory allocation minimization, and low-latency validation for Personal Weather Stations (PWS).

## Specialist Responsibilities
- **IoT & Edge Performance Engineer**: Implements batch ingestion contracts, reduces GC allocation pressure, and models high-throughput sensor telemetry.

## Methodology & Execution Steps
1. **Micro-Batch Ingestion**: Provide bulk ingestion interfaces (`ingestBatch(List<IotStationTelemetryDto>)`) to amortize lock overhead and validation loops.
2. **Buffer Pooling**: Minimize object allocations during deserialization by reusing transient telemetry structures where appropriate.
3. **Sliding Window Downsampling**: Aggregate high-frequency raw sensor pulses into time-windowed averages (e.g. 5-minute averages) to bound database storage growth.
4. **Validation Pre-Filtering**: Perform fast rejection of corrupt or out-of-bounds telemetry packets before invoking complex domain logic.
5. **Simulated High-Throughput Benchmarks**: Test ingestion throughput under bursts of 1,000+ simulated telemetry events per second.

