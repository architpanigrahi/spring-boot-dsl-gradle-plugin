plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "2.0.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.3.0"
}

group = "io.github.architpanigrahi"
version = "0.1.1"

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
