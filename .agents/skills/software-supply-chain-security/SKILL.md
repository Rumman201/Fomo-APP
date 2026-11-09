---
name: software-supply-chain-security
description: Dependency assessment, lockfile integrity, secret scanning, SAST, SBOM, package provenance, and ProGuard/R8 code shrinking.
---

# Software Supply Chain Security Skill

## Purpose & Scope
This skill governs software supply chain security, dependency version management, secret scanning, ProGuard/R8 dead-code shrinking, and build provenance. It prevents compromised third-party packages, hardcoded secrets, and unminified release binaries from reaching production.

## When to Activate
- Managing dependencies via centralized version catalog (`gradle/libs.versions.toml`).
- Auditing dependencies for known CVEs and insecure transitive components.
- Configuring R8 / ProGuard rules (`proguard-rules.pro`) for release code obfuscation and resource shrinking.
- Scanning codebase for accidental API keys, tokens, or private credentials.

## Engineering Invariants
1. **Version Catalog Exclusivity**: All external libraries must be declared in `gradle/libs.versions.toml`; no raw unpinned dependencies in build scripts.
2. **Zero Committed Secrets**: Source code and configuration files must contain zero plain API keys, tokens, or private keys.
3. **R8 Obfuscation on Release**: Release builds must enable `isMinifyEnabled = true` and `isShrinkResources = true` with tested keep rules.
4. **Reproducible Builds**: Build configurations must yield deterministic build outputs.

## Step-by-Step Procedure
1. **Catalog Audit**: Inspect `libs.versions.toml` to ensure all library coordinates are pinned to stable, known-secure versions.
2. **Secret Scan**: Inspect git working tree for credentials or private tokens.
3. **ProGuard Configuration**: Author comprehensive keep rules for Retrofit, OkHttp, Gson, Room, and Material Design.
4. **Build Verification**: Execute `assembleRelease` or `assembleDebug` with minification enabled.
5. **Supply Chain Documentation**: Record dependency inventory and version hashes in Phase 6 documentation.

