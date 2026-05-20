package io.github.architpanigrahi.springbootdsl.dsl

import io.github.architpanigrahi.springbootdsl.feature.FeatureRegistry
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SECURITY_OAUTH2_CLIENT
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SECURITY_OAUTH2_RESOURCE_SERVER
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SPRING_SECURITY

class SecuritySpec(
    private val featureRegistry: FeatureRegistry,
) {
    fun security() {
        featureRegistry.select(SPRING_SECURITY)
    }

    fun springSecurity() {
        security()
    }

    fun jwt() {
        featureRegistry.select(SECURITY_OAUTH2_RESOURCE_SERVER)
    }

    fun oauth2Client() {
        featureRegistry.select(SECURITY_OAUTH2_CLIENT)
    }

    fun oauth2Login() {
        oauth2Client()
    }
}
