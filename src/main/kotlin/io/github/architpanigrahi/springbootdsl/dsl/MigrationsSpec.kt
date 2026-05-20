package io.github.architpanigrahi.springbootdsl.dsl

import org.gradle.api.GradleException

class MigrationsSpec {
    internal var selectedEngine: MigrationEngine? = null
        private set

    fun flyway() {
        selectEngine(MigrationEngine.FLYWAY)
    }

    fun liquibase() {
        selectEngine(MigrationEngine.LIQUIBASE)
    }

    private fun selectEngine(engine: MigrationEngine) {
        val existingEngine = selectedEngine

        if (existingEngine != null && existingEngine != engine) {
            throw GradleException(
                "Only one migration engine can be selected. " +
                    "Already selected: ${existingEngine.displayName}. " +
                    "Attempted: ${engine.displayName}.",
            )
        }

        selectedEngine = engine
    }
}
