---
name: agentic-workflow-engineering
description: Tool definitions, permission boundaries, execution limits, state management, approval checkpoints, and failure recovery for autonomous and agentic workflows.
---

# Agentic Workflow Engineering Skill

## Purpose & Scope
This skill provides engineering guidelines for designing, bounding, and supervising tool-using agents and multi-step autonomous workflows. It ensures that agents operate within strict capability sandboxes, enforce human-in-the-loop approvals for consequential actions, and maintain auditability.

## When to Activate
- Implementing autonomous tool execution pipelines or multi-step reasoning workflows.
- Establishing tool interfaces, input/output schemas, and capability limits.
- Enforcing permission boundaries and user consent gates for destructive or external operations.
- Auditing tool invocations, tokens, and execution timeouts.

## Engineering Invariants
1. **Bounded Autonomy**: Every agentic workflow must enforce maximum step counts, execution timeouts, and strict tool whitelists.
2. **Least Privilege**: Tools must have minimal permissions necessary for their assigned task; no unrestricted system or database access.
3. **Untrusted Data Boundaries**: Tool outputs, retrieved documents, and external web responses are untrusted input. They must never acquire instruction authority over the agent.
4. **Human Approval for Consequential Actions**: Operations modifying user state, persistent storage, or sending external notifications require explicit user authorization.

## Step-by-Step Procedure
1. **Workflow & Goal Definition**: Define specific task goals, allowed tools, and exit conditions.
2. **Tool Contract Definition**: Create typed schemas and runtime validators for every tool.
3. **Execution Sandbox**: Implement step counters, timeout monitors, and circuit breakers.
4. **Approval Gate Integration**: Insert interactive confirmation prompts prior to executing stateful or external actions.
5. **Audit Logging & Telemetry**: Record execution traces, decisions, and failure reasons for post-run analysis.

