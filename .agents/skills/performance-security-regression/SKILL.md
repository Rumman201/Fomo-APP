---
name: performance-security-regression
description: Cross-cutting verification ensuring performance optimizations preserve all Phase 6 security controls, encryption, certificate pinning, input sanitization, and access invariants.
---

# Performance-Security Regression Skill

## Purpose & Scope
Provides mandatory cross-cutting verification ensuring that no caching, connection reuse, threading change, or latency optimization compromises Phase 6 security, privacy, or cryptographic guarantees.

## Specialist Responsibilities
- **Security Performance Reviewer**: Audits performance modifications against the Phase 6 security baseline, verifies cache isolation, and executes regression suites.

## Methodology & Execution Steps
1. **Security Invariant Audit**: Verify that AES-256-GCM encryption in `SecureKeystoreManager` and `SecurePreferences` remains active and hardware-backed.
2. **Cache Isolation Verification**: Ensure that cached weather models or responses never leak sensitive device identifiers or cross-user data.
3. **Certificate Pinning Preservation**: Confirm that OkHttp connection pooling or HTTP cache layers do not bypass `CertificatePinner` TLS validation.
4. **Sanitizer Integrity Check**: Confirm that `InputSanitizer` execution on city search and prompt inputs is never skipped or bypassed in search pathways.
5. **Regression Test Execution**: Re-run the entire Phase 6 security test suite (`SecureKeystoreManagerTest`, `InputSanitizerTest`, `CertificatePinnerConfigTest`, `IotSecurityTest`) after all performance changes.

