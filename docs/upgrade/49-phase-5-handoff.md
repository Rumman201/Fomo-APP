# 49 — Phase 5 Completion & Phase 6 Handoff Specification

## 1. Confirmed Engineering State

Phase 5 has successfully delivered advanced functional excellence across the Fomo application:
- **Multi-Location Search**: Operational Open-Meteo Geocoding REST client, `CitySearchBottomSheetDialog`, `CityAdapter`, and Room `saved_cities` table.
- **Atmospheric Intelligence**: Edge computing service calculating Steadman Heat Index, Wind Chill, attire advice, and activity suitability scores.
- **Production AI Advisor**: Structured briefing engine with schema validation, injection defense, hallucination filters, and deterministic fallback.
- **Intelligent Automation**: Event-driven alert engine with cooldown deduplication and Room `automation_logs`.
- **IoT Weather Station Contract**: Standardized PWS telemetry schema and sensor boundary validator.
- **Database Architecture**: Room Version 2 with deterministic migration `MIGRATION_1_2`.
- **Quality Gates**:
  - 74 automated unit tests (**100% pass rate**).
  - 0 Android Lint fatal errors.
  - Debug APK built successfully (`app-debug.apk`, 7.93 MB).

---

## 2. Phase 6 Objectives: Security Hardening & Zero-Trust Architecture

Phase 6 will focus on comprehensive enterprise security hardening:
1. **Network Security & Certificate Pinning**:
   - Enforce certificate pinning against `api.open-meteo.com` and `geocoding-api.open-meteo.com` in `network_security_config.xml`.
   - Prevent man-in-the-middle (MITM) tampering and proxy eavesdropping.
2. **Encrypted Storage & Keystore Integration**:
   - Evaluate encrypting Room database with SQLCipher or AndroidX EncryptedSharedPreferences for sensitive user preferences.
3. **Android 14/15 Permission Hardening**:
   - Strict runtime permission gating, foreground service boundaries, and intent filter restrictions.
4. **ProGuard / R8 Shrinking & Code Obfuscation**:
   - Enable R8 shrinking, resource shrinking, and full-mode optimization in `proguard-rules.pro`.
5. **AI Safety & Attack Surface Audits**:
   - Deep analysis of untrusted third-party payload deserialization and memory boundary checks.

---

## 3. Invariants for Phase 6 Engineers & Agents

- **Preserve Working Features**: Do not break the 74 passing unit tests, multi-city search, or AI atmospheric briefing.
- **No Regressions**: Maintain 0 Android Lint errors and clean `assembleRelease` / `assembleDebug` builds.
- **Documentation Alignment**: Record all security decision records under `docs/upgrade/`.

