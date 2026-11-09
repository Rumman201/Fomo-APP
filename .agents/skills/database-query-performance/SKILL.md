---
name: database-query-performance
description: Android Jetpack Room and SQLite query optimization, indexing strategy, transaction boundary tuning, and query plan analysis.
---

# Database Query Performance Skill

## Purpose & Scope
Governs the optimization of Android Room local persistence, SQLite query execution plans, index design, schema efficiency, and transactional throughput.

## Specialist Responsibilities
- **Principal Database Performance Engineer**: Evaluates SQLite query plans (`EXPLAIN QUERY PLAN`), defines composite and covering indexes, and tunes DAO transaction boundaries.

## Methodology & Execution Steps
1. **Query Plan Inspection**: Analyze query paths for full table scans (`SCAN TABLE`) and convert them into index seeks (`SEARCH TABLE USING INDEX`).
2. **Index Optimization**: Add targeted Room indices (`@Entity(indices = {@Index(...)})`) on frequently filtered and sorted columns (e.g. `isFavorite`, `selectedAt`, `name`).
3. **Transaction Batching**: Wrap multi-row insertions and update operations inside `@Transaction` blocks to prevent disk sync thrashing.
4. **Projection Minimization**: Select only required columns rather than `SELECT *` where large entities or unnecessary fields exist.
5. **Database Benchmarking**: Measure execution latency of DAO operations before and after index additions.

