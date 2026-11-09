# 93 — Phase 8 Infrastructure as Code & Containerized Build Matrix

## Containerized Build Infrastructure

To guarantee complete environment consistency across macOS, Windows, and Linux developer environments and eliminate "works on my machine" discrepancies, build infrastructure is defined as code.

---

## 1. Container Specifications (`Dockerfile`)
- **Base OS**: `eclipse-temurin:17-jdk-jammy`
- **Android SDK Components**:
  - Android Command-Line Tools version `11076708`
  - Android SDK Platform `android-34`
  - Android SDK Build-Tools `34.0.0`
  - Platform Tools (`adb`, `fastboot`)

---

## 2. Docker Compose Orchestration (`docker-compose.yml`)
Enables one-command deterministic local builds:
```bash
docker compose up --build
```
- Mounts host project directory to `/workspace`.
- Persists Gradle package cache across runs via named volume `fomo-gradle-cache`.
- Configures headless JVM arguments (`-Dorg.gradle.daemon=false`).

