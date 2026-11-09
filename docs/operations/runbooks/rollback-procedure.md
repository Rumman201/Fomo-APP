# Operational Runbook: Production Release Rollback & Safe Downgrade

**Severity**: P0 / P1  
**Target System**: Fomo Client Application Distribution  
**Distribution Channels**: Google Play Store, Internal Track, Direct APK Distribution

---

## 1. Overview
Android application package distribution differs fundamentally from server-side web deployments: **once an APK is installed on a user's device, the client OS will not automatically revert to an older `versionCode` due to Android's built-in package downgrade protection (`INSTALL_FAILED_VERSION_DOWNGRADE`)**.

Therefore, "rollback" in Android mobile engineering requires a precise, forward-rolling hotfix strategy or Google Play Console release management intervention.

---

## 2. Release Halting Procedures (Immediate Containment)

### Scenario A: Rollout in Progress (< 100%)
1. Open **Google Play Console** -> **Release** -> **Production**.
2. Select the active release and click **Halt Rollout**.
3. *Effect*: Halts distribution to all pending devices immediately. Devices that have already updated remain on the new version; devices that haven't updated remain on the prior stable version.

### Scenario B: 100% Rollout or Critical Bug Confirmed
1. When rollout cannot be halted or critical bug impacts already updated devices:
2. **Execute Fast-Forward Rollback Release**:
   - Revert the offending commit(s) on the `main` branch:
     ```bash
     git revert <bad-commit-hash>
     ```
   - Increment `versionCode` by +1 (e.g., from `10` to `11`) and update `versionName` to indicate hotfix (e.g., `1.0.1-hotfix`).
   - Run verification suite:
     ```bash
     ./gradlew testDebugUnitTest
     ./gradlew lintDebug
     ./gradlew assembleRelease
     ```
   - Commit, tag `v1.0.1-hotfix`, and push. The GitHub Actions release pipeline (`.github/workflows/release.yml`) will build and package the production artifacts.
   - Upload the new APK/Bundle to Google Play Console with expedited review request.

---

## 3. Remote Kill-Switch & Feature Flag Degradation
For non-fatal feature bugs (e.g., IoT weather station telemetry ingestion regression or AI Advisor rendering anomalies):
1. Use client runtime feature flags:
   - Disable problematic subsystem without deploying a new binary.
   - Revert UI cards to default static forecasts.
2. Invalidate remote cache and local Room cache by toggling cache version flag.

---

## 4. Verification & Validation Gate
Before deploying any rollback hotfix:
- [ ] Confirmed `versionCode` is strictly greater than the defective release.
- [ ] Tested in-place upgrade from defective version to hotfix version on real device without data loss.
- [ ] Verified database migration passes without crashes.
- [ ] Checked ProGuard mappings and obfuscation symbols match release tag.

