package io.github.architpanigrahi.springbootdsl

import io.github.architpanigrahi.springbootdsl.dsl.DataSpec
import io.github.architpanigrahi.springbootdsl.dsl.DeveloperToolsSpec
import io.github.architpanigrahi.springbootdsl.dsl.MigrationsSpec
import io.github.architpanigrahi.springbootdsl.dsl.OperationsSpec
import io.github.architpanigrahi.springbootdsl.dsl.SecuritySpec
import io.github.architpanigrahi.springbootdsl.dsl.TestingSpec
import io.github.architpanigrahi.springbootdsl.dsl.WebSpec
import io.github.architpanigrahi.springbootdsl.feature.FeatureRegistry
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature
import org.gradle.api.Action
import org.gradle.api.GradleException

open class SpringBootPluginExtension(
    private val featureRegistry: FeatureRegistry,
    private val onIncludeCompanionTests: (Set<SpringFeature>) -> Unit,
) {
    fun web(action: Action<WebSpec>) {
        action.execute(WebSpec(featureRegistry))
    }

    fun data(action: Action<DataSpec>) {
        action.execute(DataSpec(featureRegistry))
    }

    fun devTools(action: Action<DeveloperToolsSpec>) {
        action.execute(DeveloperToolsSpec(featureRegistry))
    }

    fun test(action: Action<TestingSpec>) {
        action.execute(
            TestingSpec(
                featureRegistry = featureRegistry,
                onIncludeCompanionTests = {
                    onIncludeCompanionTests(featureRegistry.selectedFeatures())
                },
            ),
        )
    }

    fun ops(action: Action<OperationsSpec>) {
        action.execute(OperationsSpec(featureRegistry))
    }

    fun auth(action: Action<SecuritySpec>) {
        action.execute(SecuritySpec(featureRegistry))
    }

    fun migrations(action: Action<MigrationsSpec>) {
        val migrationsSpec = MigrationsSpec()
        action.execute(migrationsSpec)

        val selectedEngine =
            migrationsSpec.selectedEngine
                ?: throw GradleException(
                    "migrations { ... } requires an engine. " +
                        "For example: migrations { flyway() }",
                )

        featureRegistry.select(selectedEngine.feature)
    }
}
