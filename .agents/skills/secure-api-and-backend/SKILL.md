---
name: secure-api-and-backend
description: Input validation, output encoding, injection prevention, SSRF protections, API authorization, rate and resource limits, and certificate pinning.
---

# Secure API and Backend Skill

## Purpose & Scope
This skill provides standards for securing external API clients, network transport layers, REST interfaces, and backend adapters. It encompasses transport layer security (TLS), certificate pinning, input sanitization, injection defense, and rate limiting.

## When to Activate
- Hardening HTTP networking clients (Retrofit 2, OkHttp) against Man-In-The-Middle (MITM) attacks.
- Implementing certificate pinning and public key hashing (`CertificatePinner`).
- Enforcing input validation and sanitizing user search queries and external payloads.
- Configuring network security configurations and cleartext traffic policies.

## Engineering Invariants
1. **Mandatory HTTPS**: All external network communication must enforce TLS 1.2+; cleartext HTTP traffic is strictly prohibited.
2. **Certificate Pinning**: Critical meteorological and geocoding endpoints must enforce cryptographic certificate pins to block proxy tampering and rogue CAs.
3. **No Unsanitized Inputs**: All user inputs (city searches, query strings) must be sanitized against injection vectors (`<script>`, SQL fragments, control characters).
4. **Redacted Logging**: HTTP logging interceptors must never log private tokens, coordinates, or sensitive payloads in release builds.

## Step-by-Step Procedure
1. **Endpoint Audit**: Inventory all external URLs, hostnames, and protocols.
2. **Network Security Config**: Configure `res/xml/network_security_config.xml` with domain-specific trust anchors and cleartext restrictions.
3. **Certificate Pinning**: Configure OkHttp `CertificatePinner` with SHA-256 certificate public key hashes.
4. **Input Sanitization**: Author robust input sanitizers filtering control characters and script tags.
5. **Contract & Pinning Tests**: Author unit tests validating certificate pinner configurations and sanitizer effectiveness.

