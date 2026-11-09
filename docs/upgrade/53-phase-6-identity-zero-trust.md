# Phase 6 — Identity Hardening & Zero-Trust Architecture

## 1. Zero-Trust Philosophy on Client Devices

In a mobile client-server architecture, zero-trust requires that:
1. **The Client Runtime is Untrusted**: Android devices may be rooted, modified, or running modified kernels. No client-side secret can be treated as permanently inaccessible to a rooted user.
2. **The Network is Hostile**: Every packet may traverse an untrusted Wi-Fi hotspot with rogue certificate authorities installed in the user trust store.
3. **External Sensor Input is Untrusted**: IoT weather stations and local beacons may be simulated or spoofed.

---

## 2. Zero-Trust Implementation Pillars

### 2.1 Hardware-Rooted Identity & Keystore Isolation
- Master key generation is delegated to `AndroidKeyStore`. The key material never enters user-space memory as raw bytes; encryption and decryption operations occur inside the hardware security module (Secure Element / TEE).
- When an application wipe or account reset is initiated, `deleteKey()` removes the alias from the KeyStore, rendering any previously persisted encrypted data permanently unrecoverable.

### 2.2 Least-Privilege Network Boundaries
- The application only trusts the system CA store (`<certificates src="system" />`). User-installed certificates (e.g. from inspection proxies like Charles or mitmproxy) are rejected by default.
- Certificate pinning explicitly verifies the leaf or root public key hash, defeating even rogue enterprise root CAs.

### 2.3 Boundary Verification on Local Cache Access
- Database queries do not trust raw UI inputs; parameters are bound using Room's parameterized SQLite statements, preventing SQL injection.
- Geocoding responses are mapped through immutable domain models (`City`, `CurrentWeather`, `DailyForecast`) with strict null-checking before presentation.

