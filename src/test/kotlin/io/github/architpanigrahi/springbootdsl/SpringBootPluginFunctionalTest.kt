package io.github.architpanigrahi.springbootdsl

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

class SpringBootPluginFunctionalTest {
    @TempDir
    lateinit var projectDir: Path

    @Test
    fun `creates extension and applies required plugins`() {
        writeBuildFile(
            """
            plugins {
                id("io.github.architpanigrahi.springbootdsl")
            }

            repositories {
                mavenCentral()
            }

            tasks.register("verifyPluginSetup") {
                doLast {
                    check(project.extensions.findByName("springBootPlugin") != null) {
                        "springBootPlugin extension was not created"
                    }
                    check(project.pluginManager.hasPlugin("java")) {
                        "java plugin was not applied"
                    }
                    check(project.pluginManager.hasPlugin("org.springframework.boot")) {
                        "org.springframework.boot plugin was not applied"
                    }
                    check(project.pluginManager.hasPlugin("io.spring.dependency-management")) {
                        "io.spring.dependency-management plugin was not applied"
                    }
                }
            }
            """.trimIndent(),
        )

        val result = runGradle("verifyPluginSetup")

        assertEquals(TaskOutcome.SUCCESS, result.task(":verifyPluginSetup")?.outcome)
    }

    @Test
    fun `prints dsl options task output`() {
        writeBuildFile(
            """
            plugins {
                id("io.github.architpanigrahi.springbootdsl")
            }

            repositories {
                mavenCentral()
            }
            """.trimIndent(),
        )

        val result = runGradle("springBootDslOptions")

        assertEquals(TaskOutcome.SUCCESS, result.task(":springBootDslOptions")?.outcome)
        assertTrue(result.output.contains("springBootPlugin {"))
        assertTrue(result.output.contains("web {"))
        assertTrue(result.output.contains("auth {"))
        assertTrue(result.output.contains("includeCompanionTests()"))
    }

    @Test
    fun `adds dependencies for selected DSL features`() {
        writeBuildFile(
            """
            plugins {
                id("io.github.architpanigrahi.springbootdsl")
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

            tasks.register("verifyDependencies") {
                doLast {
                    fun hasDependency(configurationName: String, dependencyName: String): Boolean =
                        configurations.getByName(configurationName).dependencies.any { dependency ->
                            dependency.name == dependencyName
                        }

                    check(hasDependency("implementation", "spring-boot-starter-webmvc"))
                    check(hasDependency("implementation", "spring-boot-starter-webflux"))
                    check(hasDependency("implementation", "spring-boot-starter-actuator"))
                    check(hasDependency("implementation", "spring-boot-starter-security"))
                    check(hasDependency("implementation", "spring-boot-starter-oauth2-resource-server"))
                    check(hasDependency("implementation", "spring-boot-starter-oauth2-client"))
                    check(hasDependency("implementation", "flyway-core"))
                    check(hasDependency("implementation", "spring-boot-starter-data-jpa"))
                    check(hasDependency("implementation", "spring-boot-starter-data-redis"))
                    check(hasDependency("implementation", "spring-boot-starter-data-mongodb"))
                    check(hasDependency("runtimeOnly", "h2"))
                    check(hasDependency("runtimeOnly", "spring-boot-h2console"))
                    check(hasDependency("compileOnly", "lombok"))
                    check(hasDependency("annotationProcessor", "lombok"))
                    check(hasDependency("testImplementation", "spring-boot-starter-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-webmvc-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-webflux-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-security-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-security-oauth2-client-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-security-oauth2-resource-server-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-data-jpa-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-data-redis-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-data-mongodb-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-actuator-test"))
                }
            }
            """.trimIndent(),
        )

        val result = runGradle("verifyDependencies")

        assertEquals(TaskOutcome.SUCCESS, result.task(":verifyDependencies")?.outcome)
    }

    @Test
    fun `fails when jpa is configured without a database`() {
        writeBuildFile(
            """
            plugins {
                id("io.github.architpanigrahi.springbootdsl")
            }

            repositories {
                mavenCentral()
            }

            springBootPlugin {
                data {
                    jpa {
                    }
                }
            }
            """.trimIndent(),
        )

        val result = runGradleAndFail("help")

        assertTrue(
            result.output.contains("data { jpa { ... } } requires a database driver."),
            "Expected JPA configuration error in build output",
        )
    }

    @Test
    fun `fails when both mvc and webflux are selected`() {
        writeBuildFile(
            """
            plugins {
                id("io.github.architpanigrahi.springbootdsl")
            }

            repositories {
                mavenCentral()
            }

            springBootPlugin {
                web {
                    mvc()
                    reactiveServer()
                }
            }
            """.trimIndent(),
        )

        val result = runGradleAndFail("help")

        assertTrue(
            result.output.contains("mvc() and reactiveServer() cannot be selected together."),
            "Expected mutually exclusive web stack validation error",
        )
    }

    @Test
    fun `fails when migrations block has no engine`() {
        writeBuildFile(
            """
            plugins {
                id("io.github.architpanigrahi.springbootdsl")
            }

            repositories {
                mavenCentral()
            }

            springBootPlugin {
                migrations {
                }
            }
            """.trimIndent(),
        )

        val result = runGradleAndFail("help")

        assertTrue(
            result.output.contains("migrations { ... } requires an engine."),
            "Expected migration engine requirement error",
        )
    }

