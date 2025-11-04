
plugins {
    // don't change order!
    id("io.freefair.aggregate-javadoc") version("9.0.0")
    id("de.chojo.publishdata") version "1.4.0"
    id("sadu.java-conventions")
    id("sadu.maven-publish-conventions")
}

publishData {
    useEldoNexusRepos(false)
    publishingVersion = "2.3.4"
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

    javadoc(project(":sadu-core"))
    javadoc(project(":sadu-datasource"))
    javadoc(project(":sadu-mapper"))
    javadoc(project(":sadu-mariadb"))
    javadoc(project(":sadu-mysql"))
    javadoc(project(":sadu-postgresql"))
    javadoc(project(":sadu-queries"))
    javadoc(project(":sadu-sqlite"))
    javadoc(project(":sadu-testing"))
    javadocClasspath("org.apiguardian:apiguardian-api:1.1.2")
    javadoc(project(":sadu-updater"))
}
