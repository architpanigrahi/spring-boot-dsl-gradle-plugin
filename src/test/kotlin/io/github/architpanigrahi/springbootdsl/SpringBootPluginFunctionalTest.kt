package io.github.architpanigrahi.springbootdsl

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.createDirectories
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

            tasks.register("verifyDependencies") {
                doLast {
                    fun hasDependency(configurationName: String, dependencyName: String): Boolean =
                        configurations.getByName(configurationName).dependencies.any { dependency ->
                            dependency.name == dependencyName
                        }

                    check(hasDependency("implementation", "spring-boot-starter-webmvc"))
                    check(hasDependency("implementation", "spring-boot-starter-actuator"))
                    check(hasDependency("implementation", "spring-boot-starter-security"))
                    check(hasDependency("implementation", "flyway-core"))
                    check(hasDependency("implementation", "spring-boot-starter-data-jpa"))
                    check(hasDependency("implementation", "spring-boot-starter-data-redis"))
                    check(hasDependency("implementation", "spring-boot-starter-data-mongodb"))
                    check(hasDependency("runtimeOnly", "h2"))
                    check(hasDependency("runtimeOnly", "spring-boot-h2console"))
                    check(hasDependency("compileOnly", "lombok"))
                    check(hasDependency("annotationProcessor", "lombok"))
                    check(hasDependency("testImplementation", "spring-boot-starter-test"))
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
                    webMvc()
                    webFlux()
                }
            }
            """.trimIndent(),
        )

        val result = runGradleAndFail("help")

        assertTrue(
            result.output.contains("webMvc() and webFlux() cannot be selected together."),
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
                databaseMigrations {
                }
            }
            """.trimIndent(),
        )

        val result = runGradleAndFail("help")

        assertTrue(
            result.output.contains("databaseMigrations { ... } requires an engine."),
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
                databaseMigrations {
                    flyway()
                }
                databaseMigrations {
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
