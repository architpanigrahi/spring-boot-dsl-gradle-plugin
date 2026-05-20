package io.github.architpanigrahi.springbootdsl.dsl

import io.github.architpanigrahi.springbootdsl.feature.SpringFeature
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.FLYWAY_MIGRATION
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.LIQUIBASE_MIGRATION

enum class MigrationEngine(
    val feature: SpringFeature,
    val displayName: String,
) {
    FLYWAY(
        feature = FLYWAY_MIGRATION,
        displayName = "Flyway",
    ),
    LIQUIBASE(
        feature = LIQUIBASE_MIGRATION,
        displayName = "Liquibase",
    ),
}
