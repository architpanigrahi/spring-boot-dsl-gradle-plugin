package io.github.architpanigrahi.springbootdsl.catalog.modules

import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.IMPLEMENTATION
import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.TEST_IMPLEMENTATION
import io.github.architpanigrahi.springbootdsl.dependency.DependencyDeclaration
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature

internal val securityDependencies: Map<SpringFeature, List<DependencyDeclaration>> =
    mapOf(
        SpringFeature.SPRING_SECURITY to
            listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-security",
                ),
            ),
        SpringFeature.SECURITY_OAUTH2_CLIENT to
            listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-oauth2-client",
                ),
            ),
        SpringFeature.SECURITY_OAUTH2_RESOURCE_SERVER to
            listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-oauth2-resource-server",
                ),
            ),
        SpringFeature.SECURITY_OAUTH2_AUTH_SERVER to
            listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-security-oauth2-authorization-server",
                ),
            ),
        SpringFeature.SECURITY_SAML2 to
            listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-security-saml2",
                ),
            ),
    )

internal val securityTestCompanionDependencies: Map<SpringFeature, List<DependencyDeclaration>> =
    mapOf(
        SpringFeature.SPRING_SECURITY to
            listOf(
                DependencyDeclaration(
                    configuration = TEST_IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-security-test",
                ),
            ),
        SpringFeature.SECURITY_OAUTH2_CLIENT to
            listOf(
                DependencyDeclaration(
                    configuration = TEST_IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-security-oauth2-client-test",
                ),
            ),
        SpringFeature.SECURITY_OAUTH2_RESOURCE_SERVER to
            listOf(
                DependencyDeclaration(
                    configuration = TEST_IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-security-oauth2-resource-server-test",
                ),
            ),
        SpringFeature.SECURITY_OAUTH2_AUTH_SERVER to
            listOf(
                DependencyDeclaration(
                    configuration = TEST_IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-security-oauth2-authorization-server-test",
                ),
            ),
        SpringFeature.SECURITY_SAML2 to
            listOf(
                DependencyDeclaration(
                    configuration = TEST_IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-security-saml2-test",
                ),
            ),
    )
