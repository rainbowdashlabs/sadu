description = "SADU module for interaction with a PostgreSQL database"

dependencies {
    api(project(":sadu-updater"))
    api(project(":sadu-mapper"))

    testImplementation("org.postgresql", "postgresql", "42.7.1")
    testImplementation(testlibs.bundles.junit)
    testImplementation(project(":sadu-queries"))
    testImplementation(project(":sadu-datasource"))

    testImplementation(testlibs.bundles.database.postgres)
    testImplementation(testlibs.slf4j.noop)
}
