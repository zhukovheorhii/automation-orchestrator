dependencies {
    implementation(project(":domain"))

    implementation(platform(libs.springBootDependencies))
    implementation(libs.springBootStarterDataJdbc)
    implementation(libs.postgresql)
    implementation(libs.mapstruct)

    implementation(libs.bundles.flyway)

    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)

    annotationProcessor(libs.mapstructProcessor)

    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.bundles.unitTest)
}
