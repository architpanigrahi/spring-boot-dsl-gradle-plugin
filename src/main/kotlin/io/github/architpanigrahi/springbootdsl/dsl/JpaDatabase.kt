package io.github.architpanigrahi.springbootdsl.dsl

import io.github.architpanigrahi.springbootdsl.feature.SpringFeature
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.H2_DATABASE
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.MYSQL_JDBC
import io.github.architpanigrahi.springbootdsl.feature.SpringFeature.POSTGRES_JDBC

enum class JpaDatabase(
    val feature: SpringFeature,
    val displayName: String
) {
    POSTGRES(
        feature = POSTGRES_JDBC,
        displayName = "PostgreSQL"
    ),

    MYSQL(
        feature = MYSQL_JDBC,
        displayName = "MySQL"
    ),

    H2(
        feature = H2_DATABASE,
        displayName = "H2"
    )
}