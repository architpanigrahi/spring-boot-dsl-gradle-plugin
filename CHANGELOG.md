# Changelog

All notable changes to this project are documented in this file.

## [0.1.0] - 2026-05-20

- Initial public release of `io.github.architpanigrahi.springbootdsl`.
- Added typed DSL blocks for `web`, `data`, `developerTools`, and `testing`.
- Added operations support with `operations { actuator() }`.
- Added security support with `security { springSecurity() }`.
- Added fail-fast validation for conflicting web stacks (`webMvc()` + `webFlux()`).
- Added Gradle TestKit functional test coverage.
- Added compatibility contract documentation.
