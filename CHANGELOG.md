# Changelog

All notable changes to this project are documented in this file.

## [Unreleased]

- Added `data { redis() }` and `data { mongodb() }`.
- Added `databaseMigrations { flyway() | liquibase() }`.
- Added web-oriented HTTP clients DSL under `web { ... }` with `restClient()` and `webClient()`.
- Added auth capabilities under `auth { ... }`: `security()`, `jwt()`, `oauth2Client()` and alias `oauth2Login()`.
- Documented preferred block names and legacy aliases for backward compatibility.
- Added strict migration-engine exclusivity validation.
- Modularized dependency catalog for migration and data module expansion.
- Added governance docs: `CONTRIBUTING.md` and `docs/ARCHITECTURE.md`.
- Added `ktlint` quality gate and CI enforcement.
- Expanded functional tests for new success/failure paths.

## [0.1.0] - 2026-05-20

- Initial public release of `io.github.architpanigrahi.springbootdsl`.
- Added typed DSL blocks for `web`, `data`, `developerTools`, and `testing`.
- Added operations support with `operations { actuator() }`.
- Added security support with `security { springSecurity() }`.
- Added fail-fast validation for conflicting web stacks (`mvc()` + `reactiveServer()`).
- Added Gradle TestKit functional test coverage.
- Added compatibility contract documentation.
