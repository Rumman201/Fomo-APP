---
name: security-observability-response
description: Security event logging, detection rules, alert design, sensitive log redaction, incident response procedures, and security regression monitoring.
---

# Security Observability & Response Skill

## Purpose & Scope
This skill provides engineering guidelines for capturing security events, designing actionable detection alerts, preventing credential leakage in system logs, and structuring incident response procedures.

## When to Activate
- Auditing logging interceptors and log outputs to ensure sensitive data (tokens, GPS, PII) is redacted.
- Designing security audit logs for automated triggers and alert history in SQLite.
- Establishing diagnostic error handling that prevents stack trace leakage to end users.
- Formulating incident response protocols for leaked credentials or compromised endpoints.

## Engineering Invariants
1. **Zero Secret Leakage in Logs**: Android Logcat, HTTP logging interceptors, and error banners must never print secret tokens, passwords, or precise GPS coordinates.
2. **Generic User-Facing Error Messages**: User-facing dialogs must display generic error descriptions while recording detailed diagnostics only in private, secure logs.
3. **Audit Trail Integrity**: Automated meteorological alerts and critical state changes must be recorded locally with timestamps and severity classifications.
4. **Immediate Incident Response**: Compromised credentials or broken pins must trigger rapid key rotation and certificate pin updates without disrupting core offline caching.

## Step-by-Step Procedure
1. **Logging Audit**: Review all `Log.*` calls and OkHttp `HttpLoggingInterceptor` settings.
2. **Sensitive Data Redaction**: Implement redaction filters for coordinates and private tokens.
3. **Audit Log Table**: Maintain `automation_logs` in Room with bounded retention.
4. **Error Boundary Hardening**: Ensure uncaught exceptions do not reveal internal file paths or database queries.
5. **Incident Documentation**: Document incident triage and response workflows in Phase 6 records.

