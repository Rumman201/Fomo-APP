# Phase 6 — STRIDE Threat Model & Risk Analysis

## 1. Methodology

The threat model evaluates the Fomo Android application using the STRIDE framework (Spoofing, Tampering, Repudiation, Information Disclosure, Denial of Service, Elevation of Privilege) combined with DREAD risk scoring (Damage, Reproducibility, Exploitability, Affected Users, Discoverability).

---

## 2. Threat Analysis Matrix

| Threat ID | STRIDE Category | Target Component | Threat Scenario | Pre-Mitigation Risk | Mitigation Implemented | Post-Mitigation Risk |
|---|---|---|---|---|---|---|
| **T-01** | Spoofing | Open-Meteo REST API | Adversary uses rogue TLS CA certificate on compromised network to intercept/forge forecasts. | High (8.0) | OkHttp `CertificatePinner` & `network_security_config.xml` SHA-256 root certificate pinning. | Low (2.0) |
| **T-02** | Tampering | IoT Telemetry | Adversary intercepts and replays expired PWS telemetry packets to skew microclimate predictions. | High (7.5) | Timestamp freshness window checking (24h past / 1h future) & alphanumeric station ID validation. | Low (1.8) |
| **T-03** | Tampering | City Search Query | Adversary injects SQL fragments or HTML tags into city search query to corrupt storage or execute script. | Medium (6.0) | `InputSanitizer` defensive regex filtering, HTML/Script stripping, and SQL meta-character removal. | Very Low (1.0) |
| **T-04** | Information Disclosure | Local Preferences | Plaintext preferences contain user location history and device identifiers, readable via ADB or rooted backup. | High (7.8) | `SecureKeystoreManager` AES-256-GCM authenticated encryption & `data_extraction_rules.xml` backup exclusion. | Low (1.5) |
| **T-05** | Information Disclosure | Local Database | Full SQLite database extracted via standard Android Cloud Backup or device-to-device transfer. | Medium (6.5) | Explicit exclusion in `backup_rules.xml` and `data_extraction_rules.xml`. | Low (1.2) |
| **T-06** | Denial of Service | City Search Input | Overly long string (100k+ chars) causes memory exhaustion or UI ANR during search debouncing. | Medium (5.5) | Strict truncation to 100 characters in `InputSanitizer`. | Very Low (1.0) |
| **T-07** | Tampering | AI Assistant Directives | Adversary uses prompt injection phrases to override weather reasoning logic. | Medium (5.8) | `InputSanitizer.sanitizePromptInput` heuristic filtering of injection directives (`ignore previous instructions`). | Low (1.4) |
| **T-08** | Reverse Engineering | Compiled APK | Attackers reverse-engineer decompiled bytecode, tampering with integrity checks or inspecting internal logic. | Medium (6.2) | ProGuard / R8 code shrinking, dead-code removal, and identifier obfuscation (`minifyReleaseWithR8`). | Low (2.2) |

---

## 3. DREAD Scoring Breakdown

$$Score = \frac{Damage + Reproducibility + Exploitability + Affected Users + Discoverability}{5}$$

- **T-01 (TLS Interception)**: $(8 + 8 + 8 + 8 + 8) / 5 = 8.0$ (High) $\rightarrow$ Post-Mitigation: $(2 + 2 + 2 + 2 + 2) / 5 = 2.0$ (Low).
- **T-04 (Preferences Extraction)**: $(8 + 8 + 7 + 8 + 8) / 5 = 7.8$ (High) $\rightarrow$ Post-Mitigation: $(2 + 1 + 1 + 2 + 2) / 5 = 1.6$ (Low).
- **T-08 (Bytecode Tampering)**: $(6 + 7 + 6 + 6 + 6) / 5 = 6.2$ (Medium) $\rightarrow$ Post-Mitigation: $(2 + 2 + 2 + 2 + 3) / 5 = 2.2$ (Low).

