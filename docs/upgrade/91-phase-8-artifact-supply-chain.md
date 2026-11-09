# 91 — Phase 8 Artifact Production, Provenance & Supply Chain

## Supply Chain Integrity Framework

Phase 8 secures the software supply chain against malicious dependency tampering, unauthorized binary modification, and unverified releases.

---

## 1. Cryptographic Artifact Digest Verification
Every release build automatically calculates SHA-256 digests for all generated APK and AAB artifacts:
```bash
sha256sum app-release.apk > checksums.sha256
```
Consumers can independently verify artifact authenticity before installation:
```bash
sha256sum -c checksums.sha256
```

---

## 2. R8 Minification & Code Shrinking
In `app/build.gradle.kts`:
- `isMinifyEnabled = true`
- `isShrinkResources = true`
- `proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")`
- Strips unused classes, methods, and XML attributes.
- Obfuscates internal class names and symbol tables, raising the bar against reverse engineering.

---

## 3. Dependency Provenance
- Exclusive use of trusted repositories: `google()` and `mavenCentral()`.
- HTTPS transport strictly enforced for all artifact resolution URLs.
- Version catalog declarations prevent arbitrary transitively injected scripts.

