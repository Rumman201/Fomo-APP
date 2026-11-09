# 99 — Phase 8 Disaster Recovery, Backup & State Restoration

## Disaster Recovery Scope

Mobile disaster recovery focuses on local client state preservation, SQLite database corruption recovery, hardware Keystore loss mitigation, and secure state resetting.

---

## 1. Automated SQLite State Recovery
- **Room Migration Fallbacks**: When migrations fail catastrophically, destructive fallback recreation prevents total app crash loops.
- **WAL Journaling**: Prevents partial corruption during power loss.

---

## 2. Disaster Recovery Automation Tooling
Provided in `scripts/backup_restore_db.ps1` and `scripts/backup_restore_db.sh`:
- `backup`: Pulls current `fomo.db` via ADB to local workstation.
- `restore`: Pushes backup database back to device and restarts application.
- `check`: Runs SQLite PRAGMA integrity check.
- `clean`: Wipes corrupt application data and re-initializes fresh state.

