package io.github.architpanigrahi.springbootdsl.dsl

import io.github.architpanigrahi.springbootdsl.feature.FeatureRegistry
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.SPRING_BOOT_TEST

class TestingSpec(
    private val featureRegistry: FeatureRegistry,
    private val onIncludeCompanionTests: () -> Unit,
) {
    /**
     * Adds Spring Boot's default test starter.
     */
    fun springBootTest() {
        featureRegistry.select(SPRING_BOOT_TEST)
    }

    fun includeCompanionTests() {
        onIncludeCompanionTests()
    }
}
