---
name: scalable-concurrency-engineering
description: Android background thread management, thread pool sizing, bounded dispatchers, queue backpressure, and race condition prevention.
---

# Scalable Concurrency Engineering Skill

## Purpose & Scope
Governs asynchronous concurrency architecture, centralized thread pool execution (`AppExecutors`), backpressure handling, worker queue sizing, and thread safety standards.

## Specialist Responsibilities
- **Principal Distributed Systems Architect**: Enforces bounded thread pools, eliminates thread starvation, and prevents UI-blocking operations.

## Methodology & Execution Steps
1. **Thread Pool Centralization**: Replace rogue, unpooled thread instantiations (`Executors.newSingleThreadExecutor()`) with unified, bounded thread dispatchers (`AppExecutors`).
2. **Dedicated Dispatcher Pools**: Partition execution pools into Disk I/O (bounded), Network I/O (bounded), Computation (CPU-core bounded), and Main Thread (Handler-based).
3. **Queue Bounding & Backpressure**: Use bounded blocking queues to avoid OutOfMemory errors under burst arrival rates.
4. **Thread Safety & Race Condition Audits**: Audit shared mutable state for proper volatile visibility, synchronized sections, or atomic primitives (`AtomicBoolean`, `AtomicInteger`).
5. **Thread Naming & Diagnostics**: Assign informative thread names (e.g. `fomo-disk-io-1`, `fomo-net-2`) to assist trace profiling and ANR triage.

