import java.net.URI

plugins {
    alias(libs.plugins.java)
    alias(libs.plugins.mavenPublish)
}

version = "0.0.1" + System.getenv().getOrDefault("CI_VERSION_SUFFIX", "")


publishing {
    publications {
        create<MavenPublication>("maven") {
            version = project.version.toString()
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = System.getenv().getOrDefault("JAVA_LIBS_REPOSITORY_URL", "").let(::URI)
        }
    }
}
