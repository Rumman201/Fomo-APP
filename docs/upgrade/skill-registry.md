# Skill Registry — Antigravity Engineering System

## Active Workspace Skills

| Skill Name | Purpose | Activation Conditions | Relevant Project Area | Dependencies & Tooling | Reuse in Phases 2–10 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **`project-intelligence-audit`** | Deep repository exploration, dependency mapping, feature inventory, risk assessment | Phase start, new module onboarding, dependency review | Whole project | PowerShell, Gradle, Git | Phases 1–10 (Continuous Audit) |
| **`system-architecture-review`** | Architecture evaluation, boundary validation, technical design decisions | Architecture refactors, layer separation | Architecture, Presentation/Domain/Data | Class diagrams, Gradle dependency graphs | Phase 2 (Modernization) |
| **`mobile-engineering`** | Android lifecycle safety, threading, backward compatibility, adapter design | Activity/Fragment/Adapter implementation, background sync | `app/src/main/java` | Android SDK, JDK 17, Gradle | Phases 2, 3, 5 |
| **`android-ui-ux-engineering`** | Material Design 3, layout flattening, typography, TalkBack a11y, UI polish | Layout editing (`res/layout`), themes, styles | `app/src/main/res/` | Android Layout Inflator, Lint, a11y checks | Phases 4, 10 |
| **`security-hardening`** | Threat modeling, permission audit, network security, secure storage | Manifest permissions, API keys, storage | `AndroidManifest.xml`, network, prefs | Android Lint, Keystore, ProGuard | Phase 6 (Security) |
| **`database-engineering`** | Local persistence, Room ORM, schema migrations, DataStore caching | Offline caching, favorites, search history | Data layer, Room, DataStore | SQLite, Room compiler, KSP | Phase 3 (Data Layer) |
| **`performance-optimization`** | Startup time, memory leak prevention, layout overdraw, RecyclerView recycling | Frame drops, high memory, startup latency | Views, Adapters, Image loading | Systrace, Layout Inspector, Glide | Phase 7 (Performance) |
| **`qa-verification`** | Deterministic unit testing, Robolectric, Espresso UI testing, regression gates | Test writing, verification gates, CI/CD | `app/src/test`, `app/src/androidTest` | JUnit 4/5, Gradle test runner | Phases 1, 9, 10 |
| **`devops-reliability`** | Gradle optimization, version catalogs, ProGuard/R8, CI/CD pipelines | Build scripts, dependencies, CI workflows | `build.gradle.kts`, `libs.versions.toml`, CI | Gradle, GitHub Actions, R8 | Phases 1, 8 |
| **`technical-documentation`** | Architecture docs, ADRs, developer onboarding, API documentation | Technical decisions, phase handoffs | `docs/`, `README.md`, Javadoc | Markdown | Phases 1–10 |
| **`architecture-modernization`** | Clean Architecture, MVVM layer separation, boundary definition | Architecture restructuring, layer separation | Presentation, Domain, Data layers | Android Architecture Components, ADRs | Phases 2–10 |
| **`safe-large-scale-refactoring`** | Characterization testing, incremental refactoring, behavior preservation | Major package moves, class extractions | Whole project | Git, JUnit, Gradle | Phases 2–5 |
| **`modular-codebase-engineering`** | Package taxonomy, visibility control, public interfaces, encapsulation | Feature boundary definition, package design | `com.example.fomo.*` | Java package rules, Lint | Phases 2, 4 |
| **`type-safety-and-contracts`** | Strict type contracts, immutability, nullability, sealed UI states | Domain models, ViewBinding, ViewModel state | Models, ViewBinding, State | AndroidX Annotations, ViewBinding | Phases 2, 3 |
| **`technical-debt-reduction`** | Debt classification, dead code removal, complexity reduction | Technical debt eradication | Entire codebase | Static analysis, Lint | Phases 2–10 |
| **`architecture-regression-testing`** | State transition testing, contract verification, architecture invariants | Refactoring validation, state testing | `app/src/test` | JUnit 4, Architecture tests | Phases 2, 9 |
| **`backend-api-engineering`** | REST API design, Retrofit endpoints, JSON serialization, error taxonomy | Network integration, API contracts | Data layer, network, DTOs | Retrofit 2, OkHttp, Moshi/Gson | Phases 3, 5, 8 |
| **`database-integrity-engineering`** | Room schema integrity, constraints, indexes, migrations, transactions | Database design, local caching | Data layer, Room ORM, SQLite | AndroidX Room, SQLite | Phases 3, 7 |
| **`domain-logic-verification`** | Business rules, WMO meteorological mapping, cache TTL, domain invariants | Domain model verification, calculation checks | Domain layer, business logic | JUnit 4, parameterized tests | Phases 3, 9 |
| **`distributed-processing-reliability`** | Background concurrency, thread isolation, retry logic, exponential backoff | Async data fetching, offline queueing | Data layer, Executors, Coroutines | Java Concurrency, Executors | Phases 3, 7 |
| **`backend-resilience-and-observability`** | Resilient HTTP, timeouts, connection pooling, offline-first fallback, logging | Network resilience, diagnostics | Network, logging, error handling | OkHttp, LoggingInterceptor | Phases 3, 8 |
| **`integration-contract-engineering`** | Third-party API integration, Open-Meteo contract mapping, DTO isolation | Weather API integration, contract testing | Data layer, API adapters | Retrofit, JSON fixtures, JUnit | Phases 3, 9 |
| **`creative-frontend-direction`** | Visual identity, art composition, typography hierarchy, cohesive brand | Layout redesign, visual polish | Presentation layer, layouts, themes | Material Design 3, Figma, typography | Phases 4, 10 |
| **`advanced-motion-engineering`** | Android animations, entrance transitions, microinteractions, reduced motion | State transitions, item animations | Presentation, ViewPropertyAnimator | Android Animators, Interpolators | Phases 4, 10 |
| **`frontend-design-system`** | Design tokens, Material 3 theming, semantic colors, component styles | Token architecture, styles | `res/values/`, styles, themes | XML attributes, Material 3 | Phases 4, 10 |
| **`responsive-experience-engineering`** | Adaptive layouts, multi-density, tablet/foldable, touch ergonomics | Layout scaling, multi-screen | `res/layout/`, ConstraintLayout | Android WindowMetrics, dp/sp | Phases 4, 10 |
| **`accessibility-and-inclusive-ux`** | TalkBack support, content descriptions, heading semantics, contrast | Accessibility audits, a11y polish | Layouts, strings, views | TalkBack, WCAG 2.2 AA, Lint | Phases 4, 10 |
| **`premium-interaction-design`** | Touch feedback, material ripples, bottom sheets, pull-to-refresh | Gesture handling, bottom sheets | Views, Adapters, Dialogs | Material3 BottomSheet, Ripple | Phases 4, 10 |
| **`frontend-performance-profiling`** | Rendering performance, overdraw elimination, view hierarchy, 60fps | Layout efficiency, jank prevention | Layouts, RecyclerView | Android Lint, Profiler | Phases 4, 7 |
| **`visual-regression-and-browser-qa`** | Mobile layout inspection, multi-viewport verification, state testing | Visual QA, regression tests | Whole frontend | Lint, Unit tests, Gradle | Phases 4, 9 |
| **`advanced-feature-engineering`** | Requirements discovery, vertical-slice implementation, multi-location search, atmospheric intelligence | New end-to-end features, multi-location, insights | Whole stack (`ui`, `domain`, `data`) | AndroidX, Retrofit, Room, JUnit | Phases 5, 10 |
| **`production-ai-integration`** | AI integration, structured prompts, JSON schemas, deterministic guardrails, fallback pipelines | AI weather summaries, activity recommendations | `domain/service`, `ui/viewmodel` | Gson, Room, JUnit | Phases 5, 6 |
| **`agentic-workflow-engineering`** | Tool definitions, permission boundaries, execution limits, approval checkpoints | Tool-using workflows, multi-step actions | `domain/service`, automation | Java Concurrency, Architecture components | Phases 5, 6 |
| **`intelligent-automation-engineering`** | Event-driven triggers, workflow state machines, retry policies, cooldowns, idempotency | Weather alerts, background triggers, notification rules | `data/local`, `domain/service` | Room, Handlers, Executors | Phases 5, 7 |
| **`ai-evaluation-and-guardrails`** | Empirical evaluation, schema validation scoring, adversarial testing, hallucination checks | AI benchmarking, prompt verification | `app/src/test/java` | JUnit 4, JSON fixtures | Phases 5, 9 |
| **`retrieval-and-knowledge-systems`** | Structured meteorological knowledge indexing, WMO interpretation, thermal comfort retrieval | Weather reference lookup, thermal indices | `domain/model`, `domain/service` | WMO standards, NOAA tables | Phases 5, 7 |
| **`third-party-integration-engineering`** | External REST APIs, Open-Meteo Geocoding, network resilience, contract testing | Geocoding API, weather forecast extensions | `data/remote`, `network` | Retrofit 2, OkHttp, Gson | Phases 5, 8 |
| **`iot-feature-engineering`** | Hardware footprint assessment, weather station telemetry contracts, simulated sensor testing | Personal weather station contracts, telemetry ingestion | `data/remote`, `domain/model` | JSON schemas, JUnit | Phases 5, 8 |
| **`edge-intelligence-engineering`** | On-device deterministic intelligence, thermal comfort formulas, activity suitability scoring | Thermal comfort, attire advice, activity scores | `domain/service`, `domain/model` | NOAA Steadman formula, Wind Chill | Phases 5, 7 |
| **`feature-rollout-and-observability`** | Feature flags, runtime toggles, error diagnostics, graceful gating, rollback | New capability rollouts, kill switches | `ui`, `data`, configuration | SharedPreferences, AndroidX | Phases 5, 8 |
| **`full-stack-feature-qa`** | End-to-end feature verification, automated unit tests, Room tests, regression suites | Test writing, verification gates | `app/src/test/java` | JUnit 4, Room In-Memory | Phases 5, 9, 10 |
| **`principal-security-audit`** | Security inventory, attack-surface discovery, threat modeling, vulnerability validation | Security audit, threat assessment, risk register | Entire project | Android Lint, Gradle, SAST | Phases 6, 10 |
| **`zero-trust-access-engineering`** | Identity-based access, service identity, least privilege, cryptographic boundaries | Data access, Keystore encryption, boundary checks | `domain/service`, `data/local` | AndroidKeyStore, AES-256-GCM | Phases 6, 7 |
| **`authentication-authorization-hardening`** | Authentication flows, token validation, credential vaults, component export gates | Component hardening, preference encryption | `AndroidManifest.xml`, `data/local` | AndroidKeyStore, Manifest gates | Phases 6, 8 |
| **`secure-api-and-backend`** | Input validation, certificate pinning, TLS transport, injection defense, redacted logging | HTTP client, OkHttp, network security config | `data/remote`, `network` | Retrofit 2, CertificatePinner, OkHttp | Phases 6, 8 |
| **`ai-agentic-security`** | Prompt injection defenses, retrieval isolation, hallucination bounding, memory safety | AI briefing, prompt security, structured validation | `domain/service`, `domain/model` | Regex guardrails, Gson, JUnit | Phases 6, 9 |
| **`database-privacy-security`** | SQL injection prevention, backup rules, data extraction defense, safe migrations | Room database, backup configuration, retention | `data/local`, `res/xml` | Room ORM, SQLite, XML rules | Phases 6, 7 |
| **`cloud-infrastructure-hardening`** | Cloud endpoint security, transport protections, cleartext traffic denial | Network security config, endpoint boundaries | `res/xml`, network client | NetworkSecurityConfig, HTTPS | Phases 6, 8 |
| **`software-supply-chain-security`** | Dependency assessment, secret scanning, R8 code obfuscation, lockfile integrity | Dependencies, ProGuard rules, build scripts | `build.gradle.kts`, `proguard-rules.pro` | R8, ProGuard, Version Catalog | Phases 6, 8 |
| **`authorized-security-testing`** | Scoped security testing, non-destructive testing, vulnerability reproduction | Test authoring, negative testing, retesting | `app/src/test/java` | JUnit 4, MockWebServer | Phases 6, 9 |
| **`iot-firmware-security`** | Device identity, PWS telemetry validation, sensor boundary enforcement, replay defense | Personal weather station contracts, telemetry checks | `domain/service`, `data/remote` | Sensor validators, Replay guards | Phases 6, 8 |
| **`security-observability-response`** | Security event logging, sensitive log redaction, incident response, audit trails | Error handling, log sanitization, audit logging | `ui`, `data/remote`, `domain` | Logging interceptors, Room DAOs | Phases 6, 8 |
| **`security-regression-engineering`** | Negative tests, authorization matrices, secure defaults, CI release gates | Automated security tests, regression protection | `app/src/test/java` | Gradle, JUnit 4, Android Lint | Phases 6, 9, 10 |


