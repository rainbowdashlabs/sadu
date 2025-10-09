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
    id("org.gradle.toolchains.foojay-resolver-convention") version ("1.0.0")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // plugins
            plugin("spotless", "com.diffplug.spotless").version("8.0.0")
            plugin("shadow", "com.github.johnrengelman.shadow").version("8.1.1")

            library("jetbrains-annotations", "org.jetbrains", "annotations").version("26.0.2-1")
            library("hikari", "com.zaxxer", "HikariCP").version("7.0.2")

            version("slf4j", "2.0.16")
            library("slf4j-api", "org.slf4j", "slf4j-api").versionRef("slf4j")
        }

        create("testlibs") {
            version("junit", "6.0.0")
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("junit-api", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
            library("junit-platform", "org.junit.platform", "junit-platform-launcher").versionRef("junit")
            library("junit-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("junit-params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")
            bundle("junit", listOf("junit-jupiter", "junit-params", "slf4j-simple"))

            library("mokito","org.mockito", "mockito-core").version("5.+")

            version("testcontainers", "1.21.3")
            library("testcontainers-postgres", "org.testcontainers", "postgresql").versionRef("testcontainers")
            library("testcontainers-mariadb", "org.testcontainers", "mariadb").versionRef("testcontainers")
            library("testcontainers-mysql", "org.testcontainers", "mysql").versionRef("testcontainers")
            library("testcontainers-core", "org.testcontainers", "testcontainers").versionRef("testcontainers")
            library("testcontainers-junit", "org.testcontainers", "junit-jupiter").versionRef("testcontainers")

            version("slf4j", "2.0.16")
            library("slf4j-noop", "org.slf4j", "slf4j-nop").versionRef("slf4j")
            library("slf4j-simple", "org.slf4j", "slf4j-simple").versionRef("slf4j")

            library("driver-postgres", "org.postgresql:postgresql:42.7.8")
            library("driver-mariadb", "org.mariadb.jdbc:mariadb-java-client:3.5.6")
            library("driver-sqlite", "org.xerial:sqlite-jdbc:3.50.3.0")
            library("driver-mysql", "com.mysql:mysql-connector-j:9.4.0")

            bundle(
                "database-postgres",
                listOf("testcontainers-junit", "testcontainers-core", "testcontainers-postgres", "driver-postgres")
            )
            bundle(
                "database-mariadb",
                listOf("testcontainers-junit", "testcontainers-core", "testcontainers-mariadb", "driver-mariadb")
            )
            bundle(
                "database-mysql",
                listOf("testcontainers-junit", "testcontainers-core", "testcontainers-mysql", "driver-mysql")
            )
        }
    }
}
