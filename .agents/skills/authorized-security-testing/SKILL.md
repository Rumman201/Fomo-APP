---
name: authorized-security-testing
description: Authorization and scope, non-destructive testing, scanner safety, vulnerability reproduction, evidence minimization, and retesting.
---

# Authorized Security Testing Skill

## Purpose & Scope
This skill provides standards for conducting carefully scoped, authorized security testing on local and staging systems. It ensures that security verification is deterministic, evidence-backed, and non-destructive.

## When to Activate
- Authoring unit and integration tests verifying security controls and boundary defenses.
- Executing negative test cases (SQL injection payloads, prompt injection strings, out-of-bounds telemetry).
- Validating that unauthorized actions, tampered tokens, or corrupt payloads fail safely.
- Documenting verifiable proof of vulnerability mitigation.

## Engineering Invariants
1. **Authorized Scope Only**: Execute tests exclusively against local processes, in-memory databases, and authorized mock endpoints.
2. **Deterministic & Non-Destructive**: Tests must produce repeatable outcomes without corrupting production environments or leaking real user data.
3. **Evidence Minimization**: Never commit live secrets or sensitive dumps into repository test artifacts.
4. **Verified Retesting**: A vulnerability is only marked resolved when a retest confirms the flaw is eliminated.

## Step-by-Step Procedure
1. **Test Plan Formulation**: Define exact scope, test fixtures, synthetic payloads, and success criteria.
2. **Adversarial Test Authoring**: Write JUnit 4 tests in `app/src/test/java` simulating threat vectors.
3. **Execution & Evidence Recording**: Run `./gradlew testDebugUnitTest` and record exact command outputs.
4. **Remediation Verification**: Re-run tests after code changes to confirm elimination of the vulnerability.
5. **Quality Gate Compliance**: Ensure Android Lint passes with 0 fatal errors.

