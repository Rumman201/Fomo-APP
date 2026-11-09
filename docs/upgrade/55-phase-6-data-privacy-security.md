# Phase 6 — Data Privacy & Local Storage Security

## 1. Privacy Impact Assessment

The Fomo application handles weather observations, city search queries, geographic coordinates, and IoT telemetry. To protect user privacy:
- The app does not request continuous fine background GPS permissions (`ACCESS_BACKGROUND_LOCATION`).
- User location coordinates and searched cities are stored locally in Room (`fomo_weather.db`) and are never exfiltrated to third-party ad networks or analytics trackers.
- No analytics SDKs or ad monetization libraries exist in the dependency manifest (`gradle/libs.versions.toml`).

---

## 2. Backup & Extraction Hardening

### 2.1 Backup Vulnerability Analysis
By default, Android's Auto Backup mechanism copies application data directories (databases, shared preferences) to Google Drive or ADB backup archives (`adb backup`). If an attacker gains temporary physical access to an unlocked device with USB debugging enabled, sensitive databases could be extracted.

### 2.2 Implemented Rules in `backup_rules.xml` & `data_extraction_rules.xml`
```xml
<!-- Exclude local Room database files containing cached locations and forecasts -->
<exclude domain="database" path="fomo_weather.db" />
<exclude domain="database" path="fomo_weather.db-journal" />
<exclude domain="database" path="fomo_weather.db-wal" />
<exclude domain="database" path="fomo_weather.db-shm" />

<!-- Exclude encrypted preferences and cryptographic metadata -->
<exclude domain="sharedpref" path="fomo_secure_prefs.xml" />
```
Both cloud backup and device-to-device migration paths are guarded, ensuring that user history is not extracted via unencrypted host backups.

