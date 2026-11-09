---
name: workflow-distributed-qa
description: Event-driven automation testing, meteorological trigger validation, 60-minute cooldown suppression, idempotency, and audit logging.
---

# Workflow & Distributed Automation QA Skill

## Purpose
Validates the correctness, idempotency, and event timing of the weather alert and automation subsystem.

## Core Directives
1. Verify meteorological trigger evaluations (rain, high winds, freeze alerts).
2. Validate 60-minute suppression cooldowns preventing notification fatigue.
3. Verify immutable audit log recording in Room SQLite `automation_logs`.

