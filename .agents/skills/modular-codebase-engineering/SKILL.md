---
name: modular-codebase-engineering
description: Feature boundaries, module ownership, package design, public interfaces, encapsulation, dependency validation, and architecture fitness checks.
---

# Modular Codebase Engineering Skill

## Purpose
Establishes strong encapsulation, clean package taxonomies, feature boundaries, and dependency governance across the codebase.

## Activation Criteria
- Triggered when re-organizing package structures, declaring visibility modifiers (`public`, `protected`, package-private), or evaluating modular coupling.
- Used when replacing generic dumping-ground packages (`utils`, `helpers`, `common`) with domain-driven structures.

## Package Guidelines (Android Standards)
1. **Lowercase Package Names**: All package hierarchy segments must be strictly lowercase (e.g. `com.example.fomo.ui.main`, never `com.example.fomo.Activitis`).
2. **Feature & Layer Cohesion**:
   - `ui.main`: Main weather dashboard Activity, ViewBinding delegates, UI event observers.
   - `ui.adapter`: RecyclerView ListAdapters, ViewHolders, DiffUtil callbacks.
   - `domain.model`: Domain entities, value objects, immutable data models.
   - `domain.repository`: Abstract repository interfaces.
   - `data.repository`: Concrete repository implementations and memory/remote dispatchers.
3. **Encapsulation & Visibility**:
   - Limit visibility of internal helper classes, view holders, and implementation details.
   - Expose only the required public contracts across layer boundaries.
