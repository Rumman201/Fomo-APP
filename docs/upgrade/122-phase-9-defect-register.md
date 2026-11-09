# 122 — Phase 9 Defect Register & Root-Cause Analysis

## Confirmed Defects Identified & Remediated in Phase 9

| Defect ID | Severity | Affected Subsystem | Root Cause | Impact | Fix Implemented | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **DEF-QA-01** | **High** | `IotWeatherStationService.java` | Missing `Double.isNaN()` and `Double.isInfinite()` guards. In Java, `Double.NaN < MIN` and `Double.NaN > MAX` evaluate to `false`. | Corrupted or adversarial sensor streams with NaN or Infinite values bypassed boundary validation. | Added explicit `Double.isNaN()` and `Double.isInfinite()` checks for temperature, humidity, pressure, and wind speed. | **FIXED & VERIFIED** |
| **DEF-QA-02** | **Medium** | `AtmosphericIntelligenceService.java` | Using default system locale (`condition.toLowerCase()`) without `Locale.ROOT`. | On Turkish/Azerbaijani locale devices, `'I'.toLowerCase()` becomes `'ı'`, causing `"RAIN"` not to match `"rain"`. | Replaced all `toLowerCase()` calls with `toLowerCase(Locale.ROOT)`. | **FIXED & VERIFIED** |
| **DEF-QA-03** | **Medium** | `WeatherAutomationEngine.java` | Using default system locale (`current.getCondition().toLowerCase()`). | Same locale bug as DEF-QA-02; severe storm/rain triggers would fail on non-English locales. | Replaced with `toLowerCase(Locale.ROOT)`. | **FIXED & VERIFIED** |
| **DEF-QA-04** | **Medium** | `AtmosphericIntelligenceService.java` | `parsePercentage` did not clamp parsed double values to physical `[0.0, 100.0]` range. | Malformed or adversarial inputs could pass percentages > 100% or < 0%, skewing activity scoring algorithms. | Added `Math.max(0.0, Math.min(100.0, val))` clamping. | **FIXED & VERIFIED** |
| **DEF-QA-05** | **Medium** | `AtmosphericIntelligenceService.java` | `parseWindSpeed` did not clamp parsed wind speed values to physical `[0.0, 300.0]` km/h range. | Negative or infinite wind speeds could corrupt thermal comfort calculations. | Added `Math.max(0.0, Math.min(300.0, val))` clamping. | **FIXED & VERIFIED** |

