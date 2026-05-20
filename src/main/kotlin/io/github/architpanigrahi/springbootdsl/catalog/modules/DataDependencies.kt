package io.github.architpanigrahi.springbootdsl.catalog.modules

import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.IMPLEMENTATION
import io.github.architpanigrahi.springbootdsl.dependency.DependencyConfiguration.RUNTIME_ONLY
import io.github.architpanigrahi.springbootdsl.dependency.DependencyDeclaration
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature

internal val dataDependencies: Map<SpringFeature, List<DependencyDeclaration>> =
    mapOf(
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
        )
    )
