package io.github.architpanigrahi.springbootdsl.dependency

import io.github.architpanigrahi.springbootdsl.catalog.DependencyCatalog
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature
import org.gradle.api.Project

class DependencyConfigurer(
    private val project: Project,
) {
    fun configure(feature: SpringFeature) {
        DependencyCatalog
            .dependenciesFor(feature)
            .forEach { dependency ->
                project.dependencies.add(
                    dependency.configuration.gradleName,
                    dependency.notation,
                )
            }
    }
}
