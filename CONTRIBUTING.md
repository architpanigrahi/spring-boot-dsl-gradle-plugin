# Contributing

## Scope

This plugin is a typed dependency DSL for Spring Boot projects. Contributions must preserve:

- concise user-facing DSL syntax
- deterministic dependency wiring
- explicit, fail-fast validation for invalid combinations

## Compatibility Contract

- Gradle: `9.2.1+`
- Java toolchain: `21`
- Spring Boot line: `4.0.x`

Changes that impact compatibility must update `README.md` and `CHANGELOG.md`.

## DSL Design Rules

- One DSL method represents one feature capability.
- DSL methods must use explicit names (`actuator()`, `springSecurity()`, `redis()`).
- Do not introduce hidden runtime behavior beyond plugin/dependency wiring.
- Invalid combinations must fail with actionable `GradleException` messages.

## Feature Implementation Pattern

Every new feature must include all of the following:

1. `SpringFeature` enum entry
2. DSL method in `dsl/` package
3. Dependency mapping in `catalog/modules/`
4. Validation rule in `validation/` if constraints exist
5. Functional test coverage in `SpringBootPluginFunctionalTest`
6. Documentation updates in `README.md` and `CHANGELOG.md`

## Code Style and Quality Gates

- Kotlin style is enforced with `ktlint`.
- CI fails if `ktlintCheck` or tests fail.
- Keep code ASCII unless existing file already requires otherwise.
- Keep implementation focused and avoid unrelated refactors.

## Pull Request Checklist

- [ ] Added/updated DSL entry points with clear naming
- [ ] Added dependency mapping in the correct module file
- [ ] Added or updated validation rules
- [ ] Added functional test coverage for success and failure paths
- [ ] Updated `README.md` usage and compatibility docs
- [ ] Updated `CHANGELOG.md`
- [ ] Ran `./gradlew clean test ktlintCheck`
