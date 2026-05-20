package io.github.architpanigrahi.springbootdsl.catalog

import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.ANNOTATION_PROCESSOR
import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.COMPILE_ONLY
import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.IMPLEMENTATION
import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.RUNTIME_ONLY
import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.TEST_IMPLEMENTATION
import io.github.architpanigrahi.springbootdsl.dependency.DependencyDeclaration
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature

object DependencyCatalog {

    private val dependencies: Map<SpringFeature, List<DependencyDeclaration>> =
        mapOf(
            SpringFeature.WEB_MVC to listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-webmvc"
                )
            ),

            SpringFeature.WEB_FLUX to listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-webflux"
                )
            ),

            SpringFeature.DATA_JPA to listOf(
                DependencyDeclaration(
                    configuration = IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-data-jpa"
                )
            ),

            SpringFeature.POSTGRES_JDBC to listOf(
                DependencyDeclaration(
                    configuration = RUNTIME_ONLY,
                    notation = "org.postgresql:postgresql"
                )
            ),

            SpringFeature.MYSQL_JDBC to listOf(
                DependencyDeclaration(
                    configuration = RUNTIME_ONLY,
                    notation = "com.mysql:mysql-connector-j"
                )
            ),

            SpringFeature.H2_DATABASE to listOf(
                DependencyDeclaration(
                    configuration = RUNTIME_ONLY,
                    notation = "com.h2database:h2"
                ),
                DependencyDeclaration(
                    configuration = RUNTIME_ONLY,
                    notation = "org.springframework.boot:spring-boot-h2console"
                )
            ),

            SpringFeature.LOMBOK to listOf(
                DependencyDeclaration(
                    configuration = COMPILE_ONLY,
                    notation = "org.projectlombok:lombok"
                ),
                DependencyDeclaration(
                    configuration = ANNOTATION_PROCESSOR,
                    notation = "org.projectlombok:lombok"
                )
            ),

            SpringFeature.SPRING_BOOT_TEST to listOf(
                DependencyDeclaration(
                    configuration = TEST_IMPLEMENTATION,
                    notation = "org.springframework.boot:spring-boot-starter-test"
                )
            )
        )

    fun dependenciesFor(feature: SpringFeature): List<DependencyDeclaration> {
        return dependencies[feature].orEmpty()
    }
}