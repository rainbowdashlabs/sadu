description = "SADU module for interaction with a PostgreSQL database"

dependencies {
    api(project(":sadu-updater"))

    testImplementation("io.zonky.test", "embedded-postgres", "2.0.4")
    testImplementation("org.postgresql", "postgresql", "42.6.0")
    testImplementation(testlibs.bundles.junit)
    testImplementation(project(":sadu-queries"))
}
