---
name: retrieval-and-knowledge-systems
description: Structured meteorological knowledge indexing, WMO code interpretation, atmospheric guidelines retrieval, and tenant-safe contextual assembly.
---

# Retrieval and Knowledge Systems Skill

## Purpose & Scope
This skill governs the ingestion, indexing, retrieval, and contextual assembly of structured knowledge assets in weather and domain-specific systems. It ensures fast, reliable, and permission-safe retrieval of domain reference data (such as WMO weather definitions, heat index tables, and clothing guidelines).

## When to Activate
- Ingesting structured meteorological tables, standard WMO weather interpretations, and thermal comfort indices.
- Implementing local knowledge caches and retrieval pipelines for AI context enrichment.
- Validating citation accuracy, domain ground truth, and indexing freshness.

## Engineering Invariants
1. **Curated & Authoritative Sources**: Only verified, authoritative domain knowledge (e.g. World Meteorological Organization standards, NOAA Heat Index standards) may be indexed.
2. **Deterministic Lookup**: Static reference tables must use deterministic indexing (O(1) memory or indexed SQLite lookups) rather than unbounded vector searches.
3. **Data Integrity & Consistency**: Retrieved reference data must be verified against current domain models.

## Step-by-Step Procedure
1. **Knowledge Cataloging**: Identify necessary reference standards (WMO definitions, Beaufort wind scale, UV index categories).
2. **Data Structure Optimization**: Organize reference datasets into memory-efficient, indexed structures.
3. **Retrieval Pipeline**: Implement deterministic lookup services returning typed knowledge models.
4. **Context Injection**: Format retrieved guidelines into contextual payloads for AI or decision services.
5. **Validation**: Test lookup accuracy and boundary coverage across all supported codes and scales.

