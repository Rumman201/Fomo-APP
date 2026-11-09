---
name: technical-documentation
description: Architecture documentation, Architecture Decision Records (ADRs), developer onboarding, API documentation, and phase upgrade tracking.
---

# Technical Documentation Skill

## Purpose
Ensures that all architectural decisions, code conventions, upgrade paths, and development workflows are comprehensively documented and kept synchronized with code implementation.

## Activation Criteria
- Triggered when introducing structural changes, deciding on architectural patterns, or concluding engineering phases.
- Used when maintaining `docs/upgrade/` documents, creating ADRs, or writing developer onboarding guides.

## Documentation Structure
1. **Root Readme (`README.md`)**:
   - Project overview, prerequisites, environment setup, build instructions, and testing commands.
2. **Architecture Decision Records (`docs/upgrade/08-architecture-decisions.md`)**:
   - Document Context, Decision, Consequences, and Alternatives considered for every major architectural choice.
3. **Phase Tracking (`docs/upgrade/`)**:
   - Maintain the 10-phase transformation roadmap and execution logs with verifiable evidence.
4. **Code-Level Documentation**:
   - Meaningful docstrings/Javadoc on public APIs, interfaces, models, and utility classes.
