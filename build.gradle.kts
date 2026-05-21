plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "2.0.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.3.0"
}

group = "io.github.architpanigrahi"
version = "0.1.4"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

// Version Numbering - Configurable Later
val springBootVersion = "4.0.0"
val dependencyManagementPluginVersion = "1.1.7"

dependencies {
    implementation("org.springframework.boot:org.springframework.boot.gradle.plugin:$springBootVersion")
    implementation("io.spring.dependency-management:io.spring.dependency-management.gradle.plugin:$dependencyManagementPluginVersion")
    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter:5.13.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.13.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.check {
    dependsOn("ktlintCheck")
}

tasks.register("verifyCatalogCoordinates") {
    group = "Verification"
    description = "Resolves all dependency coordinates referenced by plugin catalog mappings."

    doLast {
        val catalogCoordinates =
            listOf(
                "org.springframework.boot:spring-boot-starter-webmvc",
                "org.springframework.boot:spring-boot-starter-webflux",
                "org.springframework.boot:spring-boot-starter-restclient",
                "org.springframework.boot:spring-boot-starter-webclient",
                "org.springframework.boot:spring-boot-starter-data-jpa",
                "org.postgresql:postgresql",
                "com.mysql:mysql-connector-j",
                "com.h2database:h2",
                "org.springframework.boot:spring-boot-h2console",
                "org.springframework.boot:spring-boot-starter-data-redis",
                "org.springframework.boot:spring-boot-starter-data-mongodb",
                "org.springframework.boot:spring-boot-starter-actuator",
                "org.springframework.boot:spring-boot-starter-security",
                "org.springframework.boot:spring-boot-starter-oauth2-client",
                "org.springframework.boot:spring-boot-starter-oauth2-resource-server",
                "org.flywaydb:flyway-core",
                "org.liquibase:liquibase-core",
                "org.projectlombok:lombok",
                "org.springframework.boot:spring-boot-starter-test",
                "org.springframework.boot:spring-boot-starter-webmvc-test",
                "org.springframework.boot:spring-boot-starter-webflux-test",
                "org.springframework.boot:spring-boot-starter-restclient-test",
                "org.springframework.boot:spring-boot-starter-webclient-test",
                "org.springframework.boot:spring-boot-starter-data-jpa-test",
                "org.springframework.boot:spring-boot-starter-data-redis-test",
                "org.springframework.boot:spring-boot-starter-data-mongodb-test",
                "org.springframework.boot:spring-boot-starter-actuator-test",
                "org.springframework.boot:spring-boot-starter-security-test",
                "org.springframework.boot:spring-boot-starter-security-oauth2-client-test",
                "org.springframework.boot:spring-boot-starter-security-oauth2-resource-server-test",
            ).distinct()

        val failedCoordinates = mutableListOf<String>()

        catalogCoordinates.forEach { coordinate ->
            try {
                val resolvableNotation =
                    if (coordinate.startsWith("org.springframework.boot:")) {
                        "$coordinate:$springBootVersion"
                    } else {
                        "$coordinate:+"
                    }
                val detachedConfiguration =
                    configurations.detachedConfiguration(
                        dependencies.create(resolvableNotation),
                    ).apply {
                        isTransitive = false
                    }
                detachedConfiguration.resolve()
            } catch (exception: Exception) {
                val rootCause =
                    generateSequence(exception as Throwable?) { throwable -> throwable?.cause }
                        .lastOrNull()
                failedCoordinates += "$coordinate -> ${rootCause?.message ?: exception.message}"
            }
        }

        if (failedCoordinates.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("Failed to resolve one or more catalog coordinates:")
                    failedCoordinates.forEach { failure ->
                        appendLine("- $failure")
                    }
                },
            )
        }

        logger.lifecycle("Verified ${catalogCoordinates.size} catalog coordinates.")
    }
}

gradlePlugin {
    website = "https://github.com/architpanigrahi/spring-boot-dsl-gradle-plugin"
    vcsUrl = "https://github.com/architpanigrahi/spring-boot-dsl-gradle-plugin"

    plugins {
        create("springBootPlugin") {
            id = "io.github.architpanigrahi.springbootdsl"
            implementationClass = "io.github.architpanigrahi.springbootdsl.SpringBootPlugin"
            displayName = "Spring Boot DSL Gradle Plugin"
            description = "A developer-friendly Gradle plugin for concise Spring Boot 4+ application setup through a typed DSL."
            tags.set(
                listOf(
                    "spring",
                    "spring-boot",
                    "gradle-plugin",
                    "convention-plugin",
                    "kotlin-dsl",
                ),
            )
        }
    }
}
