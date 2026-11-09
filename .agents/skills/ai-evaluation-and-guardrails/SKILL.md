---
name: ai-evaluation-and-guardrails
description: Empirical evaluation suites, structured schema validation, hallucination scoring, adversarial testing, and safety guardrails for weather AI components.
---

# AI Evaluation and Guardrails Skill

## Purpose & Scope
This skill provides systematic testing, validation, and benchmarking procedures for AI and automated reasoning systems. It replaces subjective impressions with empirical pass/fail criteria, adversarial evaluation, schema validation metrics, and prompt regression testing.

## When to Activate
- Benchmarking AI output accuracy against known meteorological baselines.
- Authoring automated test suites for AI prompts and structured JSON parsers.
- Executing adversarial test cases (e.g. malformed inputs, prompt injection attempts, extreme weather values).
- Measuring latency, fallback rates, and schema conformance.

## Engineering Invariants
1. **Empirical Verification**: Never declare an AI integration functional without running formal evaluation test cases.
2. **Zero Tolerance for Hallucinations in Safety-Critical Domains**: Temperatures, precipitation status, and wind speed reported in summaries must strictly match source telemetry.
3. **Adversarial Resilience**: AI components must handle hostile inputs, missing fields, and out-of-range sensor readings safely.
4. **Reproducible Baselines**: Evaluation datasets must be versioned and committed alongside test fixtures.

## Step-by-Step Procedure
1. **Dataset Construction**: Assemble representative test cases covering typical, extreme, and corrupted weather data.
2. **Evaluation Metrics**: Define scoring rubrics for schema validity, factual consistency, and latency.
3. **Automated Assertion Suite**: Author JUnit test cases verifying that AI responses conform to schema and match domain ground truths.
4. **Failure Injection**: Run adversarial tests simulating network timeouts, corrupted payloads, and injection attempts.
5. **Reporting**: Document evaluation scores, pass rates, and remaining limitations.

