---
name: database-engineering
description: Android local persistence design, Room ORM, schema migrations, DataStore preferences, indexing, and offline caching strategies.
---

# Database & Persistence Engineering Skill

## Purpose
Governs client-side persistence architecture, database schema design, offline cache synchronization, and preference storage.

## Activation Criteria
- Triggered when implementing offline weather caching, location history, favorites, or user configuration preferences.
- Used when creating Room entities, DAOs, migrations, or Jetpack DataStore implementations.

## Architectural Principles
1. **Single Source of Truth (SSOT)**:
   - Repository serves cached data from Room/DataStore immediately, then fetches fresh data from network in background.
2. **Schema & Migration Safety**:
   - Always version database schemas.
   - Test migrations deterministically with `MigrationTestHelper`.
   - Never use `fallbackToDestructiveMigration()` in production builds.
3. **Threading & Transactions**:
   - Perform all database queries and transactions off the Main thread using Coroutines (`suspend fun`) or RxJava.
   - Use `@Transaction` on multi-step DAO operations to guarantee atomicity.
4. **Key-Value Storage**:
   - Use Jetpack DataStore (Preferences or Proto) instead of legacy `SharedPreferences`.
