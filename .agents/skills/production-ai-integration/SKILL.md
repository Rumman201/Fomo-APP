---
name: production-ai-integration
description: Architectural integration of AI and machine intelligence, structured prompt schemas, deterministic guardrails, privacy boundaries, and graceful fallback mechanisms for weather intelligence.
---

# Production AI Integration Skill

## Purpose & Scope
This skill governs the integration of artificial intelligence, natural language generation, and machine-assisted reasoning into production mobile and client architectures. It guarantees that AI capabilities provide high user value while operating within strict deterministic boundaries, schema validation, cost/latency budgets, and privacy safeguards.

## When to Activate
- Designing natural language weather summaries, atmospheric briefings, or activity recommendations.
- Structuring AI prompts, inputs, and strict JSON output schemas.
- Implementing client-side guardrails, safety fallbacks, and deterministic truth enforcement.
- Preventing prompt-injection, hallucination leaks, or unauthorized tool executions.

## Engineering Invariants
1. **Deterministic Authority**: Core facts (actual temperature, wind speed, severe storm warnings) are strictly generated and enforced by deterministic domain code. AI is never authoritative over raw telemetry.
2. **Strict Schema Validation**: All model outputs must conform to a strongly-typed schema. Malformed or unrecognized structures must trigger immediate deterministic fallbacks.
3. **Data Minimization & Privacy**: Never transmit PII, precise GPS coordinates, device identifiers, or credentials in AI prompts. Only sanitized meteorological metrics may be processed.
4. **Resilient Failure Handling**: AI services must fail gracefully without disrupting core weather presentation.

## Step-by-Step Procedure
1. **Use Case & Value Definition**: Determine whether language synthesis adds genuine value over deterministic formatting.
2. **Contract & Schema Design**: Define strict input context and typed output contracts (e.g. `AiWeatherAdvisory`).
3. **Guardrail Implementation**: Build pre-flight sanitization and post-generation validation checks.
4. **Deterministic Fallback Pipeline**: Implement local rule-based generators that execute if AI is unreachable or invalid.
5. **Evaluation & Stress Testing**: Verify behavior against edge cases, extreme weather, and adversarial inputs.

