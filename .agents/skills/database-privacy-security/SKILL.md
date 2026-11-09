---
name: database-privacy-security
description: Database permissions, SQL injection prevention, backup protection, encryption at rest, data retention, and privacy-safe migrations.
---

# Database & Privacy Security Skill

## Purpose & Scope
This skill governs local database security, SQL injection prevention, backup protection, data extraction defense, and privacy-safe migrations in Android SQLite and Room ORM architectures.

## When to Activate
- Auditing Room entities, DAOs, and database queries for SQL injection vectors.
- Configuring Android backup rules (`backup_rules.xml`, `data_extraction_rules.xml`) to prevent physical ADB extraction.
- Implementing cryptographic storage for sensitive records and preference state.
- Authoring safe, deterministic database migrations (`Migration`) preserving data integrity.

## Engineering Invariants
1. **Parameterized Queries Only**: Never execute concatenated SQL queries; all database operations must use parameterized DAO methods or prepared statements.
2. **Backup Data Isolation**: Local SQLite databases (`*.db`, `*-wal`, `*-shm`) and secure preferences must be explicitly excluded from cloud and ADB backups.
3. **Deterministic Migrations**: All schema upgrades must provide explicit, tested migrations (`Migration`) rather than relying on destructive wipes.
4. **Data Minimization**: Retain only necessary meteorological observations; delete old audit logs to conserve storage and minimize privacy footprints.

## Step-by-Step Procedure
1. **DAO Query Audit**: Verify that all Room queries use parameterized bindings.
2. **Backup Rules Configuration**: Define explicit `<exclude>` directives in `backup_rules.xml` and `data_extraction_rules.xml`.
3. **Migration Verification**: Write unit tests executing migrations against sample databases.
4. **Adversarial SQL Injection Tests**: Execute test queries containing hostile SQL injection strings (`' OR '1'='1`, `DROP TABLE`).
5. **Data Pruning Logic**: Implement bounded retention policies for audit logs and stale cache entries.

