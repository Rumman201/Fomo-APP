---
name: release-automation-engineering
description: Automated release tagging, ProGuard/R8 release packaging, SHA-256 cryptographic digest generation, and GitHub Release drafting.
---

# Release Automation Engineering Skill

## Purpose
Governs the production packaging, artifact generation, signing validation, and delivery lifecycle for Android releases.

## Core Directives
1. Automate release triggers via Git tags (`v*.*.*`).
2. Generate verifiable SHA-256 digests (`SHA256SUMS.txt`) for all released binaries.
3. Automatically assemble release notes highlighting quality gates, R8 shrinking metrics, and security controls.

