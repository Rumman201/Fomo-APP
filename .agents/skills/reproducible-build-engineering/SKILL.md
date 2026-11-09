---
name: reproducible-build-engineering
description: Containerized Android SDK build environments, Dockerfile specifications, docker-compose orchestration, and deterministic dependency pinning.
---

# Reproducible Build Engineering Skill

## Purpose
Guarantees bit-for-bit build determinism across heterogeneous operating systems (Windows, macOS, Linux) and continuous integration runners.

## Core Directives
1. Containerize Android SDK 34 and JDK 17 using standardized Ubuntu Jammy base images.
2. Pin all dependencies strictly in `gradle/libs.versions.toml`.
3. Provide one-command local build execution via `docker compose up`.

