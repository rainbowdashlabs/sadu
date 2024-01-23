description = "SADU module for interaction with a MySQL database"

dependencies {
    api(project(":sadu-updater"))
    testImplementation(project(":sadu-datasource"))
    testImplementation(testlibs.bundles.database.mysql)
    testImplementation(testlibs.slf4j.noop)
    testImplementation(testlibs.bundles.junit)

}
