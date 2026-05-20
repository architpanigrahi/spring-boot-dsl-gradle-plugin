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
| Plugin version | `0.1.0` |
| Gradle | `9.2.1+` |
| Java toolchain | `21` |
| Spring Boot line | `4.0.x` |
| Kotlin DSL | Gradle-embedded Kotlin (`2.2.x` on Gradle 9.2.1) |

## Usage

```kotlin
plugins {
    id("io.github.architpanigrahi.springbootdsl") version "0.1.0"
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
- `oauth2Login()` is an alias for `oauth2Client()`.

Compatibility alias block:
- `security { ... }` still works and maps to `auth { ... }`.

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
    springBootTest()
}
```

## Block Naming

- `web`, `data`, `auth`, `ops`, `migrations`, `devTools`, and `test` are the preferred block names.
- Legacy aliases are kept for backward compatibility:
  - `security { ... }` -> `auth { ... }`
  - `operations { ... }` -> `ops { ... }`
  - `databaseMigrations { ... }` -> `migrations { ... }`
  - `developerTools { ... }` -> `devTools { ... }`
  - `testing { ... }` -> `test { ... }`

## Validation Behavior

- `data { jpa { ... } }` requires exactly one driver (`postgres()`, `mysql()`, or `h2()`).
- `mvc()` and `reactiveServer()` are mutually exclusive server options.
- `restClient()` and `webClient()` can be selected together.
- `migrations { ... }` requires exactly one engine (`flyway()` or `liquibase()`).
- `flyway()` and `liquibase()` are mutually exclusive and fail the build if selected together.

## Contributor Workflow

- Follow `CONTRIBUTING.md` for mandatory feature implementation and validation rules.
- Run `./gradlew clean test ktlintCheck` before opening a pull request.

## Releases

The plugin follows semantic versioning.

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
