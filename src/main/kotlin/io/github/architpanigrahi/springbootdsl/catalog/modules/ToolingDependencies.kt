package io.github.architpanigrahi.springbootdsl.catalog.modules

import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.ANNOTATION_PROCESSOR
import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.COMPILE_ONLY
import io.github.architpanigrahi.springbootdsl.dependency.DependencyDeclaration
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature

internal val toolingDependencies: Map<SpringFeature, List<DependencyDeclaration>> =
    mapOf(
        SpringFeature.LOMBOK to listOf(
            DependencyDeclaration(
                configuration = COMPILE_ONLY,
                notation = "org.projectlombok:lombok"
            ),
            DependencyDeclaration(
                configuration = ANNOTATION_PROCESSOR,
                notation = "org.projectlombok:lombok"
            )
        )
    )
