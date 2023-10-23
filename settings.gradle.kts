rootProject.name = "sadu"

include("sadu-core")
include("sadu-sqlite")
include("sadu-postgresql")
include("sadu-mariadb")
include("sadu-mysql")
include("sadu-datasource")
include("sadu-queries")
include("sadu-updater")
include("sadu-mapper")
include("sadu-examples")
include("sadu-testing")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven {
            name = "EldoNexus"
            url = uri("https://eldonexus.de/repository/maven-public/")

        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.7.0")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {

            // plugins
            plugin("spotless", "com.diffplug.spotless").version("6.22.0")
            plugin("shadow", "com.github.johnrengelman.shadow").version("8.1.1")

            version("indra", "3.1.3")
            plugin("indra-core", "net.kyori.indra").versionRef("indra")
            plugin("indra-publishing", "net.kyori.indra.publishing").versionRef("indra")
            plugin("indra-sonatype", "net.kyori.indra.publishing.sonatype").versionRef("indra")

            create("testlibs") {
                version("junit", "5.9.3")
                library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
                library("junit-params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")
                bundle("junit", listOf("junit-jupiter", "junit-params"))
            }
        }
    }
}
