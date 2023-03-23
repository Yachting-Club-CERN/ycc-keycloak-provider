import org.apache.tools.ant.filters.ReplaceTokens

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

// Generate tasks for creating configuration JARs
val configuration = java.sourceSets.register("configuration-template")

listOf("ycc-db-local", "ycc-db-test", "ycc-db-prod").forEach { env ->
    val generate = tasks.register<Sync>("generate-$env") {
        from(configuration.get().allSource)
        into(project.buildDir.toPath().resolve("tmp/generated-$env"))
        filter(ReplaceTokens::class, "tokens" to mapOf("name" to env))
    }

    val jar = tasks.register<Jar>("jar-$env") {
        from(generate)
        archiveClassifier.set(env)
    }

    tasks.named("assemble") {
        dependsOn(jar)
    }
}
