package io.github.architpanigrahi.springbootdsl.validation

import io.github.architpanigrahi.springbootdsl.feature.SpringFeature
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.WEB_FLUX
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.WEB_MVC
import org.gradle.api.logging.Logger

object WebStackCombinationRule : FeatureValidationRule {

    override fun validate(
        newlySelectedFeature: SpringFeature,
        selectedFeatures: Set<SpringFeature>,
        logger: Logger
    ) {
        val webFeatureWasJustAdded =
            newlySelectedFeature == WEB_MVC || newlySelectedFeature == WEB_FLUX

        val bothWebStacksSelected =
            WEB_MVC in selectedFeatures && WEB_FLUX in selectedFeatures

        if (webFeatureWasJustAdded && bothWebStacksSelected) {
            logger.warn(
                "Both webMvc() and webFlux() were selected. " +
                        "This may be intentional, but it is worth verifying."
            )
        }
    }
}