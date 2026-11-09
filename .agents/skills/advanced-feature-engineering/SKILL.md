---
name: advanced-feature-engineering
description: Requirements discovery, user value assessment, vertical-slice implementation, multi-location search, atmospheric intelligence, and rollout control for mobile weather applications.
---

# Advanced Feature Engineering Skill

## Purpose & Scope
This skill provides comprehensive methodologies and engineering standards for discovering, designing, vertically implementing, and rolling out advanced end-user capabilities in mobile and client-server systems. It ensures that new features deliver measurable user value without destabilizing established architectures, design systems, or data integrity.

## When to Activate
- Introducing new vertical slices (e.g. multi-location search, atmospheric intelligence, custom alerting).
- Designing end-to-end workflows connecting Presentation (UI), Domain, Data, Network, and Persistence.
- Converting user product opportunities into strict technical specifications and acceptance tests.
- Implementing feature toggles and progressive rollout gates.

## Engineering Invariants
1. **Vertical Slice Completeness**: Every feature must implement all applicable layers: UI, ViewModel, Domain Model, Repository, Local Cache (Room), and Remote API (Retrofit).
2. **Design System Continuity**: All UI extensions must strictly utilize established design tokens (`colors.xml`, `dimens.xml`, `themes.xml`) and Material 3 components.
3. **Deterministic Authority**: Probabilistic or external data must never bypass domain invariants, safety checks, or user-selected permissions.
4. **Offline Resilience**: Features must provide seamless degradation, empty states, and cached offline experiences.

## Step-by-Step Procedure
1. **Requirements & Value Framing**: Define the user goal, current gap, acceptance criteria, and operational impact.
2. **Domain Modeling**: Formulate immutable value objects and repository contracts in `domain/model` and `domain/repository`.
3. **Data & Persistence**: Add necessary Room entities, DAOs, and migrations in `data/local`.
4. **Network Integration**: Define Retrofit contracts and DTO mappers in `data/remote`.
5. **Presentation Layer**: Create ViewModels, ViewBinding layouts, and accessible UI controls adhering to Material 3.
6. **Verification & Regression Testing**: Author unit and integration tests verifying happy paths, edge cases, and failure modes.

