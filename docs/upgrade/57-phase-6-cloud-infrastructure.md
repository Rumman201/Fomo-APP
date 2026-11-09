# Phase 6 — Cloud Infrastructure & Endpoint Security

## 1. Upstream Cloud Architecture

The Fomo Android application communicates directly with Open-Meteo public API endpoints:
- Primary Weather API: `https://api.open-meteo.com/v1/forecast`
- Geocoding API: `https://geocoding-api.open-meteo.com/v1/search`

No custom proprietary cloud backend or middleman proxy is maintained, minimizing operational attack surface, credential management overhead, and intermediate data exposure.

---

## 2. External Service Hardening Controls

| Endpoint | Protocol | Port | Authentication | Pinning Status | Error Handling |
|---|---|---|---|---|---|
| `api.open-meteo.com` | HTTPS (TLS 1.2+) | 443 | None (Public Data) | SHA-256 Pinned (ISRG Root X1/X2) | Retrofit non-2xx mapped to typed `IOException` |
| `geocoding-api.open-meteo.com` | HTTPS (TLS 1.2+) | 443 | None (Public Data) | SHA-256 Pinned (ISRG Root X1/X2) | Defensive empty list fallback on 404/500 |

### 2.1 CDN & Transport Verification
- Open-Meteo utilizes Cloudflare CDN distribution.
- Transport traffic is strictly constrained to port 443.
- OkHttp automatically handles HTTP/2 connection multiplexing and keep-alive connection pooling over TLS.

