# 92 — Phase 8 Release Delivery & Version Management Workflows

## Release Lifecycle & Staged Rollout Strategy

```mermaid
sequenceDiagram
    participant Dev as Engineering Team
    participant Git as GitHub (Git Tag)
    participant CI as Release Workflow
    participant Store as Google Play Store
    participant Users as End Users (Staged)

    Dev->>Git: git tag v1.0.0 && git push origin v1.0.0
    Git->>CI: Trigger release.yml
    CI->>CI: Run Lint & 121 Unit Tests
    CI->>CI: assembleRelease + Sign + SHA-256
    CI->>Git: Publish GitHub Release (v1.0.0)
    CI->>Store: Upload AAB to Internal Testing Track
    Dev->>Store: Promote to Staged Rollout (10%)
    Store->>Users: 10% User Base Receives Update
    Note over Dev,Users: Monitor Crash-Free SLI (99.5%) for 24h
    Dev->>Store: Ramp to 25% -> 50% -> 100%
```

---

## Semantic Versioning Discipline
Version naming follows Semantic Versioning (`vMAJOR.MINOR.PATCH`):
- `MAJOR`: Fundamental UI overhaul or breaking architectural transformation.
- `MINOR`: New functional capabilities (e.g., IoT weather station support, multi-city tracking).
- `PATCH`: Bug fixes, security hardening, or performance optimizations.

In `app/build.gradle.kts`:
- `versionCode`: Monotonically increasing integer incremented with every production release.
- `versionName`: Semantic version string matching the Git tag.

