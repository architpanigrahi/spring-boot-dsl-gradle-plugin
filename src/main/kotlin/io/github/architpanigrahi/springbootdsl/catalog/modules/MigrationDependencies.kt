package io.github.architpanigrahi.springbootdsl.catalog.modules

import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.IMPLEMENTATION
import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.TEST_IMPLEMENTATION
import io.github.architpanigrahi.springbootdsl.dependency.DependencyDeclaration
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature

internal val migrationDependencies: Map<SpringFeature, List<DependencyDeclaration>> =
    mapOf(
        SpringFeature.FLYWAY_MIGRATION to
            listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.flywaydb:flyway-core",
                ),
            ),
        SpringFeature.LIQUIBASE_MIGRATION to
            listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.liquibase:liquibase-core",
                ),
            ),
    )

internal val migrationTestCompanionDependencies: Map<SpringFeature, List<DependencyDeclaration>> =
    mapOf(
        SpringFeature.FLYWAY_MIGRATION to
            listOf(
                DependencyDeclaration(
                    configuration = TEST_IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-test",
                ),
            ),
        SpringFeature.LIQUIBASE_MIGRATION to
            listOf(
                DependencyDeclaration(
                    configuration = TEST_IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-test",
                ),
            ),
    )
