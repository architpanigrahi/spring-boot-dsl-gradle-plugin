package io.github.architpanigrahi.springbootdsl.validation

import io.github.architpanigrahi.springbootdsl.feature.SpringFeature
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SECURITY_OAUTH2_AUTH_SERVER
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SECURITY_OAUTH2_CLIENT
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SECURITY_OAUTH2_RESOURCE_SERVER
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SECURITY_SAML2
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SPRING_SECURITY
import org.gradle.api.logging.Logger

object AuthAdvancedWithoutSecurityGuidanceRule : FeatureValidationRule {
    private val advancedAuthFeatures =
        setOf(
            SECURITY_OAUTH2_CLIENT,
            SECURITY_OAUTH2_RESOURCE_SERVER,
            SECURITY_OAUTH2_AUTH_SERVER,
            SECURITY_SAML2,
        )

    override fun validate(
        newlySelectedFeature: SpringFeature,
        selectedFeatures: Set<SpringFeature>,
        logger: Logger,
    ) {
        if (newlySelectedFeature !in advancedAuthFeatures) {
            return
        }

        if (SPRING_SECURITY in selectedFeatures) {
            return
        }

        logger.warn(
            "auth { ... } advanced options selected without security(). " +
                "This can be valid for specialized setups, but most applications should include: " +
                "auth { security(); oauth2Client() } or auth { security(); oauth2ResourceServer() }.",
        )
    }
}
