plugins {
    id("dev.lajoscseppento.ruthless") version "0.5.0"
    id("com.gradle.enterprise") version "+"
}

rootProject.name = "ycc-keycloak-provider"

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}
