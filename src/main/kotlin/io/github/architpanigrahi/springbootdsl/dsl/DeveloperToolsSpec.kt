package io.github.architpanigrahi.springbootdsl.dsl

import io.github.architpanigrahi.springbootdsl.feature.FeatureRegistry
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.LOMBOK

class DeveloperToolsSpec(
    private val featureRegistry: FeatureRegistry,
) {
    /**
     * Adds Lombok as a compile-only dependency and annotation processor.
     */
    fun lombok() {
        featureRegistry.select(LOMBOK)
    }
}
