---
name: cicd-pipeline-engineering
description: GitHub Actions Android workflow design, job matrix parallelization, Gradle intelligent caching, and static analysis quality gates.
---

# CI/CD Pipeline Engineering Skill

## Purpose
Governs the design, implementation, and optimization of automated Continuous Integration and Continuous Delivery (CI/CD) pipelines for Android projects.

## Core Directives
1. Enforce strict gatekeeping: Pull requests must not merge without clean Lint (`0 errors`) and 100% passing unit test suites.
2. Optimize build velocity through `gradle/actions/setup-gradle` caching.
3. Decouple pipeline stages into independent jobs (`lint`, `test`, `build`) to provide fast feedback to developers.

