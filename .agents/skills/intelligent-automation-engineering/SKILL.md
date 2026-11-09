---
name: intelligent-automation-engineering
description: Event-driven automation, meteorological trigger systems, workflow state machines, retry policies, notification cooldowns, and idempotency for weather intelligence.
---

# Intelligent Automation Engineering Skill

## Purpose & Scope
This skill governs the architecture, state management, and operational reliability of event-driven and background automations. It covers trigger definitions, condition evaluation engines, execution state transitions, idempotency, retry mechanisms, and notification delivery controls.

## When to Activate
- Designing automated alerting for severe weather, freezing temperatures, or high precipitation risk.
- Managing background evaluation workflows and scheduled checks.
- Enforcing notification cooldowns, duplicate suppression, and audit trails.
- Implementing state machines (`DRAFT`, `ENABLED`, `TRIGGERED`, `EXECUTED`, `FAILED`, `MUTED`).

## Engineering Invariants
1. **Deterministic Trigger Logic**: Alert criteria must rely on validated, reproducible meteorological thresholds.
2. **Idempotency & Duplicate Suppression**: Automations must never deliver redundant alerts within configurable cooldown windows.
3. **Execution Safety**: Long-running background operations must never run on the main UI thread and must respect OS battery and network constraints.
4. **Auditability**: All triggered automations and actions must be logged locally with timestamps, condition values, and severity levels.

## Step-by-Step Procedure
1. **Trigger Definition**: Define event sources (forecast sync, threshold breaches, temperature shifts).
2. **State Machine Modeling**: Define workflow states, transitions, and persistable lifecycle representations.
3. **Condition Evaluator**: Implement rule evaluators checking current forecast against trigger parameters.
4. **Action Execution & Deduplication**: Dispatch actions (in-app alert banner, system notification) with cooldown enforcement.
5. **Audit Logging**: Persist execution records in Room database for user inspection and troubleshooting.

