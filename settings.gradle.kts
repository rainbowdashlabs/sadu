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
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.9.0")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {

            // plugins
            plugin("spotless", "com.diffplug.spotless").version("7.0.2")
            plugin("shadow", "com.github.johnrengelman.shadow").version("8.1.1")

            version("slf4j", "2.0.16")
            library("slf4j-api", "org.slf4j", "slf4j-api").versionRef("slf4j")
        }

        create("testlibs") {
            version("junit", "5.11.4")
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("junit-params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")
            bundle("junit", listOf("junit-jupiter", "junit-params", "slf4j-simple"))

            version("testcontainers", "1.20.4")
            library("testcontainers-postgres", "org.testcontainers", "postgresql").versionRef("testcontainers")
            library("testcontainers-mariadb", "org.testcontainers", "mariadb").versionRef("testcontainers")
            library("testcontainers-mysql", "org.testcontainers", "mysql").versionRef("testcontainers")
            library("testcontainers-core", "org.testcontainers", "testcontainers").versionRef("testcontainers")
            library("testcontainers-junit", "org.testcontainers", "junit-jupiter").versionRef("testcontainers")

            version("slf4j", "2.0.16")
            library("slf4j-noop", "org.slf4j", "slf4j-nop").versionRef("slf4j")
            library("slf4j-simple", "org.slf4j", "slf4j-simple").versionRef("slf4j")

            library("driver-postgres", "org.postgresql:postgresql:42.7.5")
            library("driver-mariadb", "org.mariadb.jdbc:mariadb-java-client:3.5.2")
            library("driver-sqlite", "org.xerial:sqlite-jdbc:3.49.0.0")
            library("driver-mysql", "com.mysql:mysql-connector-j:9.2.0")

            bundle("database-postgres", listOf("testcontainers-junit", "testcontainers-core", "testcontainers-postgres", "driver-postgres"))
            bundle("database-mariadb", listOf("testcontainers-junit", "testcontainers-core", "testcontainers-mariadb", "driver-mariadb"))
            bundle("database-mysql", listOf("testcontainers-junit", "testcontainers-core", "testcontainers-mysql", "driver-mysql"))
        }
    }
}
