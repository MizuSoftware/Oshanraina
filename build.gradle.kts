val javaVersion = JavaVersion.VERSION_1_8

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "wtf.mizu"
    version = "0.1.0"

    repositories {
        mavenLocal()

        // Most of the libraries designed by Mizu are published on Maven Central.
        mavenCentral()
    }

    dependencies {
        val implementation by configurations

        implementation("org.jetbrains", "annotations", "23.0.0")
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(
                JavaLanguageVersion.of(javaVersion.ordinal + 1)
            )
        }

        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion

        withJavadocJar()
        withSourcesJar()
    }

    configure<PublishingExtension> {
        publications {
            create("mavenJava", MavenPublication::class.java) {
                from(components["java"])
            }
        }
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }
}
