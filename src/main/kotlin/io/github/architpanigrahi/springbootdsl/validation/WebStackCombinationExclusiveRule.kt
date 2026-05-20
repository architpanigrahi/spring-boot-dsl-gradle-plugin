package io.github.architpanigrahi.springbootdsl.validation

import io.github.architpanigrahi.springbootdsl.feature.SpringFeature
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.WEB_FLUX
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.WEB_MVC
import org.gradle.api.GradleException
import org.gradle.api.logging.Logger

object WebStackCombinationExclusiveRule : FeatureValidationRule {

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
            throw GradleException(
                "webMvc() and webFlux() cannot be selected together. " +
                        "Choose exactly one web stack."
            )
        }
    }
}
