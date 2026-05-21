# Release Process

## Pre-release Checklist

1. Update version in `build.gradle.kts`.
2. Move `Unreleased` entries in `CHANGELOG.md` into a versioned section with date.
3. Verify compatibility matrix in `README.md` is accurate.
4. Run:
   - `./gradlew verifyCatalogCoordinates`
   - `./gradlew clean test ktlintCheck`
5. Verify CI status is green for all Java matrix jobs.
6. Validate smoke consumer build path in CI.

## Publishing Notes

- Plugin id: `io.github.architpanigrahi.springbootdsl`
- Publishing metadata and plugin portal settings live in `build.gradle.kts`.
- Publish runs as the final pipeline stage on pushes to `main` only when the `version =` line changes in `build.gradle.kts`.
- Keep release notes focused on:
  - added DSL capabilities
  - validation behavior changes
  - compatibility changes
