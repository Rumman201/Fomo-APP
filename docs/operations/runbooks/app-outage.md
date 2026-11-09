# Operational Runbook: App Outage & Startup Failure Incident Response

**Severity**: P0 / P1  
**Target System**: Fomo Android Application  
**Component**: Application Startup, Dependency Initialization, Application Lifecycle

---

## 1. Overview
This runbook provides actionable procedures for engineering teams responding to catastrophic client startup failures, crash loops on launch, or fatal unhandled exceptions preventing users from opening the Fomo application.

---

## 2. Detection & Alerting Indicators
- **Crash-Free Session SLI**: Drops below 99.5% over a 15-minute sliding window.
- **Google Play Console / Firebase Crashlytics Alert**: Spike in `FATAL EXCEPTION: main` during `Application.onCreate` or `MainActivity.onCreate`.
- **User Telemetry**: Immediate drop in active session pings while push notifications or network requests fail.

---

## 3. Triage & Diagnostic Steps

### Step 1: Identify the Root Cause Category
1. Inspect the stack trace top-of-stack in Crashlytics or `adb logcat -b crash`:
   - **Database Migration Failure**: `IllegalStateException: A migration from X to Y was required but not found.`
   - **Hardware KeyStore Access Failure**: `KeyStoreException: KeyStore operation failed or Keystore was reinitialized.`
   - **Resource Inflation Error**: `InflateException: Binary XML file line #XX: Error inflating class.`
   - **Null Pointer / Dependency Injection Failure**: Null reference in repository or ViewModel constructor.

### Step 2: Reproduce Locally via ADB
```bash
# Capture fresh crash logs on physical test device
adb logcat -c
adb shell am start -n com.example.fomo/.ui.main.MainActivity
adb logcat -d -s AndroidRuntime:E FomoSecurity:E FomoApp:E > startup_crash.log
```

---

## 4. Mitigation & Immediate Containment

### A. Database Migration Issue
If Room migration fails after a version bump:
1. Identify whether users upgraded from Version 1, 2, or 3.
2. If schema is corrupted on device, trigger safe database reset fallback via `FomoDatabase.java`:
   - Enforce `fallbackToDestructiveMigrationOnDowngrade()`.
   - Never wipe user favorites unless migration proves mathematically impossible.

### B. Keystore Unlocking Issue
If Android Keystore master key is corrupted due to OS update or lock screen disablement:
1. `SecureKeystoreManager` must catch `KeyStoreException` and automatically invalidate cached credentials and generate a fresh AES-GCM keypair.
2. Invalidate corrupted encrypted SharedPreferences and repopulate defaults safely.

### C. Staged Rollout Halting (Play Console)
If the crash occurs in a newly deployed production release:
1. Navigate to **Google Play Console** -> **Release Overview** -> **Fomo Production**.
2. Click **Halt Rollout** immediately to prevent further user device downloads.
3. The existing installed base remains on the previous stable release.

---

## 5. Escalation & Verification Checklist
- [ ] Rollout halted on Google Play Console.
- [ ] Hotfix branch created from tagged release commit (`hotfix/vX.Y.Z`).
- [ ] Unit test written reproducing the exact crash scenario.
- [ ] `./gradlew testDebugUnitTest` and `./gradlew lintDebug` pass with 0 errors.
- [ ] Signed hotfix APK generated and verified on physical hardware (Android 7.0 - Android 14).
- [ ] Post-mortem documentation filed within 24 hours in `docs/operations/incidents/`.

