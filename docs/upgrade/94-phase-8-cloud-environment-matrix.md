# 94 — Phase 8 Multi-Environment Configuration & Secrets Isolation

## Environment Matrix

The Fomo mobile application operates across three distinct execution tiers:

| Environment | Build Variant | Keystore Signing | Upstream API Endpoint | Logging & Telemetry |
| :--- | :--- | :--- | :--- | :--- |
| **Development** | `debug` | Android Debug Keystore | Open-Meteo REST API | Verbose Logcat enabled |
| **Staging / QA** | `release` (internal) | Internal QA Keystore | Open-Meteo + Mock IoT Feed | Structured logging with debug traces |
| **Production** | `release` | Google Play Upload Keystore | Open-Meteo HTTPS (Strict TLS) | Redacted production telemetry only |

---

## Secrets Isolation Invariants
1. **Zero Source Control Secrets**: No production keystore passwords, private keys, or credentials committed to Git.
2. **GitHub Secrets Management**:
   - `KEYSTORE_BASE64`: Base64 encoded release keystore file.
   - `KEYSTORE_PASSWORD`: Keystore pass-phrase.
   - `KEY_ALIAS`: Signing key alias.
   - `KEY_PASSWORD`: Key password.
3. **Hardware Storage on Device**:
   - Master encryption key generated inside Android KeyStore hardware security module (HSM) on device, never transmitted over network.

