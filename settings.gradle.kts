plugins {
    id("dev.lajoscseppento.ruthless") version "0.8.0"
    id("com.gradle.develocity") version "+"
}

rootProject.name = "ycc-keycloak-provider"

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
    }
}
