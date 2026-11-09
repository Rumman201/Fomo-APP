# 70 — Phase 7 Performance Objectives & SLOs

## Key Performance Objectives (SLOs)

1. **SLO-PERF-01 (Startup Latency)**:
   - 95% of cold starts must render the initial weather screen in under 800 ms.
   - 99% of warm/hot starts must render in under 200 ms.

2. **SLO-PERF-02 (UI Frame Rendering)**:
   - 99% of UI frames must render within 16.6 ms (60 FPS) on Android API 24+ devices.
   - 0 frozen frames (> 700 ms) during RecyclerView scroll.

3. **SLO-PERF-03 (Cache Hit Ratio)**:
   - Cache hit ratio for repeated city queries within TTL (30 min) must exceed 85%.

4. **SLO-PERF-04 (Network Fault Fast-Fail)**:
   - When upstream API is down, circuit breaker must fast-fail within 1 ms without opening socket connections or keeping threads alive.

5. **SLO-PERF-05 (Memory Leaks & Heap Limits)**:
   - Zero memory leaks detected by LeakCanary or heap dump analysis.
   - Total heap consumption under normal operation must not exceed 64 MB.

