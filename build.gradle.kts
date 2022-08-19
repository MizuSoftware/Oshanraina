plugins {
    `java-library`
    `maven-publish`
}

group   = "wtf.mizu"
version = "0.1.0"

repositories {
    // Most of the libraries designed by Mizu are published on maven
    // central.
    mavenCentral()
}

dependencies {
    implementation("com.google.guava", "guava", "31.1-jre")
    implementation("com.squareup", "javapoet", "1.13.0")
    implementation("org.jetbrains", "annotations", "23.0.0")
}

publishing {
    publications {
        create("mavenJava", MavenPublication::class.java) {
            from(components["java"])
        }
    }
}