---
name: integration-contract-engineering
description: Third-party API integration, Open-Meteo REST API contract mapping, DTO-to-domain boundary enforcement, JSON deserialization validation, and contract testing.
---

# Integration Contract Engineering Skill

## Purpose & Scope
This skill governs the integration of third-party APIs and external microservices. It defines patterns for contract mapping, schema verification, DTO validation, and resilient boundary isolation.

## When to Activate
- Integrating external weather services (Open-Meteo REST API) or location/geocoding services.
- Mapping third-party JSON schemas to internal domain models.
- Handling API schema drift or upstream changes safely without breaking mobile clients.
- Writing contract tests using fixture JSON payloads representing actual API responses.

## Engineering Standards & Invariants
1. **Upstream Schema Isolation**:
   - Third-party response models must be isolated within `data.remote.dto` or `data.network.model`.
   - Domain layer must never import or depend on third-party DTOs.
2. **Defensive Deserialization**:
   - Anticipate missing, null, or out-of-order array data from remote weather APIs.
   - Verify array lengths match expected hourly/daily time arrays before indexing.
3. **No Hardcoded API Keys or Endpoints**:
   - Base URLs and endpoint paths must be centralized in configuration constants.
   - Use public, free, keyless endpoints (like Open-Meteo) where possible to prevent credential leaks.

## Step-by-Step Procedure
1. **Contract Definition**:
   - Document third-party endpoints, parameters (`latitude`, `longitude`, `hourly`, `current_weather`), and sample response payloads.
2. **DTO Implementation**:
   - Create serializable POJOs capturing current weather, hourly forecast arrays, and units.
3. **Mapper Function**:
   - Translate DTO arrays (timestamps, temperatures, weather codes) into clean `List<Hourly>` and `CurrentWeather` domain entities.
4. **Contract Verification**:
   - Test deserialization against real recorded JSON fixtures from Open-Meteo.
