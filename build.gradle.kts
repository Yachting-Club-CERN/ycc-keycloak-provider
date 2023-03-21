plugins {
    id("dev.lajoscseppento.ruthless.java-library")
}

val keycloakVersion = "21.0.1"

dependencies {
    compileOnly("org.keycloak:keycloak-core:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-server-spi:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-model-jpa:$keycloakVersion")
    compileOnly("commons-codec:commons-codec:[1.15,2)") // Comes with Keycloak 21.0.1
    compileOnly("jakarta.persistence:jakarta.persistence-api:[2.2.3,3)")// Comes with Keycloak 21.0.1
    compileOnly("com.oracle.database.jdbc:ojdbc11:[21.5.0.0,22)") // Comes with Keycloak 21.0.1
}

configurations {
    testImplementation.get().extendsFrom(compileOnly.get())
}
