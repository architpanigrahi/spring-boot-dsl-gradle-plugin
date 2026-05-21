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
| Plugin version | `0.1.4` |
| Gradle | `9.2.1+` |
| Java toolchain | `21` |
| Spring Boot line | `4.0.x` |
| Kotlin DSL | Gradle-embedded Kotlin (`2.2.x` on Gradle 9.2.1) |

## Usage

```kotlin
plugins {
    id("io.github.architpanigrahi.springbootdsl") version "0.1.4"
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
- On macOS ARM64, `webClient()` also adds `runtimeOnly("io.netty:netty-resolver-dns-native-macos")` with classifier `osx-aarch_64`.

Canonical server + client combinations:

```kotlin
// MVC server + WebClient
web {
    mvc()
    webClient()
}
```

```kotlin
// Reactive server + RestClient
web {
    reactiveServer()
    restClient()
}
```

```kotlin
// Client-only project (no embedded server selected)
web {
    restClient()
    webClient()
}
```

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
- `web.restClient()` -> `spring-boot-starter-restclient-test`
- `web.webClient()` -> `spring-boot-starter-webclient-test`
- `auth.security()` -> `spring-boot-starter-security-test`
- `auth.jwt()` -> `spring-boot-starter-security-oauth2-resource-server-test`
- `auth.oauth2Client()` -> `spring-boot-starter-security-oauth2-client-test`
- `data.jpa()` -> `spring-boot-starter-data-jpa-test`
- `data.redis()` -> `spring-boot-starter-data-redis-test`
- `data.mongo()` -> `spring-boot-starter-data-mongodb-test`
- `ops.actuator()` -> `spring-boot-starter-actuator-test`
- `migrations.flyway()` / `migrations.liquibase()` -> `spring-boot-starter-test`

## Capability Matrix

| DSL Capability | Runtime Dependency | Companion Test Dependency (`includeCompanionTests()`) | Notes |
| --- | --- | --- | --- |
| `web.mvc()` | `org.springframework.boot:spring-boot-starter-webmvc` | `org.springframework.boot:spring-boot-starter-webmvc-test` | Mutually exclusive with `reactiveServer()` |
| `web.reactiveServer()` | `org.springframework.boot:spring-boot-starter-webflux` | `org.springframework.boot:spring-boot-starter-webflux-test` | Mutually exclusive with `mvc()` |
| `web.restClient()` | `org.springframework.boot:spring-boot-starter-restclient` | `org.springframework.boot:spring-boot-starter-restclient-test` | Can be used with either server stack or client-only |
| `web.webClient()` | `org.springframework.boot:spring-boot-starter-webclient` | `org.springframework.boot:spring-boot-starter-webclient-test` | Can be used with either server stack or client-only |
| `data.jpa()` | `org.springframework.boot:spring-boot-starter-data-jpa` | `org.springframework.boot:spring-boot-starter-data-jpa-test` | Requires one driver via `postgres()` / `mysql()` / `h2()` |
| `data.jpa { postgres() }` | `org.postgresql:postgresql` | — | Driver only |
| `data.jpa { mysql() }` | `com.mysql:mysql-connector-j` | — | Driver only |
| `data.jpa { h2() }` | `com.h2database:h2`, `org.springframework.boot:spring-boot-h2console` | — | H2 runtime + console |
| `data.redis()` | `org.springframework.boot:spring-boot-starter-data-redis` | `org.springframework.boot:spring-boot-starter-data-redis-test` | Optional module |
| `data.mongo()` | `org.springframework.boot:spring-boot-starter-data-mongodb` | `org.springframework.boot:spring-boot-starter-data-mongodb-test` | Optional module |
| `auth.security()` | `org.springframework.boot:spring-boot-starter-security` | `org.springframework.boot:spring-boot-starter-security-test` | Base security stack |
| `auth.jwt()` | `org.springframework.boot:spring-boot-starter-oauth2-resource-server` | `org.springframework.boot:spring-boot-starter-security-oauth2-resource-server-test` | JWT resource server |
| `auth.oauth2Client()` | `org.springframework.boot:spring-boot-starter-oauth2-client` | `org.springframework.boot:spring-boot-starter-security-oauth2-client-test` | OAuth2 client |
| `ops.actuator()` | `org.springframework.boot:spring-boot-starter-actuator` | `org.springframework.boot:spring-boot-starter-actuator-test` | Ops endpoints |
| `migrations.flyway()` | `org.flywaydb:flyway-core` | `org.springframework.boot:spring-boot-starter-test` | Exactly one migration engine required |
| `migrations.liquibase()` | `org.liquibase:liquibase-core` | `org.springframework.boot:spring-boot-starter-test` | Exactly one migration engine required |
| `devTools.lombok()` | `org.projectlombok:lombok` (`compileOnly`, `annotationProcessor`) | — | Build-time only |
| `test.springBootTest()` | — | `org.springframework.boot:spring-boot-starter-test` | Explicit test stack |

## Block Naming

- Canonical blocks are `web`, `data`, `auth`, `ops`, `migrations`, `devTools`, and `test`.
- This plugin now uses a single canonical naming scheme to reduce onboarding confusion.

## Validation Behavior

- `data { jpa { ... } }` requires exactly one driver (`postgres()`, `mysql()`, or `h2()`).
- `mvc()` and `reactiveServer()` are mutually exclusive server options.
- `restClient()` and `webClient()` can be selected together.
- Selecting only `restClient()`/`webClient()` is valid and treated as client-only configuration.
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

- `0.1.4` — macOS ARM64 `webClient()` Netty DNS native resolver auto-inclusion.
- `0.1.3` — Web/client diagnostics improvements, capability matrix, all-block deep-help tests, and catalog coordinate verification task.
- `0.1.2` — DSL options task deep-help mode, corrected client starter coordinates, and expanded architecture/roadmap docs.
- `0.1.1` — Canonical DSL naming, auth expansion, companion test mappings, and dependency report output.
- `0.1.0` — Initial public release

## Project Docs

- Architecture: `docs/ARCHITECTURE.md`
- Roadmap: `docs/ROADMAP.md`
- Release process: `docs/RELEASE.md`

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