---

## Phase 7 — Performance, Scalability & Reliability Engineering Skills (Added in Phase 7)

| Skill Name | Path | Discipline | Operational Scope |
| :--- | :--- | :--- | :--- |
| **end-to-end-performance-engineering** | `.agents/skills/end-to-end-performance-engineering/SKILL.md` | Principal Performance Architect | End-to-end performance strategy, critical path tracing, workload profiling, and latency budgets |
| **frontend-runtime-optimization** | `.agents/skills/frontend-runtime-optimization/SKILL.md` | Principal Frontend Performance Engineer | Layout flattening, view inflation tuning, memory leak elimination, and 60/120fps frame budgets |
| **backend-throughput-optimization** | `.agents/skills/backend-throughput-optimization/SKILL.md` | Principal Backend Performance Engineer | OkHttp connection pooling, HTTP/2 multiplexing, JSON deserialization speedups, payload size |
| **database-query-performance** | `.agents/skills/database-query-performance/SKILL.md` | Principal Database Performance Engineer | Jetpack Room and SQLite query optimization, indexing strategy, transaction boundary tuning |
| **intelligent-caching-engineering** | `.agents/skills/intelligent-caching-engineering/SKILL.md` | Principal Caching Architect | Multi-tier caching architecture (L1 memory, L2 Room, HTTP disk cache), TTL management |
| **scalable-concurrency-engineering** | `.agents/skills/scalable-concurrency-engineering/SKILL.md` | Principal Distributed Systems Architect | Background thread management, thread pool sizing, bounded dispatchers, backpressure |
| **resilience-fault-tolerance** | `.agents/skills/resilience-fault-tolerance/SKILL.md` | Principal Site Reliability Engineer (SRE) | Circuit breaker pattern, exponential backoff with jitter, graceful offline degradation |
| **load-stress-soak-testing** | `.agents/skills/load-stress-soak-testing/SKILL.md` | Principal Performance QA Engineer | Synthetic load generation, concurrency stress tests, memory leak soak testing, capacity bounds |
| **production-slo-engineering** | `.agents/skills/production-slo-engineering/SKILL.md` | Principal SRE / Systems Architect | SLIs, SLOs, error budgets, latency percentile tracking, and performance observability |
| **ai-inference-efficiency** | `.agents/skills/ai-inference-efficiency/SKILL.md` | AI Inference Performance Engineer | Mathematical calculation memoization, floating-point optimization, token-free rule execution |
| **iot-edge-performance** | `.agents/skills/iot-edge-performance/SKILL.md` | IoT & Edge Performance Engineer | Telemetry batching, high-throughput micro-ingestion, sensor data sliding windows |
| **performance-security-regression** | `.agents/skills/performance-security-regression/SKILL.md` | Security Performance Reviewer | Cross-cutting verification ensuring optimizations preserve all Phase 6 security controls |

