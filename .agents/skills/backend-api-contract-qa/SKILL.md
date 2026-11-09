---
name: backend-api-contract-qa
description: REST API contract testing, Retrofit endpoint validation, JSON schema conformance, error taxonomy, and negative payload testing.
---

# Backend API Contract QA Skill

## Purpose
Governs integration contract testing between the mobile client and external REST APIs (Open-Meteo Weather and Geocoding services).

## Core Directives
1. Validate JSON deserialization schemas and strict field mapping against upstream API specifications.
2. Verify HTTP error status handling (400 Bad Request, 404 Not Found, 429 Rate Limit, 500-504 Server Errors).
3. Test network edge cases: zero-length bodies, malformed JSON, and unexpected null fields.

