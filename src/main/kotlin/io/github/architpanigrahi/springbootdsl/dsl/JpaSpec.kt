package io.github.architpanigrahi.springbootdsl.dsl

import org.gradle.api.GradleException

class JpaSpec {

    internal var selectedDatabase: JpaDatabase? = null
        private set

    /**
     * Uses PostgreSQL as the JDBC driver for JPA.
     */
    fun postgres() {
        selectDatabase(JpaDatabase.POSTGRES)
    }

    /**
     * Uses MySQL as the JDBC driver for JPA.
     */
    fun mysql() {
        selectDatabase(JpaDatabase.MYSQL)
    }

    /**
     * Uses H2 as the runtime database for JPA.
     */
    fun h2() {
        selectDatabase(JpaDatabase.H2)
    }

    private fun selectDatabase(database: JpaDatabase) {
        val existingDatabase = selectedDatabase

        if (existingDatabase != null && existingDatabase != database) {
            throw GradleException(
                "Only one JPA database driver can be selected. " +
                        "Already selected: ${existingDatabase.displayName}. " +
                        "Attempted: ${database.displayName}."
            )
        }

        selectedDatabase = database
    }
}