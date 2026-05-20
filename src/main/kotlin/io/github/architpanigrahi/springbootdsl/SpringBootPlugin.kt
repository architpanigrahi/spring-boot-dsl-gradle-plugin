package io.github.architpanigrahi.springbootdsl

import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfigurer
import io.github.architpanigrahi.springbootdsl.feature.FeatureRegistry
import io.github.architpanigrahi.springbootdsl.validation.FeatureValidator
import org.gradle.api.Plugin
import org.gradle.api.Project

class SpringBootPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.pluginManager.apply("java")
        project.pluginManager.apply("org.springframework.boot")
        project.pluginManager.apply("io.spring.dependency-management")

        val dependencyConfigurer = DependencyConfigurer(project)
        val featureValidator = FeatureValidator(project.logger)

        val featureRegistry = FeatureRegistry(
            onFeatureSelected = dependencyConfigurer::configure,
            onFeaturesChanged = featureValidator::validate
        )

        project.extensions.create(
            "springBootPlugin",
            SpringBootPluginExtension::class.java,
            featureRegistry
        )
    }
}