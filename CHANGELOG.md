# Changelog

All notable changes to this project are documented in this file.

## [0.1.1] - 2026-05-20

- Added `data { redis() }` and `data { mongo() }`.
- Added `migrations { flyway() | liquibase() }`.
- Added web-oriented HTTP clients DSL under `web { ... }` with `restClient()` and `webClient()`.
- Added auth capabilities under `auth { ... }`: `security()`, `jwt()`, and `oauth2Client()`.
- Finalized canonical block naming (`web`, `data`, `auth`, `ops`, `migrations`, `devTools`, `test`) and removed legacy aliases.
- Added companion test dependency support with opt-in toggle `test { includeCompanionTests() }`.
- Switched companion test mappings to feature-specific Spring Boot 4 test starters where available.
- Added plugin dependency report output file `springbootdsl-dependencies.txt` with DSL-to-dependency mapping details.
- Added Gradle console message showing the generated report file path.
- Added strict migration-engine exclusivity validation.
- Modularized dependency catalog for migration and data module expansion.
- Added governance docs: `CONTRIBUTING.md` and `docs/ARCHITECTURE.md`.
- Added `ktlint` quality gate and staged CI pipeline.
- Expanded functional tests for new success/failure/reporting paths.

## [0.1.0] - 2026-05-20

- Initial public release of `io.github.architpanigrahi.springbootdsl`.
- Added typed DSL blocks for `web`, `data`, `developerTools`, and `testing`.
- Added operations support with `operations { actuator() }`.
- Added security support with `security { springSecurity() }`.
- Added fail-fast validation for conflicting web stacks (`mvc()` + `reactiveServer()`).
- Added Gradle TestKit functional test coverage.
- Added compatibility contract documentation.
