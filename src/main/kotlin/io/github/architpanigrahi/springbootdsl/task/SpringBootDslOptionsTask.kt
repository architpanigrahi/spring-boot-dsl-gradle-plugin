package io.github.architpanigrahi.springbootdsl.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

abstract class SpringBootDslOptionsTask : DefaultTask() {
    @get:Input
    @get:Optional
    var selectedBlock: String? = null

    init {
        group = "Help"
        description = "Prints Spring Boot DSL options. Use --block=<name> for in-depth block help."
    }

    @Option(option = "block", description = "Show in-depth help for one block (web, data, auth, ops, migrations, devTools, test).")
    fun setRequestedBlock(value: String) {
        selectedBlock = value
    }

    @TaskAction
    fun printDslOptions() {
        val requestedBlock = selectedBlock?.trim()
        if (requestedBlock.isNullOrEmpty()) {
            logger.quiet(fullTemplate())
            logger.quiet("")
            logger.quiet("Use: ./gradlew springBootDslOptions --block=<name>")
            logger.quiet("Valid blocks: web, data, auth, ops, migrations, devTools, test")
            return
        }

        logger.quiet(blockHelp(requestedBlock))
    }

    private fun fullTemplate(): String =
        """
        springBootPlugin {
            web {
                mvc()
                reactiveServer()
                restClient()
                webClient()
            }

            data {
                jpa {
                    postgres()
                    mysql()
                    h2()
                }
                redis()
                mongo()
            }

            auth {
                security()
                jwt()
                oauth2Client()
            }

            ops {
                actuator()
            }

            migrations {
                flyway()
                liquibase()
            }

            devTools {
                lombok()
            }

            test {
                includeCompanionTests()
                springBootTest()
            }
        }
        """.trimIndent()

    private fun blockHelp(name: String): String =
        when (name.lowercase()) {
            "web" ->
                """
                Block: web
                Purpose: HTTP server stack and HTTP client stack.
                Options:
                - mvc() -> Adds spring-boot-starter-webmvc (server).
                - reactiveServer() -> Adds spring-boot-starter-webflux (server).
                - restClient() -> Adds spring-boot-starter-restclient (blocking client stack).
                - webClient() -> Adds spring-boot-starter-webclient (reactive client stack).
                Rules:
                - mvc() and reactiveServer() are mutually exclusive.
                - restClient() and webClient() can be used together.
                """.trimIndent()
            "data" ->
                """
                Block: data
                Purpose: Data access modules.
                Options:
                - jpa { postgres() | mysql() | h2() } -> Relational stack and selected JDBC driver/runtime support.
                - redis() -> Adds spring-boot-starter-data-redis.
                - mongo() -> Adds spring-boot-starter-data-mongodb.
                Rules:
                - jpa { ... } requires one of postgres(), mysql(), or h2().
                """.trimIndent()
            "auth" ->
                """
                Block: auth
                Purpose: Application authentication and authorization.
                Options:
                - security() -> Adds spring-boot-starter-security.
                - jwt() -> Adds spring-boot-starter-oauth2-resource-server.
                - oauth2Client() -> Adds spring-boot-starter-oauth2-client.
                """.trimIndent()
            "ops" ->
                """
                Block: ops
                Purpose: Operational capabilities.
                Options:
                - actuator() -> Adds spring-boot-starter-actuator.
                """.trimIndent()
            "migrations" ->
                """
                Block: migrations
                Purpose: Database schema migration tooling.
                Options:
                - flyway() -> Adds flyway-core.
                - liquibase() -> Adds liquibase-core.
                Rules:
                - Exactly one migration engine must be selected.
                """.trimIndent()
            "devtools" ->
                """
                Block: devTools
                Purpose: Development productivity dependencies.
                Options:
                - lombok() -> Adds lombok for compileOnly and annotationProcessor.
                """.trimIndent()
            "test" ->
                """
                Block: test
                Purpose: Test dependency selection.
                Options:
                - springBootTest() -> Adds spring-boot-starter-test.
                - includeCompanionTests() -> Adds feature-specific test starters for selected runtime features.
                """.trimIndent()
            else ->
                """
                Unknown block: $name
                Valid blocks: web, data, auth, ops, migrations, devTools, test
                Usage: ./gradlew springBootDslOptions --block=<name>
                """.trimIndent()
        }
}
