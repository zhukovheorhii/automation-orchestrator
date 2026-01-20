plugins {
    alias(libs.plugins.java)
}

allprojects {
    tasks.withType<PublishToMavenRepository> {
        doFirst {
            println("Publishing ${publication.groupId}:${publication.artifactId}:${publication.version} to ${repository.url}")
        }
    }
}

subprojects {
    apply(plugin = "java")

    group = "com.automation.secret"

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.compileJava {
        val implementation = project.configurations.findByName("implementation")
        val hasMapstruct = implementation?.allDependencies?.any {
            it.group == "org.mapstruct" || it.name.contains("mapstruct")
        } ?: false

        if (hasMapstruct) {
            options.compilerArgs.addAll(
                listOf(
                    "-Amapstruct.suppressGeneratorTimestamp=true",
                    "-Amapstruct.suppressGeneratorVersionInfoComment=true"
                )
            )
        }

        // keep methods' arguments names in the bytecode for clear Jakarta validation errors
        options.compilerArgs.add("-parameters")
    }
}


tasks.wrapper {
    gradleVersion = "9.2.1"
    distributionType = Wrapper.DistributionType.BIN
}