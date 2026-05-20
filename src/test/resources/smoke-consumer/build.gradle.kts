plugins {
    id("io.github.architpanigrahi.springbootdsl")
}

repositories {
    mavenCentral()
}

springBootPlugin {
    web {
        mvc()
    }
    ops {
        actuator()
    }
    auth {
        security()
        jwt()
        oauth2Client()
    }
    migrations {
        flyway()
    }
    data {
        jpa {
            h2()
        }
        redis()
        mongo()
    }
    devTools {
        lombok()
    }
    test {
        includeCompanionTests()
        springBootTest()
    }
}
