---
name: project-intelligence-audit
description: Comprehensive codebase reconnaissance, dependency mapping, architectural inventory, and evidence-based risk assessment.
---

# Project Intelligence Audit Skill

## Purpose
Provides structured methodology for deep exploration of project repositories, understanding domain boundaries, auditing third-party dependencies, cataloging features, and producing verifiable findings.

## Activation Criteria
- Triggered during Phase 1 reconnaissance or whenever onboarding to unfamiliar submodules.
- Used when evaluating project architecture, identifying hidden technical debt, or mapping system dependencies.

## Standards & Protocol
1. **Workspace Discovery**: Identify repository structure, build manifests, configuration files, and package boundaries.
2. **Feature Inventory**: Categorize features as implemented, partially implemented, stubbed, or missing.
3. **Dependency Mapping**: Inspect version catalogs (`libs.versions.toml`), transitive dependencies, and API levels.
4. **Evidence Collection**: Every finding must be linked to exact file paths, line numbers, and tool execution logs.
5. **No Assumptions**: Distinguish verified facts from unverified assumptions.

## Deliverables
- Project inventory report with architecture diagram.
- Technology stack matrix with verified versions.
- Prioritized issue register (P0–P3).
