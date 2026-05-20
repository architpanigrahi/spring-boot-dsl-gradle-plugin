# Spring Boot DSL Gradle Plugin

A concise, typed Gradle DSL for configuring common **Spring Boot 4+** application dependencies.

Instead of repeating standard Spring Boot plugin and dependency declarations across projects, define the capabilities your application needs in a cleaner, developer-friendly format.

## Design Contract

- One DSL method maps to one explicit capability.
- Dependency selection is deterministic and has no hidden side effects.
- Invalid feature combinations fail fast with corrective guidance.
- New features follow the module pattern documented in `docs/ARCHITECTURE.md`.

## Compatibility

| Component | Supported |
| --- | --- |
| Plugin version | `0.1.1` |
| Gradle | `9.2.1+` |
| Java toolchain | `21` |
| Spring Boot line | `4.0.x` |
| Kotlin DSL | Gradle-embedded Kotlin (`2.2.x` on Gradle 9.2.1) |

## Usage

```kotlin
plugins {
    id("io.github.architpanigrahi.springbootdsl") version "0.1.1"
}

repositories {
    mavenCentral()
}

springBootPlugin {
    web {
        mvc()
        restClient()
        webClient()
    }

    ops {
        actuator()
    }

    auth {
        security()
        jwt()
        oauth2Client()
    }

    migrations {
        flyway()
    }

    data {
        jpa {
            h2()
        }
        redis()
        mongo()
    }

    devTools {
        lombok()
    }

    test {
        includeCompanionTests()
        springBootTest()
    }
}
```

The plugin internally applies:

- Java plugin
- Spring Boot Gradle plugin
- Spring dependency management plugin

## Current DSL Support

### Web

```kotlin
web {
    mvc()
    // or reactiveServer()

    restClient()
    webClient()
}
```

- `mvc()` selects Spring MVC server starter.
- `reactiveServer()` selects WebFlux server starter.
- `restClient()` adds the blocking HTTP client stack.
- `webClient()` adds the reactive HTTP client stack.

### Data

```kotlin
data {
    jpa {
        postgres()
    }
    redis()
    mongo()
}
```

Supported JPA database drivers:

- `postgres()`
- `mysql()`
- `h2()`

Additional data modules:

- `redis()`
- `mongo()`

### Developer Tools

```kotlin
devTools {
    lombok()
}
```

### Operations

```kotlin
ops {
    actuator()
}
```

### Auth

```kotlin
auth {
    security()
    jwt()
    oauth2Client()
}
```

- `security()` adds `spring-boot-starter-security`.
- `jwt()` adds `spring-boot-starter-oauth2-resource-server`.
- `oauth2Client()` adds `spring-boot-starter-oauth2-client`.

### Database Migrations

```kotlin
migrations {
    flyway()
}
```

Supported migration engines:

- `flyway()`
- `liquibase()`

### Testing

```kotlin
test {
    includeCompanionTests()
    springBootTest()
}
```

- `includeCompanionTests()` enables companion test dependencies for selected runtime features.

Companion test dependencies (when enabled):

- `web.mvc()` -> `spring-boot-starter-webmvc-test`
- `web.reactiveServer()` -> `spring-boot-starter-webflux-test`
- `web.restClient()` -> `spring-boot-starter-webmvc-test`
- `web.webClient()` -> `spring-boot-starter-webflux-test`
- `auth.security()` -> `spring-boot-starter-security-test`
- `auth.jwt()` -> `spring-boot-starter-security-oauth2-resource-server-test`
- `auth.oauth2Client()` -> `spring-boot-starter-security-oauth2-client-test`
- `data.jpa()` -> `spring-boot-starter-data-jpa-test`
- `data.redis()` -> `spring-boot-starter-data-redis-test`
- `data.mongo()` -> `spring-boot-starter-data-mongodb-test`
- `ops.actuator()` -> `spring-boot-starter-actuator-test`
- `migrations.flyway()` / `migrations.liquibase()` -> `spring-boot-starter-test`

## Block Naming

- Canonical blocks are `web`, `data`, `auth`, `ops`, `migrations`, `devTools`, and `test`.
- This plugin now uses a single canonical naming scheme to reduce onboarding confusion.

## Validation Behavior

- `data { jpa { ... } }` requires exactly one driver (`postgres()`, `mysql()`, or `h2()`).
- `mvc()` and `reactiveServer()` are mutually exclusive server options.
- `restClient()` and `webClient()` can be selected together.
- `migrations { ... }` requires exactly one engine (`flyway()` or `liquibase()`).
- `flyway()` and `liquibase()` are mutually exclusive and fail the build if selected together.
- Companion test dependencies are only added when `includeCompanionTests()` is selected.

## Dependency Report Output

When the plugin is configured, it generates a root-level file:

- `springbootdsl-dependencies.txt`

This report includes:

- DSL feature selections (for example `web { mvc() }`)
- Dependencies mapped for each selected feature
- Deduplicated list of all dependencies applied by the plugin

The plugin also prints the report path in the Gradle console.

## Helper Task

Run:

```bash
./gradlew springBootDslOptions
```

This task is in the Gradle **Help** group and prints a full `springBootPlugin { ... }` template with all available options.

For in-depth help for one block:

```bash
./gradlew springBootDslOptions --block=web
./gradlew springBootDslOptions --block=data
./gradlew springBootDslOptions --block=auth
./gradlew springBootDslOptions --block=ops
./gradlew springBootDslOptions --block=migrations
./gradlew springBootDslOptions --block=devTools
./gradlew springBootDslOptions --block=test
```

## Contributor Workflow

- Follow `CONTRIBUTING.md` for mandatory feature implementation and validation rules.
- Run `./gradlew clean test ktlintCheck` before opening a pull request.

## Releases

The plugin follows semantic versioning.

- `0.1.1` — Canonical DSL naming, auth expansion, companion test mappings, and dependency report output.
- `0.1.0` — Initial public release

## Local Development

Build the plugin:

```bash
./gradlew build
```

Test it from another local Gradle project using a composite build:

```kotlin
pluginManagement {
    includeBuild("../spring-boot-dsl-gradle-plugin")
}
```

Then apply it without a version:

```kotlin
plugins {
    id("io.github.architpanigrahi.springbootdsl")
}
```

## License

MIT