---

## Phase 8 — DevOps, CI/CD & Operational Excellence Engineering Skills (Added in Phase 8)

| Skill Name | Path | Discipline | Operational Scope |
| :--- | :--- | :--- | :--- |
| **cicd-pipeline-engineering** | `.agents/skills/cicd-pipeline-engineering/SKILL.md` | Principal CI/CD Architect | GitHub Actions Android workflows, job parallelization, Gradle caching, and lint/test gates |
| **release-automation-engineering** | `.agents/skills/release-automation-engineering/SKILL.md` | Principal Release Engineer | Tagged releases, R8 packaging, SHA-256 cryptographic digest generation, GitHub Releases |
| **reproducible-build-engineering** | `.agents/skills/reproducible-build-engineering/SKILL.md` | Principal DevOps Engineer | Containerized Android build environments, Dockerfile, docker-compose, and dependency pinning |
| **observability-and-sli-slo** | `.agents/skills/observability-and-sli-slo/SKILL.md` | Principal SRE / Observability Architect | HealthCheckService diagnostics, AppMetricsCollector SLI/SLO tracking, zero-allocation telemetry |
| **disaster-recovery-engineering** | `.agents/skills/disaster-recovery-engineering/SKILL.md` | Principal Reliability Engineer | SQLite corruption diagnostics, automated state restoration, ADB backup/restore tooling |
| **incident-management-runbooks** | `.agents/skills/incident-management-runbooks/SKILL.md` | Operations Lead / SRE | Incident triage, P0/P1 emergency response runbooks, rollout halting, forward hotfixes |
| **aimlops-lifecycle-operations** | `.agents/skills/aimlops-lifecycle-operations/SKILL.md` | MLOps Platform Engineer | Deterministic AI model operations, empirical rule validation gates, token-free inference testing |
| **iot-fleet-operations** | `.agents/skills/iot-fleet-operations/SKILL.md` | IoT Operations Specialist | Fleet telemetry operations, batch rate caps, sensor data quarantine, stream diagnostics |
| **devops-security-supply-chain** | `.agents/skills/devops-security-supply-chain/SKILL.md` | Security DevOps Architect | Supply chain security, dependency provenance, ProGuard/R8 code obfuscation, artifact signing |

