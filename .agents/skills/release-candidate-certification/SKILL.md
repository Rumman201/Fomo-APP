---
name: release-candidate-certification
description: Release candidate qualification, full-system verification audit, defect threshold gating, Go/No-Go assessment, and handoff.
---

# Release Candidate Certification Skill

## Purpose
Provides the final, objective release assessment, evaluating test evidence against quality gates and generating traceable verification packages.

## Core Directives
1. Bind verification evidence strictly to a specific source commit and artifact SHA-256 digest.
2. Verify all quality gates (Lint: 0 errors, Tests: 100% pass, ProGuard/R8: minified clean build).
3. Deliver an unambiguous release disposition (GO, CONDITIONAL GO, NO-GO, INSUFFICIENT EVIDENCE).

