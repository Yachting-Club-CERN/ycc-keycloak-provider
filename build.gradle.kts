plugins {
    id("dev.lajoscseppento.ruthless.java-library")
    id("org.sonarqube") version "4.0.0.2929"
}

val keycloakVersion = "21.0.1"

dependencies {
    compileOnly("org.keycloak:keycloak-core:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-server-spi:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-model-jpa:$keycloakVersion")
    compileOnly("commons-codec:commons-codec:[1.15,2)") // Comes with Keycloak 20.0.1
    compileOnly("jakarta.persistence:jakarta.persistence-api:[2.2.3,23)")// Comes with Keycloak 20.0.1
    compileOnly("com.oracle.database.jdbc:ojdbc11:[21.5.0.0,22)") // Comes with Keycloak 20.0.1
}

configurations {
    testImplementation.get().extendsFrom(compileOnly.get())
}
