plugins {
    alias(libs.plugins.aggregate.javadoc)
    id("sadu.java-conventions")
    id("sadu.publishing-conventions")
}

description = "SADU with every module except sadu-testing. Its Javadoc covers all modules."

dependencies {
    api(project(":sadu-core"))
    api(project(":sadu-datasource"))
    api(project(":sadu-mapper"))
    api(project(":sadu-queries"))
    api(project(":sadu-updater"))
    api(project(":sadu-postgresql"))
    api(project(":sadu-mariadb"))
    api(project(":sadu-mysql"))
    api(project(":sadu-sqlite"))

    javadoc(project(":sadu-core"))
    javadoc(project(":sadu-datasource"))
    javadoc(project(":sadu-mapper"))
    javadoc(project(":sadu-queries"))
    javadoc(project(":sadu-updater"))
    javadoc(project(":sadu-postgresql"))
    javadoc(project(":sadu-mariadb"))
    javadoc(project(":sadu-mysql"))
    javadoc(project(":sadu-sqlite"))
    javadoc(project(":sadu-testing"))
    javadocClasspath(libs.apiguardian)
    javadocClasspath(libs.jspecify)
}
