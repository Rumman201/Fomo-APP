---
name: authentication-authorization-hardening
description: Authentication flows, token validation, sessions, MFA, account recovery, RBAC/ABAC, resource authorization, and permission regression tests.
---

# Authentication & Authorization Hardening Skill

## Purpose & Scope
This skill provides standards for securing identity, authentication tokens, session lifecycles, and authorization boundaries. It covers token validation, credential storage, broken object-level authorization (BOLA) prevention, and negative test verification.

## When to Activate
- Securing identity tokens, session state, and user preferences on mobile and client-server systems.
- Designing cryptographic credential vaults and key rotation mechanisms.
- Preventing authorization bypass, broken function-level access, or privilege escalation.
- Hardening Android component exports and intent filter permissions.

## Engineering Invariants
1. **Never Trust Client Claimed Identity**: Tokens and claims must be cryptographically validated before granting access.
2. **Secure Token Storage**: Sensitive tokens, cryptographic keys, and user secrets must never be stored in plain SharedPreferences or world-readable files.
3. **No Hardcoded Secrets**: Secret keys, master passwords, or signing certificates must never be committed to source code.
4. **Fail-Closed Authorization**: In case of verification failure, invalid signatures, or missing claims, the system must immediately deny access.

## Step-by-Step Procedure
1. **Flow Audit**: Trace authentication token acquisition, storage, transmission, and expiration.
2. **Vault Implementation**: Integrate Android KeyStore backed AES-256-GCM encryption for persistent credentials.
3. **Authorization Checkpoints**: Enforce resource checks at the service and repository boundary.
4. **Android Component Hardening**: Verify `android:exported` attributes, permission gates, and intent filters in `AndroidManifest.xml`.
5. **Adversarial Testing**: Execute test cases attempting unauthorized access with tampered tokens or missing credentials.

