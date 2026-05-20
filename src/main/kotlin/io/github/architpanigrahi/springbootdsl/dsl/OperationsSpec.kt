package io.github.architpanigrahi.springbootdsl.dsl

import io.github.architpanigrahi.springbootdsl.feature.FeatureRegistry
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SPRING_BOOT_ACTUATOR

class OperationsSpec(
    private val featureRegistry: FeatureRegistry,
) {
    fun actuator() {
        featureRegistry.select(SPRING_BOOT_ACTUATOR)
    }
}
