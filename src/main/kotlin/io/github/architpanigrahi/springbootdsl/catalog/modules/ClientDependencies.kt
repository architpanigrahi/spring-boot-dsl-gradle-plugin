package io.github.architpanigrahi.springbootdsl.catalog.modules

import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.IMPLEMENTATION
import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.TEST_IMPLEMENTATION
import io.github.architpanigrahi.springbootdsl.dependency.DependencyDeclaration
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature

internal val clientDependencies: Map<SpringFeature, List<DependencyDeclaration>> =
    mapOf(
        SpringFeature.HTTP_CLIENT_REST to
            listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-webmvc",
                ),
            ),
        SpringFeature.HTTP_CLIENT_REACTIVE to
            listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-webflux",
                ),
            ),
    )

internal val clientTestCompanionDependencies: Map<SpringFeature, List<DependencyDeclaration>> =
    mapOf(
        SpringFeature.HTTP_CLIENT_REST to
            listOf(
                DependencyDeclaration(
                    configuration = TEST_IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-webmvc-test",
                ),
            ),
        SpringFeature.HTTP_CLIENT_REACTIVE to
            listOf(
                DependencyDeclaration(
                    configuration = TEST_IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-webflux-test",
                ),
            ),
    )
