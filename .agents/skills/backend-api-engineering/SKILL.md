---
name: backend-api-engineering
description: REST API design, Retrofit endpoint contracts, payload serialization, error classification, and network contract verification for Android and client-server systems.
---

# Backend API Engineering Skill

## Purpose & Scope
This skill provides standards, conventions, and procedures for designing, integrating, and verifying REST and HTTP-based API services in mobile and client-server architectures. It covers Retrofit interface contracts, OkHttp configuration, JSON serialization, HTTP status mapping, error classification, and contract compatibility.

## When to Activate
- Integrating external RESTful web services or internal backend endpoints.
- Defining HTTP client contracts using Retrofit 2 or equivalent HTTP clients.
- Implementing request/response DTOs (Data Transfer Objects) and domain model mappers.
- Establishing error response envelopes, status code interpretation, and network failure resilience.
- Verifying API contracts against live or mocked endpoints with contract tests.

## Engineering Standards & Invariants
1. **Separation of Contracts**:
   - DTOs (Data Transfer Objects) must never leak into Presentation or Domain layers.
   - Always map DTOs to immutable Domain models through explicit mapper functions.
2. **Type Safety & Nullability**:
   - Explicitly handle null or missing fields in API JSON payloads using default values or defensive checks.
   - Never assume API payloads are 100% complete; guard against partial responses.
3. **HTTP Semantics & Error Classification**:
   - Distinguish network errors (DNS, timeout, connection reset) from HTTP status errors (4xx client error, 5xx server error).
   - Wrap raw HTTP exceptions into domain-level result types (`Result.Success`, `Result.Error`).
4. **Resilience & Timeouts**:
   - All network calls must define explicit connect (15s), read (15s), and write (15s) timeouts.
   - Enforce HTTPS and disable cleartext traffic across all external communication.

## Step-by-Step Procedure
1. **Contract Discovery**:
   - Identify base URL, query parameters, path variables, headers, and response structure.
   - Document response models and WMO/status codes.
2. **DTO Definition**:
   - Create POJO or Kotlin data classes with serialization annotations (`@SerializedName` for Gson or `@Json` for Moshi).
3. **Service Interface**:
   - Define annotated Retrofit interface (`@GET`, `@POST`, `@Query`, `@Path`).
4. **Mapper Implementation**:
   - Write bidirectional or outbound mapper functions translating DTOs to Domain models.
5. **Contract Verification**:
   - Author unit tests with recorded JSON fixtures verifying serialization and mapping edge cases.

## Tools & Commands
- Gradle execution: `./gradlew testDebugUnitTest`
- Network testing: MockWebServer, JUnit, Retrofit Mock Converter.