---

## Phase 9 — Quality Engineering, Adversarial Verification & Release Certification Skills (Added in Phase 9)

| Skill Name | Path | Discipline | Operational Scope |
| :--- | :--- | :--- | :--- |
| **principal-qa-orchestration** | `.agents/skills/principal-qa-orchestration/SKILL.md` | Principal QA Architect | Risk-based test strategy, verification matrices, specialist delegation, defect prioritization |
| **comprehensive-browser-e2e** | `.agents/skills/comprehensive-browser-e2e/SKILL.md` | Principal Mobile/E2E SDET | End-to-end user journeys, screen routing, ViewBinding, ViewModel state, and navigation |
| **visual-motion-regression** | `.agents/skills/visual-motion-regression/SKILL.md` | Visual & Motion QA Specialist | Layout density testing, animation lifecycle validation, and layout hierarchy audit |
| **accessibility-verification** | `.agents/skills/accessibility-verification/SKILL.md` | Accessibility QA Lead | Android TalkBack semantics, WCAG 2.2 AA evaluation, 48dp touch targets, high contrast |
| **backend-api-contract-qa** | `.agents/skills/backend-api-contract-qa/SKILL.md` | Principal Backend QA Engineer | REST API contract testing, JSON schema validation, error taxonomy, negative payload tests |
| **database-integrity-qa** | `.agents/skills/database-integrity-qa/SKILL.md` | Database QA Architect | Room SQLite schema integrity, index performance, atomic transactions, migration compatibility |
| **adversarial-ai-evaluation** | `.agents/skills/adversarial-ai-evaluation/SKILL.md` | AI Red-Team Engineer | Meteorological polynomial edge validation, adversarial prompt injection red teaming, hallucination bounds |
| **workflow-distributed-qa** | `.agents/skills/workflow-distributed-qa/SKILL.md` | Automation QA Engineer | Event-driven automation, meteorological trigger validation, 60-min suppression cooldowns |
| **iot-firmware-verification** | `.agents/skills/iot-firmware-verification/SKILL.md` | IoT Verification Engineer | Weather station telemetry contract testing, physical bounds, anti-replay, batch limits |
| **chaos-resilience-verification** | `.agents/skills/chaos-resilience-verification/SKILL.md` | Chaos & Reliability Engineer | Fault injection, network flapping, upstream outage simulation, circuit breaker verification |
| **autonomous-defect-remediation** | `.agents/skills/autonomous-defect-remediation/SKILL.md` | Principal Remediation Engineer | Defect reproduction, root-cause diagnosis, minimal code patches, regression tests |
| **release-candidate-certification** | `.agents/skills/release-candidate-certification/SKILL.md` | Independent Release Quality Lead | Release candidate qualification, full-system audit, defect threshold gating, Go/No-Go assessment |

---

## Domain Specialist Activation Note
- **IoT Device Engineering**: *Deactivated / Omitted*. The reconnaissance audit confirmed this project is an Android mobile weather client with zero hardware, microcontroller, or embedded telemetry requirements. No artificial hardware requirements are introduced.
- **Visual Browser QA**: *Adapted for Android*. Converted into mobile layout and accessibility testing guidelines (`android-ui-ux-engineering`).
