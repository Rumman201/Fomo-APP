---
name: feature-rollout-and-observability
description: Feature flags, local runtime configuration, error telemetry, operational diagnostics, graceful feature gating, and rollback strategies.
---

# Feature Rollout and Observability Skill

## Purpose & Scope
This skill provides engineering guidance for managing feature releases, runtime toggles, operational logging, diagnostic reporting, and graceful rollback strategies. It ensures that new capabilities can be enabled or disabled dynamically without requiring risky emergency code modifications.

## When to Activate
- Introducing high-impact or experimental features (e.g. AI advisory, automation alerts, multi-location search).
- Configuring local SharedPreferences or remote feature flag providers.
- Logging operational diagnostics and error rates across network, database, and UI workflows.
- Establishing kill switches for external service degradation.

## Engineering Invariants
1. **Safe Defaults**: All feature flags must default to safe, functional fallback behavior if configuration is missing or corrupted.
2. **Deterministic Gating**: Flags must evaluate consistently during an active session to prevent jarring UI state transitions.
3. **No PII in Telemetry**: Operational logs must never contain personal data, credentials, or precise location coordinates.
4. **Instant Rollback / Disable**: Gated features must provide an immediate off switch that returns the app to its baseline stable state.

## Step-by-Step Procedure
1. **Flag Definition**: Define typed feature flag keys and default values in a centralized manager.
2. **Access Abstraction**: Wrap flag checks in a clean service interface (`FeatureFlagService`).
3. **UI & Service Gating**: Use flag values to conditionally render UI entry points or trigger background routines.
4. **Diagnostic Logging**: Record feature activation status and operational errors at appropriate log levels.
5. **Rollback Verification**: Test behavior when flags are toggled off to ensure smooth degradation.

