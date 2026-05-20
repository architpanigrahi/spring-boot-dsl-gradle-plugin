package io.github.architpanigrahi.springbootdsl

import io.github.architpanigrahi.springbootdsl.dsl.DataSpec
import io.github.architpanigrahi.springbootdsl.dsl.DeveloperToolsSpec
import io.github.architpanigrahi.springbootdsl.dsl.MigrationsSpec
import io.github.architpanigrahi.springbootdsl.dsl.OperationsSpec
import io.github.architpanigrahi.springbootdsl.dsl.SecuritySpec
import io.github.architpanigrahi.springbootdsl.dsl.TestingSpec
import io.github.architpanigrahi.springbootdsl.dsl.WebSpec
import io.github.architpanigrahi.springbootdsl.feature.FeatureRegistry
import org.gradle.api.Action
import org.gradle.api.GradleException

open class SpringBootPluginExtension(
    private val featureRegistry: FeatureRegistry,
) {
    fun web(action: Action<WebSpec>) {
        action.execute(WebSpec(featureRegistry))
    }

    fun data(action: Action<DataSpec>) {
        action.execute(DataSpec(featureRegistry))
    }

    fun developerTools(action: Action<DeveloperToolsSpec>) {
        action.execute(DeveloperToolsSpec(featureRegistry))
    }

    fun testing(action: Action<TestingSpec>) {
        action.execute(TestingSpec(featureRegistry))
    }

    fun test(action: Action<TestingSpec>) {
        testing(action)
    }

    fun operations(action: Action<OperationsSpec>) {
        action.execute(OperationsSpec(featureRegistry))
    }

    fun ops(action: Action<OperationsSpec>) {
        operations(action)
    }

    fun security(action: Action<SecuritySpec>) {
        action.execute(SecuritySpec(featureRegistry))
    }

    fun auth(action: Action<SecuritySpec>) {
        security(action)
    }

    fun databaseMigrations(action: Action<MigrationsSpec>) {
        val migrationsSpec = MigrationsSpec()
        action.execute(migrationsSpec)

        val selectedEngine =
            migrationsSpec.selectedEngine
                ?: throw GradleException(
                    "databaseMigrations { ... } requires an engine. " +
                        "For example: databaseMigrations { flyway() }",
                )

        featureRegistry.select(selectedEngine.feature)
    }

    fun migrations(action: Action<MigrationsSpec>) {
        databaseMigrations(action)
    }

    fun devTools(action: Action<DeveloperToolsSpec>) {
        developerTools(action)
    }
}
