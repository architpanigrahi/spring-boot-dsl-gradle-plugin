# Architecture

## Goals

- Keep the DSL easy to memorize for new developers.
- Keep feature-to-dependency mapping explicit and deterministic.
- Keep validation failures immediate, actionable, and local to the selected block.
- Keep dependency reports auditable for build governance.

## End-to-End Flow

1. User configures `springBootPlugin { ... }`.
2. Block methods (for example `web { restClient() }`) select `SpringFeature` values.
3. `FeatureRegistry` stores selected features and triggers validation callbacks.
4. `FeatureValidator` applies rule objects on every selection.
5. `DependencyConfigurer` resolves `SpringFeature` to declarations from `DependencyCatalog`.
6. Dependencies are added to Gradle configurations with deduplication.
7. If `includeCompanionTests()` is enabled, companion test mappings are backfilled for already selected features.
8. On `afterEvaluate`, `springbootdsl-dependencies.txt` is generated at project root.

## Component Responsibilities

- `SpringBootPlugin` (`src/main/kotlin/io/github/architpanigrahi/springbootdsl/SpringBootPlugin.kt`)
  - Applies required base plugins.
  - Creates extension and wires callbacks.
  - Registers helper task (`springBootDslOptions`).
  - Triggers dependency reporting lifecycle hook.

- `SpringBootPluginExtension` (`src/main/kotlin/io/github/architpanigrahi/springbootdsl/SpringBootPluginExtension.kt`)
  - Defines canonical DSL block entry points.
  - Creates block spec objects and delegates feature selection.
  - Handles block-level validation preconditions such as required migration engine.

- `dsl/*Spec` classes
  - Provide the user-facing API methods.
  - Map each method to one (or intentionally grouped) feature selections.

- `FeatureRegistry` + `SpringFeature`
  - Central feature source-of-truth.
  - Emits selection changes to validators/configurers in a consistent order.

- `catalog/modules/*Dependencies.kt`
  - Domain-specific dependency declarations and companion test mappings.
  - Keeps changes localized when adding a new feature domain.

- `DependencyConfigurer`
  - Applies dependencies once per `(configuration, notation)` pair.
  - Tracks by-feature mappings for reporting.
  - Supports companion dependency late enablement.

- `validation/*Rule`
  - Encapsulate cross-feature constraints.
  - Throw with corrective guidance when combinations are invalid.

## Dependency Mapping Model

- Runtime feature dependencies live in module maps:
  - `webDependencies`, `clientDependencies`, `dataDependencies`, `securityDependencies`, `operationsDependencies`, `migrationDependencies`, `toolingDependencies`, `testingDependencies`.
- Companion test dependencies are separate maps in the same module files.
- `DependencyCatalog` merges all module maps into two indexes:
  - `dependenciesFor(feature)`
  - `companionTestDependenciesFor(feature)`

This split ensures:
- No hidden dependency side effects between runtime and test concerns.
- Easier review of test-starter additions.
- Safer future migration when Spring Boot dependency naming changes.

## Current Validation Rules

- Web server exclusivity:
  - `mvc()` and `reactiveServer()` cannot be selected together.
- Migration engine exclusivity:
  - `flyway()` and `liquibase()` cannot be selected together.
- Migration block requirement:
  - `migrations { ... }` must select exactly one engine (enforced in extension).
- JPA database requirement:
  - `data { jpa { ... } }` must choose one driver (enforced in JPA spec).

## Reporting and Observability

- Report file: `springbootdsl-dependencies.txt`
- Contents:
  - DSL feature labels
  - Applied dependencies per selected feature
  - Deduplicated final dependency list
- Helper task:
  - `springBootDslOptions`
  - `springBootDslOptions --block=<name>` for block-specific guidance.

## Testing Strategy

- Functional coverage (Gradle TestKit):
  - Plugin application and extension creation.
  - DSL task output/help behavior.
  - Validation failure scenarios.
  - Dependency declaration checks by configuration.
  - Dependency report generation checks.
  - Classpath dependency resolution checks for `restClient()` and `webClient()`.
- Smoke consumer:
  - Composite include build to verify plugin behavior from a consuming project shape.

## Extension Guidelines

When adding a DSL option:
1. Add/extend enum value in `SpringFeature`.
2. Add DSL method in the relevant `*Spec`.
3. Add runtime dependency mapping in a module map.
4. Add companion test mapping if required.
5. Add/adjust validation rule(s) if combination constraints exist.
6. Update report labeling in `DependencyConfigurer`.
7. Update README and changelog.
8. Add functional tests for success + invalid combinations.
