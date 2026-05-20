package io.github.architpanigrahi.springbootdsl.catalog.modules

import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.IMPLEMENTATION
import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.TEST_IMPLEMENTATION
import io.github.architpanigrahi.springbootdsl.dependency.DependencyDeclaration
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature

internal val operationsDependencies: Map<SpringFeature, List<DependencyDeclaration>> =
    mapOf(
        SpringFeature.SPRING_BOOT_ACTUATOR to
            listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-actuator",
                ),
            ),
    )

internal val operationsTestCompanionDependencies: Map<SpringFeature, List<DependencyDeclaration>> =
    mapOf(
        SpringFeature.SPRING_BOOT_ACTUATOR to
            listOf(
                DependencyDeclaration(
                    configuration = TEST_IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-actuator-test",
                ),
            ),
    )
