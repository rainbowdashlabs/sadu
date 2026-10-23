
plugins {
    alias(libs.plugins.aggregate.javadoc)
    id("sadu.java-conventions")
    id("sadu.publishing-conventions")
}

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
    javadocClasspath(libs.apiguardian)
    javadoc(project(":sadu-updater"))
}
