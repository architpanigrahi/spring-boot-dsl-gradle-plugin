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
        assertTrue(result.output.contains("reactiveServer()"))
        assertTrue(result.output.contains("Use: ./gradlew springBootDslOptions --block=<name>"))
        assertTrue(!result.output.contains("Choose one server stack"))
    }

    @Test
    fun `prints in-depth block help for web`() {
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

        val result = runGradle("springBootDslOptions", "--block=web")

        assertEquals(TaskOutcome.SUCCESS, result.task(":springBootDslOptions")?.outcome)
        assertTrue(result.output.contains("Block: web"))
        assertTrue(result.output.contains("mvc() and reactiveServer() are mutually exclusive."))
        assertTrue(result.output.contains("restClient() and webClient() can be used together."))
    }

    @Test
    fun `prints in-depth block help for all blocks`() {
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

        val blockExpectations =
            mapOf(
                "data" to "Block: data",
                "auth" to "Block: auth",
                "ops" to "Block: ops",
                "migrations" to "Block: migrations",
                "devTools" to "Block: devTools",
                "test" to "Block: test",
            )

        blockExpectations.forEach { (block, expectedHeader) ->
            val result = runGradle("springBootDslOptions", "--block=$block")

            assertEquals(TaskOutcome.SUCCESS, result.task(":springBootDslOptions")?.outcome)
            assertTrue(result.output.contains(expectedHeader), "Expected block header for --block=$block")
        }
    }

    @Test
    fun `prints unknown block guidance`() {
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

        val result = runGradle("springBootDslOptions", "--block=unknown")

        assertEquals(TaskOutcome.SUCCESS, result.task(":springBootDslOptions")?.outcome)
        assertTrue(result.output.contains("Unknown block: unknown"))
        assertTrue(result.output.contains("Valid blocks: web, data, auth, ops, migrations, devTools, test"))
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
                    check(hasDependency("implementation", "spring-boot-starter-actuator"))
                    check(hasDependency("implementation", "spring-boot-starter-security"))
                    check(hasDependency("implementation", "spring-boot-starter-oauth2-resource-server"))
                    check(hasDependency("implementation", "spring-boot-starter-oauth2-client"))
                    check(hasDependency("implementation", "flyway-core"))
                    check(hasDependency("implementation", "spring-boot-starter-data-jpa"))
                    check(hasDependency("implementation", "spring-boot-starter-data-redis"))
                    check(hasDependency("implementation", "spring-boot-starter-data-mongodb"))
                    check(hasDependency("implementation", "spring-boot-starter-restclient"))
                    check(hasDependency("implementation", "spring-boot-starter-webclient"))
                    check(hasDependency("runtimeOnly", "h2"))
                    check(hasDependency("runtimeOnly", "spring-boot-h2console"))
                    check(hasDependency("compileOnly", "lombok"))
                    check(hasDependency("annotationProcessor", "lombok"))
                    check(hasDependency("testImplementation", "spring-boot-starter-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-webmvc-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-restclient-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-webclient-test"))
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
        assertTrue(
            result.output.contains("web { mvc(); webClient() } or web { reactiveServer(); restClient() }."),
            "Expected canonical web combination guidance in build output",
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

                    check(hasDependency("implementation", "spring-boot-starter-restclient"))
                    check(hasDependency("implementation", "spring-boot-starter-webclient"))
                }
            }
            """.trimIndent(),
        )

        val result = runGradle("verifyClientDependencies")

        assertEquals(TaskOutcome.SUCCESS, result.task(":verifyClientDependencies")?.outcome)
    }

    @Test
    fun `shows guidance when only web clients are selected`() {
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
            """.trimIndent(),
        )

        val result = runGradle("help")

        assertTrue(
            result.output.contains("selects HTTP clients only and does not select a server stack."),
            "Expected client-only web guidance warning in output",
        )
        assertTrue(
            result.output.contains("web { mvc(); webClient() } or web { reactiveServer(); restClient() }."),
            "Expected canonical server combination guidance in output",
        )
    }

    @Test
    fun `does not show client-only guidance when server stack is selected`() {
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
            }
            """.trimIndent(),
        )

        val result = runGradle("help")

        assertTrue(
            result.output.contains("selects HTTP clients only and does not select a server stack.").not(),
            "Did not expect client-only guidance when a server stack is selected",
        )
    }

    @Test
    fun `resolves rest and reactive client dependencies on classpaths`() {
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
                test {
                    includeCompanionTests()
                }
            }

            tasks.register("verifyClientResolution") {
                doLast {
                    val runtimeArtifacts = configurations.getByName("runtimeClasspath")
                        .resolvedConfiguration
                        .resolvedArtifacts
                        .map { "${'$'}{it.moduleVersion.id.group}:${'$'}{it.name}" }
                        .toSet()

                    check(runtimeArtifacts.contains("org.springframework.boot:spring-boot-starter-restclient"))
                    check(runtimeArtifacts.contains("org.springframework.boot:spring-boot-starter-webclient"))

                    val testRuntimeArtifacts = configurations.getByName("testRuntimeClasspath")
                        .resolvedConfiguration
                        .resolvedArtifacts
                        .map { "${'$'}{it.moduleVersion.id.group}:${'$'}{it.name}" }
                        .toSet()

                    check(testRuntimeArtifacts.contains("org.springframework.boot:spring-boot-starter-restclient-test"))
                    check(testRuntimeArtifacts.contains("org.springframework.boot:spring-boot-starter-webclient-test"))
                }
            }
            """.trimIndent(),
        )

        val result = runGradle("verifyClientResolution")

        assertEquals(TaskOutcome.SUCCESS, result.task(":verifyClientResolution")?.outcome)
    }

    @Test
    fun `adds macOS native netty resolver for webClient on arm64`() {
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
                    webClient()
                }
            }

            tasks.register("verifyMacOsNettyResolver") {
                doLast {
                    val osName = System.getProperty("os.name").orEmpty().lowercase()
                    val osArch = System.getProperty("os.arch").orEmpty().lowercase()
                    val shouldInclude = osName.contains("mac") && (osArch.contains("aarch64") || osArch.contains("arm64"))

                    val nettyDependency =
                        configurations.getByName("runtimeOnly")
                            .dependencies
                            .withType(org.gradle.api.artifacts.ExternalModuleDependency::class.java)
                            .find { dependency ->
                                dependency.group == "io.netty" && dependency.name == "netty-resolver-dns-native-macos"
                            }

                    if (shouldInclude) {
                        check(nettyDependency != null)
                        check(nettyDependency!!.artifacts.any { artifact -> artifact.classifier == "osx-aarch_64" })
                    } else {
                        check(nettyDependency == null)
                    }
                }
            }
            """.trimIndent(),
        )

        val result = runGradle("verifyMacOsNettyResolver")

        assertEquals(TaskOutcome.SUCCESS, result.task(":verifyMacOsNettyResolver")?.outcome)
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
                    check(hasDependency("testImplementation", "spring-boot-starter-restclient-test").not())
                    check(hasDependency("testImplementation", "spring-boot-starter-webclient-test").not())
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

                    check(hasDependency("testImplementation", "spring-boot-starter-restclient-test"))
                    check(hasDependency("testImplementation", "spring-boot-starter-webclient-test"))
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
        assertTrue(report.contains("implementation: org.springframework.boot:spring-boot-starter-webclient"))
        assertTrue(report.contains("testImplementation: org.springframework.boot:spring-boot-starter-webclient-test"))
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
