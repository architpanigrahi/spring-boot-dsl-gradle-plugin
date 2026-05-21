package io.github.architpanigrahi.springbootdsl.validation

import io.github.architpanigrahi.springbootdsl.feature.SpringFeature
import org.gradle.api.logging.Logger

class FeatureValidator(
    private val logger: Logger,
    private val rules: List<FeatureValidationRule> =
        listOf(
            WebStackCombinationExclusiveRule,
            WebClientsWithoutServerGuidanceRule,
            MigrationEngineExclusiveRule,
        ),
) {
    fun validate(
        newlySelectedFeature: SpringFeature,
        selectedFeatures: Set<SpringFeature>,
    ) {
        rules.forEach { rule ->
            rule.validate(
                newlySelectedFeature = newlySelectedFeature,
                selectedFeatures = selectedFeatures,
                logger = logger,
            )
        }
    }
}
