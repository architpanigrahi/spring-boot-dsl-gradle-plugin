package io.github.architpanigrahi.springbootdsl.validation

import io.github.architpanigrahi.springbootdsl.feature.SpringFeature
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.FLYWAY_MIGRATION
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.LIQUIBASE_MIGRATION
import org.gradle.api.GradleException
import org.gradle.api.logging.Logger

object MigrationEngineExclusiveRule : FeatureValidationRule {
    override fun validate(
        newlySelectedFeature: SpringFeature,
        selectedFeatures: Set<SpringFeature>,
        logger: Logger,
    ) {
        val migrationFeatureWasJustAdded =
            newlySelectedFeature == FLYWAY_MIGRATION || newlySelectedFeature == LIQUIBASE_MIGRATION

        val bothEnginesSelected =
            FLYWAY_MIGRATION in selectedFeatures && LIQUIBASE_MIGRATION in selectedFeatures

        if (migrationFeatureWasJustAdded && bothEnginesSelected) {
            throw GradleException(
                "flyway() and liquibase() cannot be selected together. " +
                    "Choose exactly one migration engine.",
            )
        }
    }
}
