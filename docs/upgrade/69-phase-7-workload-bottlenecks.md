# 69 — Phase 7 Workload Models & Bottleneck Analysis

## Workload Profiles

### 1. Daily Active Forecast Workload
- **User Activity**: App open on morning alarm, location check, 7-day outlook review, attire advice consultation.
- **Access Pattern**: Burst read on cold start (1 location query, 1 current weather query, 24 hourly points, 7 daily points).
- **Bottleneck Identified**: Uncached network roundtrip (~600-1200ms) blocking UI rendering.
- **Resolution**: Multi-tier cache returns cached forecast instantly (< 5ms) while background executor fetches fresh delta.

### 2. Multi-City Switching Workload
- **User Activity**: Switching rapidly between 5 saved cities in horizontal pager or dropdown.
- **Access Pattern**: Consecutive Room database queries and image icon loading passes.
- **Bottleneck Identified**: SQLite full-table scans and redundant Bitmap allocations in RecyclerView.
- **Resolution**: Added B-Tree composite indices (`isFavorite`, `selectedAt`) and configured Glide thumbnail dimensions to `override(64, 64)`.

### 3. Continuous IoT Weather Station Ingestion Workload
- **User Activity**: Ambient weather station streaming barometric, temperature, and humidity sensors at 10Hz.
- **Access Pattern**: High-frequency telemetry events.
- **Bottleneck Identified**: Individual database inserts and atomic validations causing UI thread lockups.
- **Resolution**: `IotWeatherStationService.ingestBatch(List<IotStationTelemetryDto>)` grouping points into single transaction with bounded validation.

