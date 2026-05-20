plugins {
    id("io.github.architpanigrahi.springbootdsl")
}

repositories {
    mavenCentral()
}

springBootPlugin {
    web {
        webMvc()
    }
    operations {
        actuator()
    }
    security {
        springSecurity()
    }
    databaseMigrations {
        flyway()
    }
    data {
        jpa {
            h2()
        }
        redis()
        mongodb()
    }
    developerTools {
        lombok()
    }
    testing {
        springBootTest()
    }
}
