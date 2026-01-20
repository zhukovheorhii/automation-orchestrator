dependencies {
    testImplementation(platform(libs.springBootDependencies))
    testImplementation(libs.bundles.unitTest)
    testImplementation(libs.archunitJunit5)
    testRuntimeOnly(libs.junitPlatformLauncher)
}
