---
name: devops-security-supply-chain
description: Software supply chain security, dependency provenance, ProGuard/R8 code obfuscation, and cryptographic artifact signing.
---

# DevOps Security & Supply Chain Skill

## Purpose
Guarantees supply chain integrity, eliminates build drift, and enforces cryptographic verification for all released software packages.

## Core Directives
1. Strictly pin dependencies in `gradle/libs.versions.toml` with zero dynamic ranges.
2. Generate and verify SHA-256 digests (`SHA256SUMS.txt`) for all release artifacts.
3. Enforce R8 minification, code obfuscation, and dead-code elimination in release builds.

