# Phase 6 — Security Controls & Architecture Specification

## 1. Cryptographic Architecture

### 1.1 AndroidKeyStore AES-256-GCM Specification
The application employs hardware-backed cryptographic storage via `SecureKeystoreManager`:
- **Key Storage Provider**: `AndroidKeyStore` (Hardware TEE / StrongBox Keymaster where available).
- **Key Algorithm**: `AES` (Advanced Encryption Standard).
- **Key Size**: 256 bits (`KEY_SIZE_BITS = 256`).
- **Cipher Transformation**: `AES/GCM/NoPadding`.
- **Initialization Vector (IV)**: 12-byte (96-bit) cryptographically secure pseudorandom IV (`SecureRandom`). Never reused; prepended to the ciphertext.
- **Authentication Tag**: 128-bit tag (`GCM_TAG_LENGTH_BITS = 128`) guaranteeing message authenticity and integrity.
- **Envelope Format**: 
  $$\text{Payload} = [\text{IV}_{12\text{ bytes}} \parallel \text{Ciphertext} \parallel \text{Tag}_{16\text{ bytes}}]$$
- **Base64 Serialization**: Dual-environment encoding (`android.util.Base64` on device; reflective fallback on host JVM tests).

---

## 2. Secure Preferences (`SecurePreferences`)
- Encrypted wrapper over Android `SharedPreferences`.
- Serializes primitive types (`String`, `boolean`, `long`, `int`) through AES-256-GCM.
- In case of tampering or key corruption, it gracefully returns fallback defaults rather than crashing the host activity.

---

## 3. Network Transport Controls

### 3.1 OkHttp Certificate Pinning
- Pinned to Let's Encrypt / ISRG Root certificate authorities:
  - `sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=` (ISRG Root X1)
  - `sha256/jQJTbIh0grw0/1TkHSumWb+Fs0Ggogr621gT3PvPKG0=` (Let's Encrypt R3 / Backup)
  - `sha256/diuDEWzNEeCdUbal9uhqBsWM9ToPtdvgWJZ1BSRhUFA=` (ISRG Root X2)
- Domains covered: `api.open-meteo.com` and `geocoding-api.open-meteo.com`.
- Configurable toggle (`enablePinning = false`) available exclusively for local `MockWebServer` integration testing.

### 3.2 Network Security Config
- `cleartextTrafficPermitted="false"` enforced globally across all base configurations.
- Subdomain pinning specified through `2028-12-31`.

---

## 4. Input Sanitization & Boundary Defenses (`InputSanitizer`)

- **Search Query Filter**:
  - Eliminates control characters: `[\p{Cntrl}&&[^\r\n\t]]`.
  - Strips script and style blocks: `<script[^>]*>[\s\S]*?</script>`, `<style[^>]*>[\s\S]*?</style>`.
  - Strips HTML tags: `<[^>]*>`.
  - Neutralizes SQL meta-characters: `([';]|--|/\*|\*/)`.
  - Normalizes multiple whitespace sequences and bounds string length to 100 characters.
- **Geographic Coordinate Validation**:
  - Rejects non-finite numbers (`NaN`, `Infinity`).
  - Constrains latitude to $[-90.0, +90.0]$ and longitude to $[-180.0, +180.0]$.
- **Station ID Whitelist**:
  - Enforces alphanumeric pattern: `^[A-Za-z0-9_-]{3,32}$`.

---

## 5. IoT Telemetry Freshness & Anti-Replay Guardrails

- Telemetry timestamps evaluated against system clock:
  - Maximum age: 24 hours ($86,400,000 \text{ ms}$).
  - Maximum future skew: 1 hour ($3,600,000 \text{ ms}$).
- Packets failing this window are rejected with `Status.REPLAY_OR_EXPIRED_TIMESTAMP`.

