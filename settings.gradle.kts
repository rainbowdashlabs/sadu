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

pluginManagement{
    repositories{
        mavenLocal()
        gradlePluginPortal()
        maven{
            name = "EldoNexus"
            url = uri("https://eldonexus.de/repository/maven-public/")

        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.6.0")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {

            // plugins
            plugin("spotless", "com.diffplug.spotless").version("6.19.0")
            plugin("shadow", "com.github.johnrengelman.shadow").version("8.1.1")

        }
    }
}
