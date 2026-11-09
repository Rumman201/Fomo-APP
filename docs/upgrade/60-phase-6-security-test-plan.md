# Phase 6 — Security Verification & Test Plan

## 1. Test Architecture & Security Test Matrix

Automated verification ensures that security mechanisms function correctly across all execution paths.

| Test Suite | Class File | Focus Area | Verification Method |
|---|---|---|---|
| **Cryptographic Engine** | `SecureKeystoreManagerTest.java` | AES-256-GCM, random IVs, tag verification, tamper rejection | Automated unit test |
| **Encrypted Storage** | `SecurePreferencesTest.java` | Data encryption in storage, primitive types, tamper fallback | Automated unit test with `InMemorySharedPreferences` |
| **Input Sanitization** | `InputSanitizerTest.java` | XSS removal, SQLi strip, coordinate bounds, station IDs | Automated unit test |
| **Certificate Pinning** | `CertificatePinnerConfigTest.java` | Pin set completeness, SHA-256 formatting, client integration | Automated unit test |
| **IoT Telemetry Security** | `IotSecurityTest.java` | Replay attacks, clock skew rejection, station ID formatting | Automated unit test |

---

## 2. Test Execution Details

1. **Tamper Detection**: Mutating a single bit in the AES-GCM ciphertext or authentication tag triggers `GeneralSecurityException` / `AEADBadTagException`, proving message integrity verification works.
2. **IV Uniqueness**: Encrypting identical plaintext twice produces distinct payloads, verifying non-repeating initialization vectors.
3. **Payload Truncation**: Passing byte arrays smaller than 12 bytes immediately raises corrupt envelope exceptions.
4. **Input Boundary Enforcement**: Invalid coordinates like $(90.001, 10.0)$ or `NaN` evaluate to `false`, preventing malformed queries.
5. **Replay Window Enforcement**: Ingestion of telemetry older than 24 hours fails with `Status.REPLAY_OR_EXPIRED_TIMESTAMP`.

