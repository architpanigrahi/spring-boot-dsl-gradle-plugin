package io.github.architpanigrahi.springbootdsl.catalog

import io.github.architpanigrahi.springbootdsl.catalog.modules.clientDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.clientTestCompanionDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.dataDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.dataTestCompanionDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.migrationDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.migrationTestCompanionDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.operationsDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.operationsTestCompanionDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.securityDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.securityTestCompanionDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.testingDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.toolingDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.webDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.webTestCompanionDependencies
import io.github.architpanigrahi.springbootdsl.dependency.DependencyDeclaration
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature

object DependencyCatalog {
    private val dependencies: Map<SpringFeature, List<DependencyDeclaration>> =
        buildMap {
            putAll(webDependencies)
            putAll(clientDependencies)
            putAll(dataDependencies)
            putAll(toolingDependencies)
            putAll(operationsDependencies)
            putAll(securityDependencies)
            putAll(migrationDependencies)
            putAll(testingDependencies)
        }

    private val companionTestDependencies: Map<SpringFeature, List<DependencyDeclaration>> =
        buildMap {
            putAll(webTestCompanionDependencies)
            putAll(clientTestCompanionDependencies)
            putAll(dataTestCompanionDependencies)
            putAll(operationsTestCompanionDependencies)
            putAll(securityTestCompanionDependencies)
            putAll(migrationTestCompanionDependencies)
        }

    fun dependenciesFor(feature: SpringFeature): List<DependencyDeclaration> {
        return dependencies[feature].orEmpty()
    }

    fun companionTestDependenciesFor(feature: SpringFeature): List<DependencyDeclaration> {
        return companionTestDependencies[feature].orEmpty()
    }
}
