---
name: database-integrity-qa
description: Room SQLite schema integrity, composite index performance, atomic transactions, migration compatibility, and corruption resilience.
---

# Database Integrity QA Skill

## Purpose
Validates local persistence reliability, entity constraints, index coverage, and backward-compatible schema migrations in Room SQLite.

## Core Directives
1. Verify database migration scripts (`MIGRATION_1_2`, `MIGRATION_2_3`) upgrade schema without data loss.
2. Ensure primary key uniqueness, foreign key consistency, and composite index usage (`EXPLAIN QUERY PLAN`).
3. Validate atomic write boundaries and thread safety under concurrent read/write stress.

