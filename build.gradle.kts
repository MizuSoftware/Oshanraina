plugins {
    `java-library`
    `maven-publish`
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group   = "wtf.mizu"
    version = "0.1.0"
}

subprojects {
    repositories {
        // Most of the libraries designed by Mizu are published on maven
        // central.
        mavenCentral()
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }

    publishing {
        publications {
            create("mavenJava", MavenPublication::class.java) {
                from(components["java"])
            }
        }
    }
}