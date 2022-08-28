val javaVersion = JavaVersion.VERSION_1_8

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

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

                val gitHost = "github.com"
                val repoId = "MizuSoftware/oshanraina"

                pom {
                    name.set("Oshanraina")
                    url.set("https://github.com/MizuSoftware/Oshanraina")
                    description.set("")

                    licenses {
                        license {
                            name.set("GNU Affero General Public License")
                            url.set("https://www.gnu.org/licenses/agpl-3.0.en.html")
                            distribution.set("repo")
                        }
                    }

                    developers {
                        developer {
                            id.set("Shyrogan")
                            name.set("Sebastien VIAL")
                        }
                        developer {
                            id.set("lambdagg")
                        }
                        developer {
                            id.set("xtrm")
                            email.set("oss@xtrm.me")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://$gitHost/$repoId.git")
                        developerConnection.set("scm:git:ssh://$gitHost/$repoId.git")
                        url.set("https://$gitHost/$repoId")
                    }
                }

                // Configure the signing extension to sign this Maven artifact.
                configure<SigningExtension> {
                    isRequired = project.properties["signing.keyId"] != null
                    sign(this@create)
                }
            }
        }
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }
}
