description = "SADU module to send queries to a database."

dependencies {
    api(project(":sadu-core"))
    api(project(":sadu-mapper"))

    testImplementation(project(":sadu-datasource"))
    testImplementation(project(":sadu-mariadb"))
    testImplementation(project(":sadu-mapper"))
    testImplementation(testlibs.bundles.database.mariadb)
    testImplementation(testlibs.slf4j.noop)
    testImplementation(testlibs.bundles.junit)
}
