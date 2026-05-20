package io.github.architpanigrahi.springbootdsl.catalog

import io.github.architpanigrahi.springbootdsl.catalog.modules.dataDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.migrationDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.operationsDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.securityDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.testingDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.toolingDependencies
import io.github.architpanigrahi.springbootdsl.catalog.modules.webDependencies
import io.github.architpanigrahi.springbootdsl.dependency.DependencyDeclaration
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature

object DependencyCatalog {
    private val dependencies: Map<SpringFeature, List<DependencyDeclaration>> =
        buildMap {
            putAll(webDependencies)
            putAll(dataDependencies)
            putAll(toolingDependencies)
            putAll(operationsDependencies)
            putAll(securityDependencies)
            putAll(migrationDependencies)
            putAll(testingDependencies)
        }

    fun dependenciesFor(feature: SpringFeature): List<DependencyDeclaration> {
        return dependencies[feature].orEmpty()
    }
}
