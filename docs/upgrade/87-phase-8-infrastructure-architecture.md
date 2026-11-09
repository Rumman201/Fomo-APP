# 87 — Phase 8 Cloud & Deployment Architecture Overview

## Deployment Architecture Diagram

```mermaid
flowchart TD
    subgraph Development
        Dev["Developer Workstation"] -->|git push / PR| GH["GitHub Repository"]
    end

    subgraph CI_Pipeline["GitHub Actions CI Pipeline (.github/workflows/ci.yml)"]
        Lint["Android Lint Check (0 Errors Gate)"]
        Test["Unit & Regression Tests (121 Tests Gate)"]
        Build["Release Compilation (R8 Shrinking)"]
        Digest["SHA-256 Digest & SBOM Generation"]
        Lint --> Build
        Test --> Build
        Build --> Digest
    end

    subgraph Release_Pipeline["Release Pipeline (.github/workflows/release.yml)"]
        Tag["Git Tag (v*.*.*)"] --> Preflight["Pre-Flight Verifications"]
        Preflight --> Sign["APK/AAB Signing"]
        Sign --> GHRelease["GitHub Releases Distribution"]
        Sign --> PlayConsole["Google Play Console Track"]
    end

    subgraph Container_Build["Reproducible Container Build (Dockerfile)"]
        Docker["Ubuntu + JDK 17 + Android SDK 34"] --> DeterministicAPK["Deterministic APK Binary"]
    end

    GH --> CI_Pipeline
    GH --> Release_Pipeline
```

---

## Infrastructure Boundaries
1. **Source Repository**: GitHub repository enforcing branch protection on `main` requiring passing CI status checks.
2. **Build Runners**: Standard `ubuntu-latest` GitHub-hosted runners or local Docker containers (`docker-compose up`).
3. **Distribution Channels**:
   - Internal Testing Track: For automated dogfooding and QA testing.
   - Production Track: Google Play Store with staged rollouts (10% -> 25% -> 50% -> 100%).
   - Direct Distribution: Signed standalone APKs with verifiable SHA-256 checksums published on GitHub Releases.

