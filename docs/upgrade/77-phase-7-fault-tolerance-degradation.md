# 77 — Phase 7 Fault Tolerance & Graceful Degradation

## Fault Tolerance Matrix

| Failure Mode | Detection Vector | Fault Tolerance Strategy | User Experience |
| :--- | :--- | :--- | :--- |
| **No Network Connectivity** | `ConnectivityManager` active network null | Instant switch to L2 SQLite cached data | Instant render with "Offline Mode" chip |
| **Upstream 5xx / Timeout** | OkHttp `SocketTimeoutException` / HTTP 500-504 | Circuit breaker trips; serve L1/L2 cache | Display cached weather with last-sync badge |
| **Corrupted Room Record** | Room SQLite parsing exception | Fallback to safe defaults or re-query | App maintains stability without crashing |
| **IoT Sensor Data Out of Range** | Ingestion validation rules | Bad record quarantined; valid batch committed | Notification banner on degraded sensor |
| **Keystore Hardware Key Wipe** | `KeyStoreException` | Auto-reinitialization of encryption keys | Safe reset of session tokens without crash |

---

## Graceful Degradation Principles
1. **Never Show a Blank Screen**: A cached forecast from 2 hours ago is infinitely better than an empty screen with an unhandled exception.
2. **Immediate Feedback**: Notify users cleanly through non-blocking Snackbars or subtle status chips when displaying stale or degraded telemetry.
3. **Preserve Battery**: Avoid aggressive polling when network state is known to be down or unreachable.

