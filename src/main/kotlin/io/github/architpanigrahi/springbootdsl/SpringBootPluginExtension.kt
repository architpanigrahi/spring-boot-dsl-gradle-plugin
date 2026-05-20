package io.github.architpanigrahi.springbootdsl

import io.github.architpanigrahi.springbootdsl.dsl.DataSpec
import io.github.architpanigrahi.springbootdsl.dsl.DeveloperToolsSpec
import io.github.architpanigrahi.springbootdsl.dsl.OperationsSpec
import io.github.architpanigrahi.springbootdsl.dsl.SecuritySpec
import io.github.architpanigrahi.springbootdsl.dsl.TestingSpec
import io.github.architpanigrahi.springbootdsl.dsl.WebSpec
import io.github.architpanigrahi.springbootdsl.feature.FeatureRegistry
import org.gradle.api.Action

open class SpringBootPluginExtension(
    private val featureRegistry: FeatureRegistry
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

    fun operations(action: Action<OperationsSpec>) {
        action.execute(OperationsSpec(featureRegistry))
    }

    fun security(action: Action<SecuritySpec>) {
        action.execute(SecuritySpec(featureRegistry))
    }
}
