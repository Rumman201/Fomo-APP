# 100 — Phase 8 Incident Management, Escalation & Runbook System

## Runbook Directory Index

Operational incident procedures are formalized under `docs/operations/runbooks/`:

| Runbook File | Severity | Focus Area | Primary Mitigation |
| :--- | :--- | :--- | :--- |
| `app-outage.md` | P0 / P1 | App crash loops on startup | Halt Play Store rollout; trigger hotfix branch |
| `api-degradation.md` | P1 / P2 | Open-Meteo 5xx/outage | Circuit breaker fast-fail; serve L1/L2 cached weather |
| `database-corruption-recovery.md` | P1 | SQLite corruption | Database extraction, repair via CLI, or state wipe |
| `rollback-procedure.md` | P0 / P1 | Bad release deployed | Halt rollout; release incremented `versionCode` forward fix |
| `security-incident-response.md` | P0 | Key leakage or TLS pin issue | Rotate CI secrets, invalidate local Keystore, rotate cert pins |

---

## Escalation Path
- **Level 1 (Detection)**: Automated alerts from Crashlytics / Google Play Android Vitals.
- **Level 2 (Triage)**: On-call Mobile Engineer validates severity and engages relevant runbook.
- **Level 3 (Remediation)**: Release Engineer deploys hotfix or halts staged rollout.
- **Level 4 (Post-Mortem)**: Formal RCA (Root Cause Analysis) documented within 24 hours.

