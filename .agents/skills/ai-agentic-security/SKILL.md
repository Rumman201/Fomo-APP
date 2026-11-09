---
name: ai-agentic-security
description: Prompt injection defenses, agent tool permissions, retrieval isolation, hallucination bounding, memory protection, and adversarial security evaluations.
---

# AI & Agentic Security Skill

## Purpose & Scope
This skill provides comprehensive defensive engineering standards for securing AI models, large language model (LLM) interfaces, tool-using autonomous workflows, and automated reasoning engines against prompt injection, data poisoning, hallucination risks, and unauthorized tool calls.

## When to Activate
- Hardening AI synthesis engines (`WeatherAiAdvisor`) against prompt injection and jailbreak payloads.
- Implementing post-generation hallucination and disparity filters to protect domain facts.
- Restricting tool capabilities to read-only, non-destructive operations.
- Designing adversarial evaluation suites assessing AI safety boundaries.

## Engineering Invariants
1. **Never Trust Model Output**: Generative AI text is probabilistic and untrusted; it must never drive security decisions, database mutations, or device controls without deterministic validation.
2. **Deterministic Domain Ground Truth**: Core facts (temperatures, severe weather warnings) are owned exclusively by deterministic code. Model text claiming contradictory facts must be discarded.
3. **Prompt Injection Resistance**: Pre-flight regex filters must inspect raw payloads for instruction-override attempts (`ignore previous instructions`, `system prompt`) and abort immediately.
4. **Data Minimization & Privacy**: Never transmit PII, precise coordinates, or credentials to external LLMs.

## Step-by-Step Procedure
1. **Attack Surface Mapping**: Identify all model inputs, prompts, context assembly routines, and output consumers.
2. **Dual-Tier Guardrail Design**: Implement pre-flight injection detection and post-generation disparity filters.
3. **Deterministic Fallback Pipeline**: Ensure an offline rule-based generator executes whenever validation fails.
4. **Adversarial Benchmark Suite**: Author automated tests simulating prompt injection, jailbreaks, and extreme hallucinations.
5. **Continuous Verification**: Monitor fallback rates and schema validation errors.

