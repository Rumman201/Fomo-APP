# Operational Runbook: Security Incident Response & Key Revocation

**Severity**: P0  
**Target System**: Fomo Client Security, Cryptographic Storage, API Authentication  
**Regulatory / Standard Compliance**: OWASP Mobile Top 10, Zero-Trust Architecture

---

## 1. Overview
This runbook governs procedures for security engineers and DevOps teams responding to confirmed or suspected security incidents, including API key leakage, TLS certificate revocation, cryptographic key corruption, and tampering attempts.

---

## 2. Threat Scenarios & Response Procedures

### Scenario 1: External API Key Exposure or Compromise
*Detection*: Secret scanner alert (e.g., GitHub Secret Scanning, GitGuardian) flagging committed credentials or third-party API keys.
*Action*:
1. **Immediate Revocation**: Log in to provider console (e.g., Open-Meteo commercial portal or reverse proxy gateway) and rotate API key immediately.
2. **Rotate Secrets in CI/CD**:
   - Update GitHub Repository Secrets (`SETTINGS_ENCRYPTION_KEY`, `RELEASE_KEYSTORE`).
   - Flush GitHub Actions cache to eliminate leaked artifacts.
3. **Client Configuration Update**:
   - Push updated secret via secure remote config or issue expedited hotfix release.

---

### Scenario 2: TLS Certificate Compromise or Pinning Rotation
*Detection*: Security advisory regarding compromised root CA, expired leaf certificate, or scheduled key rotation for `api.open-meteo.com`.
*Action*:
1. Inspect `CertificatePinnerConfig.java`:
   - Verify backup pins are already defined in the pin set.
2. If new public key pin must be added:
   - Extract SHA-256 fingerprint from new TLS certificate:
     ```bash
     openssl s_client -connect api.open-meteo.com:443 -servername api.open-meteo.com </dev/null 2>/dev/null | \
     openssl x509 -pubkey -noout | \
     openssl pkey -pubin -outform der | \
     openssl dgst -sha256 -binary | \
     openssl enc -base64
     ```
   - Update pin in `CertificatePinnerConfig.java`.
   - Run `CertificatePinnerConfigTest.java` to verify pin format validity.

---

### Scenario 3: Device-Level Keystore Compromise or Root Detection
*Detection*: Hardware Keystore tampering or unauthorized key extraction attempt on rooted/jailbroken devices.
*Action*:
1. `SecureKeystoreManager` automatically wipes encryption keys upon detecting hardware integrity failure.
2. Invalidate `SecurePreferences` entries containing sensitive tokens.
3. Force re-authentication / re-consent for location data and sensitive stored preferences.

---

## 3. Post-Incident Forensic Documentation
- [ ] Document exact incident timeline, source IP/commit, and affected scope.
- [ ] Verify all exposed secrets were rotated and old secrets destroyed.
- [ ] Add regression test in `SecurityPerformanceRegressionTest.java` to prevent recurrence.
- [ ] Update `docs/upgrade/07-risk-register.md` and file security advisory in `docs/security/advisories/`.

