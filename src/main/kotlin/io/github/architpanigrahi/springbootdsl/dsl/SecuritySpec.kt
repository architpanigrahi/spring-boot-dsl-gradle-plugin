package io.github.architpanigrahi.springbootdsl.dsl

import io.github.architpanigrahi.springbootdsl.feature.FeatureRegistry
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SPRING_SECURITY

class SecuritySpec(
    private val featureRegistry: FeatureRegistry,
) {
    fun springSecurity() {
        featureRegistry.select(SPRING_SECURITY)
    }
}
