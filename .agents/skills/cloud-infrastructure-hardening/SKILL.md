---
name: cloud-infrastructure-hardening
description: Cloud endpoint security, transport protections, CDN/WAF posture, API gateway configuration, and least-privilege cloud network exposure.
---

# Cloud & Infrastructure Hardening Skill

## Purpose & Scope
This skill provides standards for securing client-to-cloud communications, external endpoint trust boundaries, content delivery network (CDN) configurations, and network transport policies.

## When to Activate
- Reviewing cloud endpoint boundaries (Open-Meteo REST endpoints, Geocoding endpoints).
- Ensuring zero cleartext traffic across all mobile network configurations.
- Validating client transport parameters against cloud provider security baselines.

## Engineering Invariants
1. **Zero Cleartext Exposure**: Disable all unencrypted HTTP transport across all build types.
2. **Strict TLS Ciphers**: Restrict transport to TLS 1.2 and TLS 1.3 with forward secrecy ciphers.
3. **No Embedded Cloud Secrets**: Never hardcode cloud master keys or administrative credentials in mobile client code.

## Step-by-Step Procedure
1. **Endpoint Inventory**: Audit all external hostnames and protocol configurations.
2. **Network Security XML**: Enforce `cleartextTrafficPermitted="false"`.
3. **Transport Validation**: Verify that attempts to connect via HTTP are rejected at the platform networking layer.
4. **Credential Audit**: Run automated scanners ensuring zero cloud credentials exist in source trees.

