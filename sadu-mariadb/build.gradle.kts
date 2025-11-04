plugins {
    id("sadu.java-conventions")
    id("sadu.maven-publish-conventions")
}

description = "SADU module for interaction with a MariaDB database"

dependencies {
    api(project(":sadu-updater"))
    api(project(":sadu-mapper"))

    testImplementation(project(":sadu-queries"))
    testImplementation(project(":sadu-datasource"))
    testImplementation(testlibs.bundles.database.mariadb)
    testImplementation(testlibs.slf4j.noop)
    testImplementation(testlibs.bundles.junit)
}
