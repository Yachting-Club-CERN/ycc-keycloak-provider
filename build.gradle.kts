import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("dev.lajoscseppento.ruthless.java-library")
    id("org.sonarqube") version "4.4.1.3373"
    `maven-publish`
}

val keycloakVersion = "26.1.4"

// Provided for Keyclaok
val oracleDriverVersion = "23.7.0.25.01"

// Exact versions included with Keycloak 26.1.4
val commonsCodecVersion = "1.17.1"
val jacksonVersion = "2.17.2"
val jakartaPersistenceVersion = "3.1.0"
val jandexVersion = "3.2.3"

dependencies {
    compileOnly("org.keycloak:keycloak-core:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-server-spi:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-model-jpa:$keycloakVersion")
    compileOnly("commons-codec:commons-codec:$commonsCodecVersion")
    compileOnly("jakarta.persistence:jakarta.persistence-api:$jakartaPersistenceVersion")

    compileOnly("com.oracle.database.jdbc:ojdbc11:$oracleDriverVersion")

    testImplementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    // Needed for running Hibernate demos as of 2025-03 / Keycloak 26.1.4
    testImplementation("io.smallrye:jandex:$jandexVersion")
}

configurations {
    testImplementation.get().extendsFrom(compileOnly.get())
}

// Needed for running Hibernate demos as of 2025-03 / Keycloak 26.1.4
tasks.withType<JavaExec> {
    jvmArgs("-Djava.util.logging.manager=org.jboss.logmanager.LogManager")
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
