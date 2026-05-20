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
    data {
        jpa {
            h2()
        }
    }
    developerTools {
        lombok()
    }
    testing {
        springBootTest()
    }
}
