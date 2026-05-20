package io.github.architpanigrahi.springbootdsl.dsl

import io.github.architpanigrahi.springbootdsl.feature.FeatureRegistry
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.DATA_JPA
import org.gradle.api.Action
import org.gradle.api.GradleException

class DataSpec(
    private val featureRegistry: FeatureRegistry
) {
    /**
     * Adds Spring Data JPA and requires exactly one JDBC database driver.
     */
    fun jpa(action: Action<JpaSpec>) {
        val jpaSpec = JpaSpec()
        action.execute(jpaSpec)

        val selectedDatabase =
            jpaSpec.selectedDatabase
                ?: throw GradleException(
                    "data { jpa { ... } } requires a database driver. " +
                            "For example: jpa { postgres() }"
                )

        featureRegistry.selectAll(
            DATA_JPA,
            selectedDatabase.feature
        )
    }
}