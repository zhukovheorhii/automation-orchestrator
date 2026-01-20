plugins {
    alias(libs.plugins.openapiGenerator)
    alias(libs.plugins.java)
}

dependencies {
    implementation(project(":domain"))

    implementation(platform(libs.springBootDependencies))
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterValidation)

    implementation(libs.jacksonDatabindNullable)
    implementation(libs.swaggerAnnotationsV3)

    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstructProcessor)

    testImplementation(libs.bundles.unitTest)
    testImplementation(libs.springBootStarterWebMvcTest)
}

sourceSets {
    main {
        java {
            srcDirs("src/main/java", "${layout.buildDirectory.get()}/generated-src/main/java")
        }
    }
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$projectDir/open-api/src/main/resources/openapi.yaml")
    outputDir.set("${layout.buildDirectory.get()}/generated-src")
    apiPackage.set("com.automation.secret.restapi.generated.api")
    modelPackage.set("com.automation.secret.restapi.generated.dto")
    modelNameSuffix.set("WebDto")
    configOptions.set(mapOf(
        "useSpringBoot3" to "true",
        "dateLibrary" to "java8",
        "interfaceOnly" to "true",
        "useTags" to "true",
        "skipDefaultInterface" to "true",
        "hideGenerationTimestamp" to "true",
        "implicitHeaders" to "true",
        "sourceFolder" to "main/java",
        "useResponseEntity" to "false"
    ))
}

openApiValidate {
    inputSpec.set("$projectDir/open-api/src/main/resources/openapi.yaml")
}

tasks.compileJava {
    dependsOn(tasks.openApiGenerate)
}
