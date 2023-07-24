import com.diffplug.gradle.spotless.SpotlessPlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin

plugins {
    java
    `maven-publish`
    `java-library`
    id("de.chojo.publishdata") version "1.2.4"
    alias(libs.plugins.spotless)
    alias(libs.plugins.indra.core)
    alias(libs.plugins.indra.publishing)
    alias(libs.plugins.indra.sonatype)
}

group = "de.chojo.sadu"
version = "1.3.1"

dependencies {
    api(project(":sadu-sqlite"))
    api(project(":sadu-postgresql"))
    api(project(":sadu-mariadb"))
    api(project(":sadu-mysql"))
    api(project(":sadu-updater"))
    api(project(":sadu-datasource"))
}

subprojects {
    apply {
        // We want to apply several plugins to subprojects
        plugin<JavaPlugin>()
        plugin<SpotlessPlugin>()
        plugin<de.chojo.PublishData>()
        plugin<JavaLibraryPlugin>()
    }
    if (!project.name.contains("examples")) {
        apply {
            plugin<MavenPublishPlugin>()
            plugin<IndraPlugin>()
            plugin<IndraPublishingPlugin>()
            plugin<SigningPlugin>()
        }
        indra {
            javaVersions {
                target(15)
                testWith(15)
            }

            github("rainbowdashlabs", "sadu") {
                ci(true)
            }

            lgpl3OrLaterLicense()
            signWithKeyFromPrefixedProperties("rainbowdashlabs")
            configurePublications {
                pom {
                    developers {
                        developer {
                            id.set("rainbowdashlabs")
                            name.set("Florian Fülling")
                            email.set("mail@chojo.dev")
                        }
                    }
                }
            }
        }

    }
}

indra {
    javaVersions {
        target(15)
        testWith(15)
    }

    github("rainbowdashlabs", "sadu") {
        ci(true)
    }

    lgpl3OrLaterLicense()
}

indraSonatype {
    useAlternateSonatypeOSSHost("s01")
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://eldonexus.de/repository/maven-public/")
        maven("https://eldonexus.de/repository/maven-proxies/")
    }

    dependencies {
        testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.9.3")
        testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.9.3")
        testImplementation("org.mockito", "mockito-core", "3.+")
    }

    spotless {
        java {
            licenseHeaderFile(rootProject.file("HEADER.txt"))
            target("**/*.java")
        }
    }

    publishData {
        useEldoNexusRepos()
        publishComponent("java")
    }

    if (!project.name.contains("examples")) {
        publishing {
            publications.create<MavenPublication>("eldonexus") {
                publishData.configurePublication(this)

                pom {
                    url.set("https://github.com/rainbowdashlabs/sadu")
                    developers {
                        developer {
                            name.set("Florian Fülling")
                            url.set("https://github.com/rainbowdashlabs")
                        }
                    }
                    licenses {
                        license {
                            name.set("GNU Affero General Public License v3.0")
                            url.set("https://github.com/rainbowdashlabs/sadu/blob/main/LICENSE.md")
                        }
                    }
                }
            }

            repositories {
                maven {
                    authentication {
                        credentials(PasswordCredentials::class) {
                            username = System.getenv("NEXUS_USERNAME")
                            password = System.getenv("NEXUS_PASSWORD")
                        }
                    }

                    setUrl(publishData.getRepository())
                    name = "EldoNexus"
                }
            }
        }

    }

    // We configure some general tasks for our modules
    tasks {
        test {
            dependsOn(spotlessCheck)
            useJUnitPlatform()
            testLogging {
                events("passed", "skipped", "failed")
            }
        }

        compileJava {
            dependsOn(spotlessApply)
        }

        javadoc {
            applyJavaDocOptions(options)
        }
    }
}

fun applyJavaDocOptions(options: MinimalJavadocOptions) {
    val javaDocOptions = options as StandardJavadocDocletOptions
    javaDocOptions.links(
            "https://javadoc.io/doc/com.google.code.findbugs/jsr305/latest/",
            "https://javadoc.io/doc/org.jetbrains/annotations/latest/",
            "https://docs.oracle.com/en/java/javase/${java.toolchain.languageVersion.get().asInt()}/docs/api/"
    )
}

tasks {
    register<Javadoc>("alljavadoc") {
        applyJavaDocOptions(options)

        setDestinationDir(file("${buildDir}/docs/javadoc"))
        val projects = project.rootProject.allprojects.filter { p -> !p.name.contains("example") }
        setSource(projects.map { p -> p.sourceSets.main.get().allJava })
        classpath = files(projects.map { p -> p.sourceSets.main.get().compileClasspath })
    }
}
