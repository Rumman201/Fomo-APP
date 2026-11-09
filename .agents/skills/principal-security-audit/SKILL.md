---
name: principal-security-audit
description: Security inventory, attack-surface discovery, threat modeling, vulnerability validation, evidence collection, severity assessment, and remediation prioritization.
---

# Principal Security Audit Skill

## Purpose & Scope
This skill provides standards, procedures, and methodologies for auditing an application's comprehensive attack surface, establishing an evidence-based threat model, prioritizing verified vulnerabilities, and directing remediation engineering without disrupting legitimate business functionality.

## When to Activate
- Performing deep security assessments across mobile, backend, database, and integration boundaries.
- Mapping trust boundaries, assets, entry points, and data flow diagrams (STRIDE).
- Triage of static analysis findings (SAST), software composition analysis (SCA), and dynamic test results.
- Formulating vulnerability registers and verifying root causes before code changes.

## Engineering Invariants
1. **Evidence-Based Severity**: Never label scanner output as a confirmed vulnerability without manual reachability and exploitability verification.
2. **Authorized Scope Only**: Never conduct attacks or penetration tests against third-party endpoints or production infrastructure without explicit authorization.
3. **No Fabricated Security Claims**: Do not claim zero vulnerabilities, absolute immunity, or regulatory compliance without formal independent scope.
4. **Preserve System Functionality**: Security controls must integrate cleanly into established architectures without breaking existing user features.

## Step-by-Step Procedure
1. **Asset & Boundary Reconnaissance**: Map all protected assets, entry points, and trust boundaries.
2. **Threat Modeling (STRIDE)**: Identify spoofing, tampering, repudiation, information disclosure, DoS, and elevation of privilege vectors.
3. **Vulnerability Verification**: Inspect code, configuration, and dependencies; replicate findings in isolated test environments.
4. **Remediation Specification**: Design root-cause architectural fixes rather than superficial string suppressions.
5. **Retest & Regression Hardening**: Verify remediation through automated negative tests and update residual risk documentation.

