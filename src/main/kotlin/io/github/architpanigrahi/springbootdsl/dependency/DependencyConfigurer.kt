package io.github.architpanigrahi.springbootdsl.dependency

import io.github.architpanigrahi.springbootdsl.catalog.DependencyCatalog
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature
import org.gradle.api.Project
import java.time.Instant

class DependencyConfigurer(
    private val project: Project,
) {
    private val appliedDependencies = mutableSetOf<Pair<String, String>>()
    private val dependenciesByFeature = linkedMapOf<SpringFeature, MutableList<DependencyDeclaration>>()
    private var companionTestsEnabled = false

    fun configure(feature: SpringFeature) {
        val mainDependencies = DependencyCatalog.dependenciesFor(feature)
        applyDependencies(mainDependencies)
        appendFeatureDependencies(feature, mainDependencies)

        if (companionTestsEnabled) {
            val companionDependencies = DependencyCatalog.companionTestDependenciesFor(feature)
            applyDependencies(companionDependencies)
            appendFeatureDependencies(feature, companionDependencies)
        }
    }

    fun enableCompanionTestsFor(selectedFeatures: Set<SpringFeature>) {
        if (companionTestsEnabled) {
            return
        }

        companionTestsEnabled = true
        selectedFeatures.forEach { feature ->
            val companionDependencies = DependencyCatalog.companionTestDependenciesFor(feature)
            applyDependencies(companionDependencies)
            appendFeatureDependencies(feature, companionDependencies)
        }
    }

    fun emitDependencyReport() {
        if (dependenciesByFeature.isEmpty()) {
            return
        }

        val reportFile = project.rootProject.file("springbootdsl-dependencies.txt")
        val reportText = buildReport()
        reportFile.writeText(reportText)

        project.logger.lifecycle(
            "Spring Boot DSL report written: ${reportFile.absolutePath}",
        )
    }

    private fun applyDependencies(dependencies: List<DependencyDeclaration>) {
        dependencies.forEach { dependency ->
            val key = dependency.configuration.gradleName to dependency.notation
            val isNewDependency = appliedDependencies.add(key)

            if (!isNewDependency) {
                return@forEach
            }

            project.dependencies.add(
                dependency.configuration.gradleName,
                dependency.notation,
            )
        }
    }

    private fun appendFeatureDependencies(
        feature: SpringFeature,
        dependencies: List<DependencyDeclaration>,
    ) {
        if (dependencies.isEmpty()) {
            return
        }

        val featureDependencies = dependenciesByFeature.getOrPut(feature) { mutableListOf() }
        dependencies.forEach { dependency ->
            val alreadyTracked =
                featureDependencies.any {
                    it.configuration == dependency.configuration && it.notation == dependency.notation
                }
            if (!alreadyTracked) {
                featureDependencies.add(dependency)
            }
        }
    }

    private fun buildReport(): String {
        val lines = mutableListOf<String>()
        lines += "Spring Boot DSL Dependency Report"
        lines += "Project: ${project.path}"
        lines += "Generated at: ${Instant.now()}"
        lines += ""
        lines += "Feature selections and mapped dependencies:"

        dependenciesByFeature.forEach { (feature, dependencies) ->
            lines += "- ${featureDslLabel(feature)}"
            dependencies.forEach { dependency ->
                lines += "  - ${dependency.configuration.gradleName}: ${dependency.notation}"
            }
        }

        lines += ""
        lines += "All plugin-applied dependencies (deduplicated):"
        appliedDependencies
            .sortedWith(compareBy({ it.first }, { it.second }))
            .forEach { (configuration, notation) ->
                lines += "- $configuration: $notation"
            }

        return lines.joinToString(separator = "\n", postfix = "\n")
    }

    private fun featureDslLabel(feature: SpringFeature): String {
        return when (feature) {
            SpringFeature.WEB_MVC -> "web { mvc() }"
            SpringFeature.WEB_FLUX -> "web { reactiveServer() }"
            SpringFeature.HTTP_CLIENT_REST -> "web { restClient() }"
            SpringFeature.HTTP_CLIENT_REACTIVE -> "web { webClient() }"
            SpringFeature.DATA_JPA -> "data { jpa { ... } }"
            SpringFeature.POSTGRES_JDBC -> "data { jpa { postgres() } }"
            SpringFeature.MYSQL_JDBC -> "data { jpa { mysql() } }"
            SpringFeature.H2_DATABASE -> "data { jpa { h2() } }"
            SpringFeature.DATA_REDIS -> "data { redis() }"
            SpringFeature.DATA_MONGODB -> "data { mongo() }"
            SpringFeature.LOMBOK -> "devTools { lombok() }"
            SpringFeature.SPRING_BOOT_ACTUATOR -> "ops { actuator() }"
            SpringFeature.SPRING_SECURITY -> "auth { security() }"
            SpringFeature.SECURITY_OAUTH2_CLIENT -> "auth { oauth2Client() }"
            SpringFeature.SECURITY_OAUTH2_RESOURCE_SERVER -> "auth { jwt() }"
            SpringFeature.FLYWAY_MIGRATION -> "migrations { flyway() }"
            SpringFeature.LIQUIBASE_MIGRATION -> "migrations { liquibase() }"
            SpringFeature.SPRING_BOOT_TEST -> "test { springBootTest() }"
        }
    }
}
