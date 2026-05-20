# Spring Boot DSL Gradle Plugin

A concise, typed Gradle DSL for configuring common **Spring Boot 4+** application dependencies.

Instead of repeating standard Spring Boot plugin and dependency declarations across projects, define the capabilities your application needs in a cleaner, developer-friendly format.

## Usage

```kotlin
plugins {
    id("io.github.architpanigrahi.springbootdsl") version "0.1.0"
}

repositories {
    mavenCentral()
}

springPlugin {
    web {
        webMvc()
    }

    data {
        jpa {
            h2()
        }
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
}
```

Supported JPA database drivers:

- `postgres()`
- `mysql()`
- `h2()`

### Developer Tools

```kotlin
developerTools {
    lombok()
}
```

### Testing

```kotlin
testing {
    springBootTest()
}
```

## Example

```kotlin
springPlugin {
    web {
        webMvc()
    }

    data {
        jpa {
            postgres()
        }
    }

    developerTools {
        lombok()
    }

    testing {
        springBootTest()
    }
}
```

This configures a Spring Boot MVC application with JPA, PostgreSQL, Lombok, and the standard Spring Boot test starter.

## Releases

The plugin follows semantic versioning.

- `0.1.0` — Initial public release

Future releases will expand support for additional Spring Boot features while keeping the DSL clean and predictable.

## Roadmap

Planned additions include:

- MongoDB and Redis
- Flyway and Liquibase
- Spring Security
- Testcontainers
- Actuator
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
