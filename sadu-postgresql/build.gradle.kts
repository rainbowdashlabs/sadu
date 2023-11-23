description = "SADU module for interaction with a PostgreSQL database"

dependencies {
    api(project(":sadu-updater"))

    testImplementation("io.zonky.test", "embedded-postgres", "2.0.4")
    testImplementation("org.postgresql", "postgresql", "42.7.0")
    testImplementation(testlibs.bundles.junit)
    testImplementation(project(":sadu-queries"))
    testImplementation(project(":sadu-datasource"))

    testImplementation(testlibs.testcontainers.postgres)
    testImplementation(testlibs.testcontainers.core)
    testImplementation(testlibs.testcontainers.junit)
    testImplementation(testlibs.slf4j.noop)
}
