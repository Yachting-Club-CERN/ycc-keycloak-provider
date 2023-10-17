import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("dev.lajoscseppento.ruthless.java-library")
    id("org.sonarqube") version "4.4.1.3373"
    `maven-publish`
}

val keycloakVersion = "22.0.4"

dependencies {
    compileOnly("org.keycloak:keycloak-core:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-server-spi:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-model-jpa:$keycloakVersion")
    compileOnly("commons-codec:commons-codec:[1.16,2)") // Comes with Keycloak 22.0.4
    compileOnly("jakarta.persistence:jakarta.persistence-api:[3.1.0,4)")// Comes with Keycloak 22.0.4
    compileOnly("com.oracle.database.jdbc:ojdbc11:[23.2.0.0,24)") // Comes with Keycloak 22.0.4
}

configurations {
    testImplementation.get().extendsFrom(compileOnly.get())
}

// Generate tasks for creating configuration JARs
val configuration = java.sourceSets.register("configuration-template")
val configurationJarTasks = mutableListOf<TaskProvider<Jar>>()

listOf("ycc-db-local", "ycc-db-dev", "ycc-db-test", "ycc-db-prod").forEach { env ->
    val generate = tasks.register<Sync>("generate-$env") {
        from(configuration.get().allSource)
        into(layout.buildDirectory.file("tmp/generated-$env"))
        filter(ReplaceTokens::class, "tokens" to mapOf("name" to env))
    }

    val jar = tasks.register<Jar>("jar-$env") {
        from(generate)
        archiveClassifier.set(env)
    }
    configurationJarTasks.add(jar)

    tasks.named("assemble") {
        dependsOn(jar)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            configurationJarTasks.forEach { artifact(it) }
        }
    }

    if (!(version as String).contains("-SNAPSHOT")) {
        logger.quiet("Enabling publication for version {}", version)

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/Yachting-Club-CERN/ycc-keycloak-provider")
                credentials {
                    username =
                        project.findProperty("gpr.user") as String? ?: System.getenv("GPR_USERNAME")
                    password =
                        project.findProperty("gpr.key") as String? ?: System.getenv("GPR_TOKEN")
                }
            }
        }
    } else {
        logger.quiet("Skipping publication for version {}", version)
    }
}
