---
name: ai-inference-efficiency
description: On-device mathematical calculation memoization, floating-point optimization, token-free rule execution, and prompt cache architecture for weather AI components.
---

# AI Inference Efficiency Skill

## Purpose & Scope
Provides optimization patterns for deterministic on-device intelligence, mathematical memoization of thermal comfort formulas (NOAA Steadman, Wind Chill), and efficient caching of generated advisory outputs.

## Specialist Responsibilities
- **AI Inference Performance Engineer**: Optimizes computational overhead, minimizes redundant polynomial evaluations, and implements structured advisory caching.

## Methodology & Execution Steps
1. **Mathematical Memoization**: Cache results of expensive floating-point functions (e.g. `Math.pow`, polynomial expansions in Steadman Heat Index) using discretized input keys.
2. **Advisory Synthesis Caching**: Cache generated `AiWeatherAdvisory` models keyed by weather condition code and rounded temperature to eliminate redundant string generation.
3. **Zero-Token Edge Execution**: Leverage deterministic rule engines (`AtmosphericIntelligenceService`, `WeatherAutomationEngine`) to provide instant user value with zero cloud API latency or token cost.
4. **Input Size Bounding**: Enforce strict character limits on user and contextual inputs before processing to prevent algorithmic complexity spikes.
5. **Quality & Accuracy Verification**: Confirm that memoization and quantization do not introduce numerical drift or compromise safety guardrails.

