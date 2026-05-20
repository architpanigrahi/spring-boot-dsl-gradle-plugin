package io.github.architpanigrahi.springbootdsl.dsl

import io.github.architpanigrahi.springbootdsl.feature.FeatureRegistry
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.WEB_FLUX
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.WEB_MVC

class WebSpec(
    private val featureRegistry: FeatureRegistry
) {
    /**
     * Adds Spring Boot's Web MVC starter.
     */
    fun webMvc() {
        featureRegistry.select(WEB_MVC)
    }

    /**
     * Adds Spring Boot's WebFlux starter.
     */
    fun webFlux() {
        featureRegistry.select(WEB_FLUX)
    }
}