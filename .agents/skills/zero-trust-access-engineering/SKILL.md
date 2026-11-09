---
name: zero-trust-access-engineering
description: Identity-based access, service identity, least privilege, trust-boundary analysis, resource authorization, policy enforcement, and access testing.
---

# Zero-Trust Access Engineering Skill

## Purpose & Scope
This skill provides architectural guidance and engineering patterns for implementing Zero-Trust principles: "Never Trust, Always Verify". It governs explicit verification of identity, resource-level authorization, least privilege, cryptographic boundaries, and continuous policy enforcement.

## When to Activate
- Transitioning from perimeter-based security to resource-oriented explicit verification.
- Enforcing least privilege across client applications, local databases, background workers, and AI services.
- Designing cryptographic key storage and data boundaries on client devices.
- Authoring negative authorization tests ensuring unauthorized access attempts fail safely.

## Engineering Invariants
1. **Explicit Resource Verification**: A valid session or app execution context does not grant blanket access; every resource query must validate ownership and boundary constraints.
2. **Least Privilege**: Services, components, and tools receive only the minimal permissions required for their specific function.
3. **Hardware-Rooted Trust**: Where available, cryptographic keys must be anchored in hardware-backed KeyStore (TEE / StrongBox) with AES-256-GCM.
4. **Deterministic Enforcement**: Access control must be enforced by deterministic code, never delegated to probabilistic models.

## Step-by-Step Procedure
1. **Boundary & Asset Identification**: Identify all sensitive data stores, preferences, and API communication channels.
2. **Policy Definition**: Formulate permission matrices (Subject × Resource × Action → Allow/Deny).
3. **Cryptographic Protection**: Implement authenticated encryption (AES-GCM) with hardware key generation.
4. **Enforcement Implementation**: Wire policy checks and cryptographic wrappers into data repositories and services.
5. **Negative Test Verification**: Author unit tests validating that missing keys, forged tokens, or unauthorized queries are strictly rejected.

