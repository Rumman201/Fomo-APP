# 95 — Phase 8 Container & Orchestration Deployment Framework

## Containerized Build Harness Execution

The container harness serves as an isolated, repeatable execution environment for developers and automated CI agents.

---

## 1. Local Container Execution Workflow
To build, lint, and test within the reproducible container:
```bash
# 1. Build the container image
docker compose build

# 2. Run lint and unit tests
docker compose run --rm android-builder ./gradlew lintDebug testDebugUnitTest

# 3. Assemble release APK
docker compose run --rm android-builder ./gradlew assembleRelease
```

---

## 2. Resource Allocation Limits
In production CI and container execution:
- CPU Limit: 4 Cores.
- Memory Limit: 4096 MB RAM.
- Heap Max: 3072 MB (`-Xmx3072m`).
- Metaspace Max: 512 MB (`-XX:MaxMetaspaceSize=512m`).
- Prevents out-of-memory (OOM) killer terminations on constrained CI nodes.

