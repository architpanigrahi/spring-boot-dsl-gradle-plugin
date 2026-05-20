package io.github.architpanigrahi.springbootdsl.dsl

import io.github.architpanigrahi.springbootdsl.feature.FeatureRegistry
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.HTTP_CLIENT_REACTIVE
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.HTTP_CLIENT_REST
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.WEB_FLUX
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.WEB_MVC

class WebSpec(
    private val featureRegistry: FeatureRegistry,
) {
    fun mvc() {
        featureRegistry.select(WEB_MVC)
    }

    fun reactiveServer() {
        featureRegistry.select(WEB_FLUX)
    }

    fun restClient() {
        featureRegistry.select(HTTP_CLIENT_REST)
    }

    fun webClient() {
        featureRegistry.select(HTTP_CLIENT_REACTIVE)
    }
}
