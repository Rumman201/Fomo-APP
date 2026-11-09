# Phase 6 — Security Operations & Incident Response Manual

## 1. Operational Security Procedures

### 1.1 Key Compromise & Emergency Key Rotation
If a device is reported lost, stolen, or compromised:
1. `SecureKeystoreManager.deleteKey()` is invoked during user-initiated logout or remote wipe callback.
2. AndroidKeyStore permanently purges the master key alias `fomo_master_keystore_key`.
3. Any persisted records in `fomo_secure_prefs.xml` become cryptographically unrecoverable, preventing unauthorized data exfiltration.

### 1.2 Certificate Rotation Procedure
Open-Meteo REST certificates use Let's Encrypt roots. If Let's Encrypt or ISRG updates root signing keys:
1. Retrieve new SHA-256 pin hashes:
   ```bash
   openssl s_client -connect api.open-meteo.com:443 -servername api.open-meteo.com | \
   openssl x509 -pubkey -noout | \
   openssl pkey -pubin -outform der | \
   openssl dgst -sha256 -binary | \
   openssl enc -base64
   ```
2. Add backup pin to `ApiClient.java` (`buildCertificatePinner()`) and `app/src/main/res/xml/network_security_config.xml`.
3. Release hotfix through standard Google Play distribution channel.

---

## 2. Security Observability & Logging Invariants

- **Zero Sensitive Data in Logcat**: Never log sensitive tokens, coordinates, user search queries, or cryptographic IVs to Android Logcat in release builds.
- **Controlled Error Messages**: When cryptographic decryption fails (e.g. data tampering), `SecurePreferences` returns safe default values and writes sanitized diagnostics without leaking stack traces or ciphertext details.

