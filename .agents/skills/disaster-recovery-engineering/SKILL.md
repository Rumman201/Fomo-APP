---
name: disaster-recovery-engineering
description: SQLite corruption diagnostics, automated state restoration, ADB backup/restore tooling, and destructive migration fallbacks.
---

# Disaster Recovery Engineering Skill

## Purpose
Governs client state resilience, corrupted SQLite recovery, state extraction tooling, and emergency reset procedures.

## Core Directives
1. Provide cross-platform backup and recovery scripts (`scripts/backup_restore_db.ps1` and `scripts/backup_restore_db.sh`).
2. Implement Room fallback mechanisms preventing launch crash loops during schema corruption.
3. Validate database integrity using SQLite PRAGMA checks.

