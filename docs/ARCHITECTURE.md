# Architecture

## Core Flow

1. User calls DSL methods inside `springBootPlugin { ... }`.
2. DSL methods select `SpringFeature` flags through `FeatureRegistry`.
3. `DependencyConfigurer` maps selected features to dependencies via `DependencyCatalog`.
4. `FeatureValidator` executes rules and fails fast when combinations are invalid.

## Package Responsibilities

- `dsl/`: user-facing API and local selection semantics
- `feature/`: canonical feature flags and selection registry
- `catalog/modules/`: dependency declarations grouped by domain
- `validation/`: cross-feature rules and guardrails
- `dependency/`: generic Gradle dependency wiring primitives

## Feature Domains

- `web`: MVC or WebFlux
- `data`: JPA + JDBC drivers, Redis, MongoDB
- `operations`: Actuator
- `security`: Spring Security
- `migrations`: Flyway or Liquibase
- `tooling`: Lombok
- `testing`: Spring Boot test starter

## Validation Model

Validation runs each time a feature is selected.

- `WebStackCombinationExclusiveRule`: prevents `webMvc()` + `webFlux()`
- `MigrationEngineExclusiveRule`: prevents `flyway()` + `liquibase()`

Rules must be deterministic, explicit, and throw `GradleException` with corrective guidance.