    @Test
    fun `fails when both flyway and liquibase are selected`() {
        writeBuildFile(
            """
            plugins {
                id("io.github.architpanigrahi.springbootdsl")
            }

            repositories {
                mavenCentral()
            }

            springBootPlugin {
                migrations {
                    flyway()
                }
                migrations {
                    liquibase()
                }
            }
            """.trimIndent(),
        )

        val result = runGradleAndFail("help")

        assertTrue(
            result.output.contains("flyway() and liquibase() cannot be selected together."),
            "Expected mutually exclusive migration engine validation error",
        )
    }

    @Test
    fun `allows both rest and reactive clients`() {
        writeBuildFile(
            """
            plugins {
                id("io.github.architpanigrahi.springbootdsl")
            }

            repositories {
                mavenCentral()
            }

            springBootPlugin {
                web {
                    restClient()
                    webClient()
                }
            }
            
            tasks.register("verifyClientDependencies") {
                doLast {
                    fun hasDependency(configurationName: String, dependencyName: String): Boolean =
                        configurations.getByName(configurationName).dependencies.any { dependency ->
                            dependency.name == dependencyName
                        }

                    check(hasDependency("implementation", "spring-boot-starter-webmvc"))
                    check(hasDependency("implementation", "spring-boot-starter-webflux"))
                }
            }
            """.trimIndent(),
        )

        val result = runGradle("verifyClientDependencies")

        assertEquals(TaskOutcome.SUCCESS, result.task(":verifyClientDependencies")?.outcome)
    }

    @Test
    fun `does not add companion test dependencies unless enabled`() {
        writeBuildFile(
            """
            plugins {
                id("io.github.architpanigrahi.springbootdsl")
            }

            repositories {
                mavenCentral()
            }

            springBootPlugin {
                web {
                    restClient()
                    webClient()
                }
                auth {
                    security()
                    jwt()
                    oauth2Client()
                }
            }

            tasks.register("verifyNoCompanionTests") {
                doLast {
                    fun hasDependency(configurationName: String, dependencyName: String): Boolean =
                        configurations.getByName(configurationName).dependencies.any { dependency ->
                            dependency.name == dependencyName
                        }

                    check(hasDependency("testImplementation", "spring-boot-starter-webmvc-test").not())
                    check(hasDependency("testImplementation", "spring-boot-starter-webflux-test").not())
                    check(hasDependency("testImplementation", "spring-boot-starter-security-test").not())
                }
            }
            """.trimIndent(),
        )

        val result = runGradle("verifyNoCompanionTests")

        assertEquals(TaskOutcome.SUCCESS, result.task(":verifyNoCompanionTests")?.outcome)
    }

    @Test
    fun `adds companion tests when enabled after selecting features`() {
        writeBuildFile(
            """
            plugins {
                id("io.github.architpanigrahi.springbootdsl")
            }

            repositories {
                mavenCentral()
            }

            springBootPlugin {
                web {
                    restClient()
                    webClient()
                }
                auth {
                    security()
                }
                test {
                    includeCompanionTests()
                }
            }

            tasks.register("verifyLateCompanionEnable") {
                doLast {
                    fun hasDependency(configurationName: String, dependencyName: String): Boolean =
                        configurations.getByName(configurationName).dependencies.any { dependency ->
                            dependency.name == dependencyName
                        }

                    check(hasDependency("testImplementation", "spring-boot-starter-webmvc-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-webflux-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-security-test"))
                }
            }
            """.trimIndent(),
        )

        val result = runGradle("verifyLateCompanionEnable")

        assertEquals(TaskOutcome.SUCCESS, result.task(":verifyLateCompanionEnable")?.outcome)
    }

    @Test
    fun `writes dependency report file with feature mappings`() {
        writeBuildFile(
            """
            plugins {
                id("io.github.architpanigrahi.springbootdsl")
            }

            repositories {
                mavenCentral()
            }

            springBootPlugin {
                web {
                    mvc()
                    webClient()
                }
                auth {
                    security()
                    jwt()
                }
                test {
                    includeCompanionTests()
                }
            }
            """.trimIndent(),
        )

        runGradle("help")

        val reportFile = projectDir.resolve("springbootdsl-dependencies.txt")
        assertTrue(reportFile.exists(), "Expected springbootdsl-dependencies.txt to be created")

        val report = reportFile.readText()
        assertTrue(report.contains("web { mvc() }"))
        assertTrue(report.contains("web { webClient() }"))
        assertTrue(report.contains("auth { security() }"))
        assertTrue(report.contains("auth { jwt() }"))
        assertTrue(report.contains("implementation: org.springframework.boot:spring-boot-starter-webmvc"))
        assertTrue(report.contains("implementation: org.springframework.boot:spring-boot-starter-webflux"))
        assertTrue(report.contains("testImplementation: org.springframework.boot:spring-boot-starter-webflux-test"))
    }

    private fun writeBuildFile(contents: String) {
        projectDir.resolve("settings.gradle.kts").writeText("rootProject.name = \"test-project\"")
        projectDir.resolve("build.gradle.kts").writeText(contents)
        projectDir.resolve("src/main/java").createDirectories()
    }

    private fun runGradle(vararg arguments: String) =
        GradleRunner.create()
            .withProjectDir(projectDir.toFile())
            .withArguments(*arguments, "--stacktrace")
            .withPluginClasspath()
            .build()

    private fun runGradleAndFail(vararg arguments: String) =
        GradleRunner.create()
            .withProjectDir(projectDir.toFile())
            .withArguments(*arguments, "--stacktrace")
            .withPluginClasspath()
            .buildAndFail()
}
