---
name: security-hardening
description: Android security posture review, manifest permission audits, network security configurations, cryptographic storage, and vulnerability remediation.
---

# Android Security Hardening Skill

## Purpose
Enforces threat modeling, OWASP Mobile Top 10 compliance, secure data storage, least-privilege permission management, and secure communication protocols.

## Activation Criteria
- Triggered when configuring AndroidManifest.xml, network requests, local caching, IPC components, or API authentication keys.
- Used when auditing app permissions, exported components, or third-party SDK security advisories.

## Security Controls
1. **Component Export Safety**:
   - Explicitly declare `android:exported="true|false"` for all Activities, Services, Receivers, and Providers.
   - Restrict exported components unless explicitly intended as entry points (e.g. `android.intent.action.MAIN`).
2. **Permission Least Privilege**:
   - Only request permissions strictly necessary for app functionality.
   - For Android 13+ (API 33+), properly guard runtime permissions (`POST_NOTIFICATIONS`, fine location).
3. **Network Security Configuration**:
   - Enforce HTTPS across all network endpoints (`cleartextTrafficPermitted="false"`).
   - Use certificate pinning for critical backend endpoints where appropriate.
4. **Secrets & Storage Protection**:
   - Never commit API keys, signing keystores, or private tokens into git.
   - Use `EncryptedSharedPreferences` or Android Keystore for sensitive tokens.
5. **Backup Security**:
   - Explicitly define `backup_rules.xml` and `data_extraction_rules.xml` to exclude sensitive tokens, credentials, or private caches.
