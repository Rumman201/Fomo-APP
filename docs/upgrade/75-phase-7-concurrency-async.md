# 75 — Phase 7 Scalable Concurrency & AppExecutors Dispatching

## Overview
Phase 7 replaces uncontrolled thread creation with centralized, bounded thread dispatchers via `AppExecutors.java`.

---

## Thread Dispatcher Allocation Matrix

| Dispatcher | Thread Pool Sizing | Linux Process Priority | Intended Workload |
| :--- | :--- | :--- | :--- |
| **diskIO** | Fixed 2 Threads | `THREAD_PRIORITY_BACKGROUND` | Room SQLite transactions, SharedPreferences I/O, file reading |
| **networkIO** | Fixed 3 Threads | `THREAD_PRIORITY_BACKGROUND` | OkHttp REST requests, Geocoding API lookups, IoT micro-ingestion |
| **computation** | Max(2, CPU Cores) | `THREAD_PRIORITY_DEFAULT` | Polynomial thermal comfort calculations, JSON mapping, DiffUtil |
| **mainThread** | 1 (Main Looper) | `THREAD_PRIORITY_URGENT_DISPLAY` | UI updates, LiveData observations, RecyclerView bindings |

---

## Concurrency Guarantees
- **No Rogue Thread Spawning**: Completely eliminates `new Thread().start()` calls.
- **ANR Prevention**: All disk and network operations strictly isolated to background thread dispatchers.
- **JVM Test Compatibility**: `MainThreadExecutor` includes automatic fallback for host JVM execution where Android `Looper` is unmocked.

