# Roadmap

## Scope

This roadmap keeps two constraints in balance:

- **Simplicity first** for onboarding and day-1 use.
- **Extensibility next** for larger production projects.

## Current Baseline (0.1.x)

- Canonical single-level blocks: `web`, `data`, `auth`, `ops`, `migrations`, `devTools`, `test`.
- Focused nested block only for `data { jpa { ... } }`.
- Deterministic dependency mapping and fail-fast validation.
- Optional companion test dependency system.
- Dependency report generation and DSL options helper task.

## Near-Term (0.2.x)

### 1) Web and Client Ergonomics

- Keep current `web` API stable:
  - `mvc()`, `reactiveServer()`, `restClient()`, `webClient()`.
- Add improved diagnostics for common confusion:
  - Explain server-vs-client selection when only clients are used.
  - Suggest canonical combinations in validation messages.
- Add usage examples for:
  - MVC server + WebClient
  - Reactive server + RestClient
  - Client-only projects

### 2) Security Expansion

- Completed in `0.1.x`:
  - Added `oauth2ResourceServer()` naming for resource server semantics.
  - Added `oauth2AuthServer()` and `saml2()`.
  - Added companion test mappings for the above auth options.
  - Added guidance warning when advanced auth options are selected without `security()`.
- Next security items:
  - clarify recommended auth combinations in troubleshooting docs.
  - add advanced auth smoke scenarios in CI.

### 3) Data and Messaging Additions

- Add commonly requested stores and integration modules:
  - `r2dbc()`
  - `kafka()`
  - `amqp()`
- Add explicit test companion mappings where official starter-test modules exist.

### 4) Observability and Ops

- Expand `ops` for production readiness:
  - `prometheus()`
  - `tracing()`
- Keep one-method-per-capability rule.

### 5) Documentation Quality

- Add “capability matrix” table in README:
  - DSL method
  - dependency notation
  - companion test mapping
  - compatibility notes
- Add migration notes per release line.

## Mid-Term (0.3.x)

### 1) Compatibility and Version Policy

- Add explicit Spring Boot line policy (for example `4.0.x` vs `4.1.x` support windows).
- Add compatibility tests against multiple Gradle minors.
- Add a small dependency validation task for maintainers that resolves all catalog artifacts.

### 2) Better Developer Guidance

- Extend `springBootDslOptions` with:
  - optional compact mode (`--format=compact`)
  - optional mapping mode (`--show-dependencies`) to print DSL -> artifact mapping
- Keep defaults beginner-friendly and short.

### 3) Internal Architecture Hardening

- Extract dependency report formatter into a dedicated component.
- Add rule test fixtures for easier validation-rule additions.
- Add regression tests for deduplication and late companion enablement edge cases.

## Long-Term (1.0.0)

### 1) API Stability Contract

- Freeze canonical block/method names.
- Provide deprecation windows for future API changes.
- Publish compatibility guarantees for:
  - plugin API
  - generated dependency report format
  - helper task CLI contract

### 2) Enterprise Readiness

- Add optional BOM/version governance helpers.
- Add optional convention presets (strictly opt-in).
- Add release automation and signed artifact checks.

## Non-Goals

- No deeply nested DSL hierarchies beyond clear necessity.
- No implicit bundle modes that hide concrete dependencies.
- No dynamic behavior that changes dependency outcomes unpredictably.

## Release Process Checkpoints

For each release:

1. Validate new dependency coordinates by resolution tests.
2. Validate all DSL options with functional tests.
3. Update README capability matrix and examples.
4. Update `CHANGELOG.md` under `Unreleased`, then cut release section.
5. Tag and publish with semantic versioning discipline.
