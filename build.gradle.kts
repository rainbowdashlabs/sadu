import com.diffplug.gradle.spotless.SpotlessPlugin
import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.MavenPublishPlugin
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    java
    `java-library`
    id("de.chojo.publishdata") version "1.4.0"
    id("com.vanniktech.maven.publish") version "0.30.0"
    alias(libs.plugins.spotless)
}

publishData {
    useEldoNexusRepos(false)
    publishingVersion = "2.3.2"
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
            plugin<SigningPlugin>()
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
        "https://javadoc.io/doc/org.jetbrains/annotations/latest/",
        "https://docs.oracle.com/en/java/javase/${java.toolchain.languageVersion.get().asInt()}/docs/api/"
    )
}

tasks {
    register<Javadoc>("alljavadoc") {
        applyJavaDocOptions(options)

        setDestinationDir(file("${layout.buildDirectory.get()}/docs/javadoc"))
        val projects = project.rootProject.allprojects.filter { p -> !p.name.contains("example") }
        setSource(projects.map { p -> p.sourceSets.main.get().allJava.filter { p -> p.name != "module-info.java" } })
        classpath = files(projects.map { p -> p.sourceSets.main.get().compileClasspath })
    }
}
