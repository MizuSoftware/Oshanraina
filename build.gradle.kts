plugins {
    id("java-library")
}

repositories {
    // Most of the libraries designed by Mizu are published on maven
    // central.
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains", "annotations", "23.0.0")
}