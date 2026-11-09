# 89 — Phase 8 Build Determinism, Reproducibility & Caching

## Deterministic Build Principles

To ensure that identical source code produces bit-for-bit identical or structurally equivalent binaries regardless of host OS or build runner:

---

## 1. Environment Standardization (Containerization)
- **Base OS**: Ubuntu 22.04 LTS (Jammy Jellyfish).
- **Java Runtime**: Eclipse Temurin OpenJDK 17.0.10+7.
- **Android SDK Tools**: Command-line tools package `11076708_latest`.
- **Compile SDK**: API 34 (Android 14).
- **Build Tools**: Version `34.0.0`.
- Encapsulated in root `Dockerfile` and `docker-compose.yml`.

---

## 2. Dependency Pinning & Locking
- All dependencies declared strictly with semantic versions in `gradle/libs.versions.toml`.
- Dynamic version ranges (e.g., `+` or `2.+`) are strictly prohibited by invariant rules.
- Reproducible Gradle resolution across developer machines and cloud runners.

---

## 3. Build Cache & Daemon Discipline
- In `gradle.properties`:
  ```properties
  org.gradle.parallel=true
  org.gradle.caching=true
  org.gradle.jvmargs=-Xmx3072m -XX:MaxMetaspaceSize=512m
  ```
- CI environment overrides daemon behavior (`-Dorg.gradle.daemon=false`) to prevent memory leaks and zombie processes across build runs.

