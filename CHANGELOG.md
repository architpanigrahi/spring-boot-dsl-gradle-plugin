# Changelog

All notable changes to this project are documented in this file.

## [Unreleased]

## [0.1.4] - 2026-05-21

- Added automatic macOS ARM64 runtime dependency for `webClient()`:
  - `io.netty:netty-resolver-dns-native-macos` with classifier `osx-aarch_64`

## [0.1.3] - 2026-05-21

- Added improved web diagnostics for client-only selections (`restClient()`/`webClient()`) with explicit server-vs-client guidance.
- Expanded web conflict validation messaging with canonical server+client combinations.
- Added README examples for:
  - MVC server + WebClient
  - Reactive server + RestClient
  - Client-only projects
- Added README capability matrix mapping DSL methods to runtime and companion test dependencies.
- Added maintainer task `verifyCatalogCoordinates` to resolve and verify all catalog dependency coordinates before release.
- Expanded `springBootDslOptions --block=<name>` functional coverage across all supported blocks.

## [0.1.2] - 2026-05-21

- Enhanced helper task `springBootDslOptions` (group: `Help`) to print an uncommented full DSL template with all options.
- Added `springBootDslOptions --block=<name>` for in-depth, block-specific help (`web`, `data`, `auth`, `ops`, `migrations`, `devTools`, `test`).
- Added resolution-level functional coverage to validate `restClient()` and `webClient()` dependency coordinates on runtime/test classpaths.
- Corrected client dependency mappings to use dedicated Spring Boot 4 client starters:
  - `restClient()` -> `spring-boot-starter-restclient`
  - `webClient()` -> `spring-boot-starter-webclient`
  - companion tests -> `spring-boot-starter-restclient-test` and `spring-boot-starter-webclient-test`
- Expanded architecture documentation and added a detailed roadmap document for feature growth planning.
- Expanded functional tests for new success/failure/reporting/help-task paths.

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

## [0.1.0] - 2026-05-20

- Initial public release of `io.github.architpanigrahi.springbootdsl`.
- Added typed DSL blocks for `web`, `data`, `developerTools`, and `testing`.
- Added operations support with `operations { actuator() }`.
- Added security support with `security { springSecurity() }`.
- Added fail-fast validation for conflicting web stacks (`mvc()` + `reactiveServer()`).
- Added Gradle TestKit functional test coverage.
- Added compatibility contract documentation.
