---
name: database-integrity-engineering
description: Android Jetpack Room database schema assessment, table constraints, indices, migrations, transactions, and data integrity verification.
---

# Database Integrity Engineering Skill

## Purpose & Scope
This skill governs the design, implementation, and verification of local SQLite persistence through Android Jetpack Room. It establishes schema integrity, transactional consistency, indexing strategies, thread safety, migration plans, and automated database testing.

## When to Activate
- Creating or updating Room entities, DAOs, and database classes.
- Adding relational tables, primary keys, foreign keys, or unique constraints.
- Writing complex SQL queries (`@Query`, `@Transaction`) and evaluating index utilization.
- Planning and executing schema migrations (`Migration(1, 2)`).
- Testing in-memory Room instances for data integrity and thread safety.

## Engineering Standards & Invariants
1. **Primary Keys & Constraints**:
   - Every entity must declare an explicit primary key (`@PrimaryKey`).
   - Use composite keys or unique indices where entity uniqueness requires multiple columns.
2. **Transactional Consistency**:
   - Any multi-entity mutation or bulk update must be annotated with `@Transaction` in the DAO.
3. **Threading & Non-Blocking Access**:
   - Never query or write to Room on the Android Main (UI) thread (`cannotAccessOnMainThread` invariant).
   - Use background executors or Coroutines for all database operations.
4. **Deterministic Migration & Testing**:
   - Enable schema export (`room.schemaLocation`) for version tracking.
   - For version increments, write migration scripts and test them using `MigrationTestHelper`.
   - Never rely on `fallbackToDestructiveMigration()` in production without explicit data retention considerations.

## Step-by-Step Procedure
1. **Schema Design**:
   - Define entity classes with `@Entity(tableName = "...")`.
   - Specify non-null constraints, default values, and column names.
2. **DAO Contract**:
   - Define DAO interfaces with `@Dao`, `@Insert(onConflict = OnConflictStrategy.REPLACE)`, `@Update`, `@Query`.
3. **Database Definition**:
   - Define abstract `@Database(entities = {...}, version = 1, exportSchema = false)` class.
4. **Integration in Repository**:
   - Expose repository methods that read from DAO and write network responses to DAO.
5. **Testing**:
   - Write unit tests using `Room.inMemoryDatabaseBuilder(context, ...)` to verify CRUD and constraints.
