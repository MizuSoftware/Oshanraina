plugins {
    id("java-library")
}

allprojects {
    apply(plugin = "java-library")

    repositories {
        // Most of the libraries designed by Mizu are published on maven
        // central.
        mavenCentral()
    }
}