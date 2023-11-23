description = "SADU module for interaction with a MariaDB database"

dependencies {
    api(project(":sadu-updater"))

    testImplementation(project(":sadu-queries"))
    testImplementation(project(":sadu-datasource"))
    testImplementation(testlibs.bundles.database.mariadb)
    testImplementation(testlibs.slf4j.noop)
    testImplementation(testlibs.bundles.junit)
}
