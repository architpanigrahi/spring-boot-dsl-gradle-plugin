package io.github.architpanigrahi.springbootdsl.validation

import io.github.architpanigrahi.springbootdsl.feature.SpringFeature
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.HTTP_CLIENT_REACTIVE
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.HTTP_CLIENT_REST
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.WEB_FLUX
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.WEB_MVC
import org.gradle.api.logging.Logger

object WebClientsWithoutServerGuidanceRule : FeatureValidationRule {
    override fun validate(
        newlySelectedFeature: SpringFeature,
        selectedFeatures: Set<SpringFeature>,
        logger: Logger,
    ) {
        val selectedClientFeature =
            newlySelectedFeature == HTTP_CLIENT_REST || newlySelectedFeature == HTTP_CLIENT_REACTIVE
        if (!selectedClientFeature) {
            return
        }

        val hasServerSelection = WEB_MVC in selectedFeatures || WEB_FLUX in selectedFeatures
        if (hasServerSelection) {
            return
        }

        val selectedClientCount =
            selectedFeatures.count {
                it == HTTP_CLIENT_REST || it == HTTP_CLIENT_REACTIVE
            }
        if (selectedClientCount != 1) {
            return
        }

        logger.warn(
            "web { restClient()/webClient() } selects HTTP clients only and does not select a server stack. " +
                "This is valid for client-only projects. " +
                "For server apps use one canonical combination: " +
                "web { mvc(); webClient() } or web { reactiveServer(); restClient() }.",
        )
    }
}
