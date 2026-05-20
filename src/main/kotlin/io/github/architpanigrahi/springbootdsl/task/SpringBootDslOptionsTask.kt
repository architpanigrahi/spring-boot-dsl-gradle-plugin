package io.github.architpanigrahi.springbootdsl.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class SpringBootDslOptionsTask : DefaultTask() {
    init {
        group = "Help"
        description = "Prints all Spring Boot DSL block options."
    }

    @TaskAction
    fun printDslOptions() {
        logger.quiet(
            """
            springBootPlugin {
                web {
                    // Choose one server stack:
                    mvc()
                    // reactiveServer()

                    // Optional HTTP clients:
                    restClient()
                    webClient()
                }

                data {
                    // Optional stores:
                    redis()
                    mongo()

                    // Optional relational stack:
                    // jpa {
                    //     postgres() // or mysql() or h2()
                    // }
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
                    // Choose one:
                    flyway()
                    // liquibase()
                }

                devTools {
                    lombok()
                }

                test {
                    // Optional companion test dependencies:
                    includeCompanionTests()
                    springBootTest()
                }
            }
            """.trimIndent(),
        )
    }
}
