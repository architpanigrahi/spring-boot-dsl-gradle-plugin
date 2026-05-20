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
        webMvc()
    }

    operations {
        actuator()
    }

    security {
        springSecurity()
    }

    databaseMigrations {
        flyway()
    }

    data {
        jpa {
            h2()
        }
        redis()
        mongodb()
    }

    developerTools {
        lombok()
    }

    testing {
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
    webMvc()
    webFlux()
}
```

### Data

```kotlin
data {
    jpa {
        postgres()
    }
    redis()
    mongodb()
}
```

Supported JPA database drivers:

- `postgres()`
- `mysql()`
- `h2()`

Additional data modules:

- `redis()`
- `mongodb()`

### Developer Tools

```kotlin
developerTools {
    lombok()
}
```

### Operations

```kotlin
operations {
    actuator()
}
```

### Security

```kotlin
security {
    springSecurity()
}
```

### Database Migrations

```kotlin
databaseMigrations {
    flyway()
}
```

Supported migration engines:

- `flyway()`
- `liquibase()`

### Testing

```kotlin
testing {
    springBootTest()
}
```

## Example

```kotlin
springBootPlugin {
    web {
        webMvc()
    }

    operations {
        actuator()
    }

    security {
        springSecurity()
    }

    databaseMigrations {
        flyway()
    }

    data {
        jpa {
            postgres()
        }
        redis()
    }

    developerTools {
        lombok()
    }

    testing {
        springBootTest()
    }
}
```

This configures a Spring Boot MVC application with Actuator, Security, JPA + PostgreSQL, Redis, Flyway, Lombok, and the Spring Boot test starter.

## Validation Behavior

- `data { jpa { ... } }` requires exactly one driver (`postgres()`, `mysql()`, or `h2()`).
- `webMvc()` and `webFlux()` are mutually exclusive and fail the build if selected together.
- `databaseMigrations { ... }` requires exactly one engine (`flyway()` or `liquibase()`).
- `flyway()` and `liquibase()` are mutually exclusive and fail the build if selected together.

## Contributor Workflow

- Follow `CONTRIBUTING.md` for mandatory feature implementation and validation rules.
- Run `./gradlew clean test ktlintCheck` before opening a pull request.

## Releases

The plugin follows semantic versioning.

- `0.1.0` — Initial public release

## Roadmap

Planned additions include:

- Testcontainers
- broader Spring Initializr-style coverage

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
