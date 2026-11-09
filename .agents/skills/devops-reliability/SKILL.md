---
name: devops-reliability
description: Gradle build configuration, version catalog management, ProGuard/R8 shrinking, CI/CD pipelines, and release engineering.
---

# DevOps & SRE Reliability Skill

## Purpose
Governs build reproducibility, dependency versioning via TOML version catalogs, code shrinking, automated CI/CD pipelines, and release signing infrastructure.

## Activation Criteria
- Triggered when configuring Gradle files (`build.gradle.kts`, `settings.gradle.kts`, `gradle/libs.versions.toml`), ProGuard rules, or CI workflows.
- Used when fixing build failures, optimizing build caching, or preparing production APK/AAB release bundles.

## Standards & Best Practices
1. **Centralized Dependencies**:
   - Manage all library coordinates and versions strictly within `gradle/libs.versions.toml`.
   - Never hardcode library strings in module `build.gradle.kts` files (`UseTomlInstead`).
2. **Build Caching & Parallel Execution**:
   - Enable `org.gradle.parallel=true` and `org.gradle.caching=true` in `gradle.properties`.
3. **Shrinking & Obfuscation**:
   - Maintain `proguard-rules.pro` with explicit keep rules for serialized data classes and reflection-based libraries.
   - Verify `isMinifyEnabled = true` on release builds produces valid, functioning APKs without missing class runtime crashes.
4. **CI/CD Integration**:
   - Automate linting, unit tests, and assemble tasks on pull requests and main branch commits via GitHub Actions.
