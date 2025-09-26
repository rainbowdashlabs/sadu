import com.diffplug.gradle.spotless.SpotlessPlugin
import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.MavenPublishPlugin
import com.vanniktech.maven.publish.SonatypeHost
import de.chojo.PublishData

plugins {
    // don't change order!
    id("io.freefair.aggregate-javadoc") version("8.12.2.1")

    java
    `java-library`
    id("de.chojo.publishdata") version "1.4.0"
    id("com.vanniktech.maven.publish") version "0.30.0"
    alias(libs.plugins.spotless)
}

publishData {
    useEldoNexusRepos(false)
    publishingVersion = "2.3.3"
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

    javadoc(project(":sadu-sqlite"))
    javadoc(project(":sadu-postgresql"))
    javadoc(project(":sadu-mariadb"))
    javadoc(project(":sadu-mysql"))
    javadoc(project(":sadu-updater"))
    javadoc(project(":sadu-datasource"))
}


allprojects {
    apply {
        plugin<PublishData>()
        plugin<JavaPlugin>()
        plugin<SpotlessPlugin>()
        plugin<JavaLibraryPlugin>()
    }

    repositories {
        mavenCentral()
        maven("https://eldonexus.de/repository/maven-public/")
        maven("https://eldonexus.de/repository/maven-proxies/")
    }

    publishData {
        useEldoNexusRepos(false)
    }


    dependencies {
        testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.11.4")
        testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.11.4")
        testImplementation("org.mockito", "mockito-core", "5.+")
    }

    spotless {
        java {
            licenseHeaderFile(rootProject.file("HEADER.txt"))
            target("**/*.java")
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    logger.info("Configuring project ${project.name}")

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

subprojects {
    afterEvaluate {
        if (!project.name.contains("examples")) {
            logger.info("Configuring maven central publishing for de.chojo.sadu:${project.name}:${publishData.getVersion()}\nDescription: ${project.description}")
            apply {
                plugin<MavenPublishPlugin>()
            }

            mavenPublishing {
                publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
                signAllPublications()

                coordinates(groupId = "de.chojo.sadu", artifactId = project.name, version = publishData.getVersion())

                pom {
                    name.set("Sadu")
                    description.set(project.description)
                    inceptionYear.set("2025")
                    url.set("https://github.com/rainbowdashlabs/sadu")
                    licenses {
                        license {
                            name.set("LGPL-3.0")
                            url.set("https://opensource.org/license/lgpl-3-0")
                        }
                    }

                    developers {
                        developer {
                            id.set("rainbowdashlabs")
                            name.set("Lilly Fülling")
                            email.set("mail@chojo.dev")
                            url.set("https://github.com/rainbowdashlabs")
                        }
                    }

                    scm {
                        url.set("https://github.com/rainbowdashlabs/sadu")
                        connection.set("scm:git:git://github.com/rainbowdashlabs/sadu.git")
                        developerConnection.set("scm:git:ssh://github.com/racinbowdashlabs/sadu.git")
                    }
                }

                configure(
                    JavaLibrary(
                        javadocJar = JavadocJar.Javadoc(),
                        sourcesJar = true
                    )
                )
            }
        }
    }
}


mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()


    coordinates(groupId = "de.chojo.sadu", artifactId = project.name, version = publishData.getVersion())

    pom {
        name.set("Sadu")
        description.set(project.description)
        inceptionYear.set("2025")
        url.set("https://github.com/rainbowdashlabs/sadu")
        licenses {
            license {
                name.set("LGPL-3.0")
                url.set("https://opensource.org/license/lgpl-3-0")
            }
        }

        developers {
            developer {
                id.set("rainbowdashlabs")
                name.set("Lilly Fülling")
                email.set("mail@chojo.dev")
                url.set("https://github.com/rainbowdashlabs")
            }
        }

        scm {
            url.set("https://github.com/rainbowdashlabs/sadu")
            connection.set("scm:git:git://github.com/rainbowdashlabs/sadu.git")
            developerConnection.set("scm:git:ssh://github.com/racinbowdashlabs/sadu.git")
        }
    }

    configure(
        JavaLibrary(
            javadocJar = JavadocJar.Javadoc(),
            sourcesJar = true
        )
    )
}


fun applyJavaDocOptions(options: MinimalJavadocOptions) {
    val javaDocOptions = options as StandardJavadocDocletOptions
    javaDocOptions.links(
        "https://javadoc.io/doc/org.jetbrains/annotations/latest/",
        "https://docs.oracle.com/en/java/javase/${java.toolchain.languageVersion.get().asInt()}/docs/api/"
    )
}

tasks.javadoc.configure {
    applyJavaDocOptions(options)
}
