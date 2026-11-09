---
name: third-party-integration-engineering
description: Integration of external REST APIs, Open-Meteo Geocoding endpoints, network resilience, DTO mapping, rate limiting, and contract testing.
---

# Third-Party Integration Engineering Skill

## Purpose & Scope
This skill provides architecture patterns, resilience techniques, and validation procedures for integrating external web services, REST APIs, and third-party data providers. It ensures that external contracts are safely encapsulated behind domain boundaries, properly tested with recorded fixtures, and shielded against outages.

## When to Activate
- Integrating third-party APIs (e.g. Open-Meteo Geocoding API for city search, Open-Meteo Air Quality API).
- Designing resilient Retrofit interfaces, interceptors, and DTO mappers.
- Handling rate limits, network timeouts, HTTP error codes, and partial responses.
- Writing contract tests using real and simulated API payloads.

## Engineering Invariants
1. **Encapsulation & Boundary Isolation**: External DTOs must never leak into UI or business logic. All external payloads must be mapped to internal domain entities.
2. **Resilience & Timeouts**: All third-party HTTP calls must enforce bounded timeouts (<=15s) and safe fallback handling.
3. **No Unencrypted Channels**: All external API communications must use HTTPS with certificate validation.
4. **Transparent Verification**: Clearly distinguish between live API responses and mocked integration tests in documentation and logs.

## Step-by-Step Procedure
1. **Contract Discovery**: Inspect official third-party API documentation, endpoints, parameters, and rate limits.
2. **DTO & Service Design**: Implement typed DTOs with serialization annotations and Retrofit interface definitions.
3. **Mapper & Boundary Implementation**: Author domain mappers that sanitize, validate, and convert external responses into domain models.
4. **Resilience Configuration**: Configure OkHttpClient with timeouts, logging, and error interception.
5. **Contract Testing**: Author unit tests validating DTO deserialization against authentic JSON fixtures.

