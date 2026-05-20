# Changelog

All notable changes to this project are documented in this file.

## [Unreleased]

- Added `data { redis() }` and `data { mongodb() }`.
- Added `databaseMigrations { flyway() | liquibase() }`.
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
- Added fail-fast validation for conflicting web stacks (`webMvc()` + `webFlux()`).
- Added Gradle TestKit functional test coverage.
- Added compatibility contract documentation.
