rootProject.name = "sadu"

include("sadu-core")
include("sadu-sqlite")
include("sadu-postgres")
include("sadu-mariadb")
include("sadu-mysql")
include("sadu-datasource")
include("sadu-queries")
include("sadu-updater")

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
include("sadu-examples")
