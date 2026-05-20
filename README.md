# Spring Boot DSL Gradle Plugin

A developer-friendly Gradle plugin for concise Spring Boot 4+ application setup through a typed DSL.

Instead of writing repetitive plugin and dependency declarations manually, define the capabilities your application needs:

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