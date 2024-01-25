import com.diffplug.gradle.spotless.SpotlessPlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin

plugins {
    java
    `maven-publish`
    `java-library`
    id("de.chojo.publishdata") version "1.4.0"
    alias(libs.plugins.spotless)
    alias(libs.plugins.indra.core)
    alias(libs.plugins.indra.publishing)
    alias(libs.plugins.indra.sonatype)
}

publishData {
    useEldoNexusRepos(false)
    publishingVersion = "1.4.1"
}

group = "de.chojo.sadu"
version = publishData.getVersion()
description = "The SADU library, containing everything you need and not need."

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
                target(17)
                testWith(17)
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
                            url.set("https://github.com/rainbowdashlabs")
                        }
                    }
                }
            }
        }
    }
}

indra {
    javaVersions {
        target(17)
        testWith(17)
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
                    url.set("https://github.com/rainbowdashlabs")
                }
            }
        }
    }
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

    publishData {
        useEldoNexusRepos(false)
    }


    dependencies {
        testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.10.1")
        testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.10.1")
        testImplementation("org.mockito", "mockito-core", "5.+")
    }

    spotless {
        java {
            licenseHeaderFile(rootProject.file("HEADER.txt"))
            target("**/*.java")
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

        setDestinationDir(file("${layout.buildDirectory}/docs/javadoc"))
        val projects = project.rootProject.allprojects.filter { p -> !p.name.contains("example") }
        setSource(projects.map { p -> p.sourceSets.main.get().allJava })
        classpath = files(projects.map { p -> p.sourceSets.main.get().compileClasspath })
    }
}
