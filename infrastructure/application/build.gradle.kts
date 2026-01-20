plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.jib)
    alias(libs.plugins.java)
    alias(libs.plugins.moduleGraphAssert)
}

tasks.jar {
    isEnabled = false
}

springBoot {
    buildInfo()
}

moduleGraphAssert {
    allowed = arrayOf(":infrastructure:.* -> :domain", ":infrastructure:application -> :.*")
    restricted = arrayOf(":domain -X> :.*")
    assertOnAnyBuild = true
}

jib {
    from {
        image = "eclipse-temurin:25-jdk"
    }
    container {
        jvmFlags = listOf("-XX:MaxRAMPercentage=75")
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":infrastructure:rest-api"))
    implementation(project(":infrastructure:jdbc-storage-connector"))

    implementation(platform(libs.springBootDependencies))
    implementation(libs.springBootStarterActuator)
    implementation(libs.jakartaAnnotationApi)
    implementation(libs.jakartaServletApi)
    implementation(libs.springTransaction)
    implementation(libs.springContext)
    implementation(libs.springWeb)
    implementation(libs.commonsLang3)

    testImplementation(libs.springBootStarterTest)
    testRuntimeOnly(libs.junitPlatformLauncher)
    testImplementation(libs.bundles.postgresIntegrationTest)
}