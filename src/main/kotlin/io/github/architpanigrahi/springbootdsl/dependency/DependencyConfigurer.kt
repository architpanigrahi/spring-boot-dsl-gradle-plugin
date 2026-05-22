package io.github.architpanigrahi.springbootdsl.dependency

import io.github.architpanigrahi.springbootdsl.catalog.DependencyCatalog
import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.RUNTIME_ONLY
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.HTTP_CLIENT_REACTIVE
import org.gradle.api.Project
import java.time.Instant

class DependencyConfigurer(
    private val project: Project,
) {
    private data class AppliedDependency(
        val configuration: String,
        val notation: String,
        val classifier: String?,
    )

    private val appliedDependencies = mutableSetOf<AppliedDependency>()
    private val dependenciesByFeature = linkedMapOf<SpringFeature, MutableList<DependencyDeclaration>>()
    private var companionTestsEnabled = false

    fun configure(feature: SpringFeature) {
        val mainDependencies = DependencyCatalog.dependenciesFor(feature)
        applyDependencies(mainDependencies)
        appendFeatureDependencies(feature, mainDependencies)

        val platformDependencies = platformDependenciesFor(feature)
        applyDependencies(platformDependencies)
        appendFeatureDependencies(feature, platformDependencies)

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
            val key =
                AppliedDependency(
                    configuration = dependency.configuration.gradleName,
                    notation = dependency.notation,
                    classifier = dependency.classifier,
                )
            val isNewDependency = appliedDependencies.add(key)

            if (!isNewDependency) {
                return@forEach
            }

            val gradleDependency =
                project.dependencies.create(
                    classifierAwareNotation(
                        notation = dependency.notation,
                        classifier = dependency.classifier,
                    ),
                )
            project.dependencies.add(dependency.configuration.gradleName, gradleDependency)
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
                    it.configuration == dependency.configuration &&
                        it.notation == dependency.notation &&
                        it.classifier == dependency.classifier
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
                lines += "  - ${dependency.configuration.gradleName}: ${dependencyDisplayNotation(dependency)}"
            }
        }

        lines += ""
        lines += "All plugin-applied dependencies (deduplicated):"
        appliedDependencies
            .sortedWith(compareBy({ it.configuration }, { it.notation }, { it.classifier ?: "" }))
            .forEach { dependency ->
                lines += "- ${dependency.configuration}: ${dependencyDisplayNotation(dependency.notation, dependency.classifier)}"
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
            SpringFeature.SECURITY_OAUTH2_RESOURCE_SERVER -> "auth { oauth2ResourceServer() }"
            SpringFeature.SECURITY_OAUTH2_AUTH_SERVER -> "auth { oauth2AuthServer() }"
            SpringFeature.SECURITY_SAML2 -> "auth { saml2() }"
            SpringFeature.FLYWAY_MIGRATION -> "migrations { flyway() }"
            SpringFeature.LIQUIBASE_MIGRATION -> "migrations { liquibase() }"
            SpringFeature.SPRING_BOOT_TEST -> "test { springBootTest() }"
        }
    }

    private fun platformDependenciesFor(feature: SpringFeature): List<DependencyDeclaration> {
        if (feature != HTTP_CLIENT_REACTIVE) {
            return emptyList()
        }

        val osName = System.getProperty("os.name").orEmpty().lowercase()
        val osArch = System.getProperty("os.arch").orEmpty().lowercase()

        val macArm64 = osName.contains("mac") && (osArch.contains("aarch64") || osArch.contains("arm64"))
        if (!macArm64) {
            return emptyList()
        }

        return listOf(macosArm64NettyDnsResolver)
    }

    private fun dependencyDisplayNotation(dependency: DependencyDeclaration): String {
        return dependencyDisplayNotation(dependency.notation, dependency.classifier)
    }

    private fun dependencyDisplayNotation(
        notation: String,
        classifier: String?,
    ): String {
        return if (classifier == null) {
            notation
        } else {
            "$notation (classifier=$classifier)"
        }
    }

    private fun classifierAwareNotation(
        notation: String,
        classifier: String?,
    ): String {
        if (classifier == null) {
            return notation
        }

        val segments = notation.split(":")
        if (segments.size < 2) {
            return notation
        }

        val group = segments[0]
        val artifact = segments[1]
        val version = segments.getOrNull(2).orEmpty()

        return if (version.isBlank()) {
            "$group:$artifact::$classifier"
        } else {
            "$group:$artifact:$version:$classifier"
        }
    }

    companion object {
        private val macosArm64NettyDnsResolver =
            DependencyDeclaration(
                configuration = RUNTIME_ONLY,
                notation = "io.netty:netty-resolver-dns-native-macos",
                classifier = "osx-aarch_64",
            )
    }
}
