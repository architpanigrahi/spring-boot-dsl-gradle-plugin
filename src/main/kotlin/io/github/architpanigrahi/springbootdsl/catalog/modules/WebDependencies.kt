package io.github.architpanigrahi.springbootdsl.catalog.modules

import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.IMPLEMENTATION
import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.TEST_IMPLEMENTATION
import io.github.architpanigrahi.springbootdsl.dependency.DependencyDeclaration
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature

internal val webDependencies: Map<SpringFeature, List<DependencyDeclaration>> =
    mapOf(
        SpringFeature.WEB_MVC to
            listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-webmvc",
                ),
            ),
        SpringFeature.WEB_FLUX to
            listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-webflux",
                ),
            ),
    )

internal val webTestCompanionDependencies: Map<SpringFeature, List<DependencyDeclaration>> =
    mapOf(
        SpringFeature.WEB_MVC to
            listOf(
                DependencyDeclaration(
                    configuration = TEST_IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-webmvc-test",
                ),
            ),
        SpringFeature.WEB_FLUX to
            listOf(
                DependencyDeclaration(
                    configuration = TEST_IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-webflux-test",
                ),
            ),
    )
