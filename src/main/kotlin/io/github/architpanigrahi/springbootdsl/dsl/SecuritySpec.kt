package io.github.architpanigrahi.springbootdsl.dsl

import io.github.architpanigrahi.springbootdsl.feature.FeatureRegistry
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SECURITY_OAUTH2_AUTH_SERVER
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SECURITY_OAUTH2_CLIENT
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SECURITY_OAUTH2_RESOURCE_SERVER
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SECURITY_SAML2
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SPRING_SECURITY

class SecuritySpec(
    private val featureRegistry: FeatureRegistry,
) {
    fun security() {
        featureRegistry.select(SPRING_SECURITY)
    }

    fun oauth2ResourceServer() {
        featureRegistry.select(SECURITY_OAUTH2_RESOURCE_SERVER)
    }

    fun oauth2Client() {
        featureRegistry.select(SECURITY_OAUTH2_CLIENT)
    }

    fun oauth2AuthServer() {
        featureRegistry.select(SECURITY_OAUTH2_AUTH_SERVER)
    }

    fun saml2() {
        featureRegistry.select(SECURITY_SAML2)
    }

    @Deprecated(
        message = "Use oauth2ResourceServer() for clearer semantics.",
        replaceWith = ReplaceWith("oauth2ResourceServer()"),
    )
    fun jwt() {
        oauth2ResourceServer()
    }
}
