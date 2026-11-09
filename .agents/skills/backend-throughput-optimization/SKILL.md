---
name: backend-throughput-optimization
description: Network request pipeline optimization, OkHttp connection pooling, HTTP/2 multiplexing, JSON deserialization speedups, and payload size reduction.
---

# Backend Throughput Optimization Skill

## Purpose & Scope
Provides principles and implementations for optimizing network roundtrips, reducing payload serialization overhead, enforcing HTTP connection pooling, and optimizing client-side data transfer.

## Specialist Responsibilities
- **Principal Backend Performance Engineer**: Owns network client configuration, OkHttp connection reuse, Retrofit adapter performance, and JSON parser tuning.

## Methodology & Execution Steps
1. **Connection Pooling**: Configure OkHttp `ConnectionPool` with tuned idle connection counts and keep-alive duration to minimize TLS handshake latency.
2. **Serialization Efficiency**: Use optimized Gson streaming adapters and avoid unnecessary reflection during JSON-to-DTO conversion.
3. **Payload Compression**: Ensure HTTP request headers include `Accept-Encoding: gzip, deflate` and support automatic transparent decompression.
4. **Conditional Requests**: Leverage HTTP `ETag` and `If-None-Match` headers to receive `304 Not Modified` responses without re-transferring payloads.
5. **Request Coalescing**: Deduplicate identical concurrent network fetches targeting the same endpoint.

