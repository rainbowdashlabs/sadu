description = "SADU module to map values received from a database to java objects using the queries module."

dependencies {
    api(project(":sadu-core"))

    testImplementation(testlibs.driver.postgres)
    testImplementation(testlibs.bundles.junit)
    testImplementation(project(":sadu-queries"))
    testImplementation(project(":sadu-datasource"))
    testImplementation(project(":sadu-postgresql"))

    testImplementation(testlibs.bundles.database.postgres)
    testImplementation(testlibs.slf4j.noop)
}
