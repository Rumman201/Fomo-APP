---
name: intelligent-caching-engineering
description: Multi-tier caching architecture (L1 memory, L2 disk/Room, HTTP disk cache), TTL management, cache stampede prevention, and authorization-aware cache isolation.
---

# Intelligent Caching Engineering Skill

## Purpose & Scope
Provides comprehensive design patterns for multi-level caching strategies, TTL management, cache invalidation, cache stampede mitigation, and privacy-safe cache storage.

## Specialist Responsibilities
- **Principal Caching Architect**: Designs multi-tier L1 (In-Memory LRU) and L2 (Persistent SQLite/Room) caching layers, configures OkHttp HTTP disk caches, and prevents stale data issues.

## Methodology & Execution Steps
1. **Multi-Tier Caching Topology**: Implement fast sub-millisecond in-memory caching (L1) coupled with persistent offline Room storage (L2) and OkHttp HTTP cache.
2. **Cache Key Formulation**: Ensure deterministic, collision-resistant cache keys incorporating coordinates, city names, and query parameters.
3. **TTL Policies**: Enforce explicit freshness time-to-live windows (e.g. 15–30 min for weather forecasts, 24 hours for geocoding results).
4. **Stampede Prevention**: Use single-flight request coalescing to prevent multiple simultaneous misses from hammering backend endpoints.
5. **Cache Invalidation & Eviction**: Configure bounded maximum size limits (`LruCache`) to prevent unbounded memory growth.

