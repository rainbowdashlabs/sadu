description = "SADU module for executing and handling queries"

dependencies {
    api(project(":sadu-core"))
    api(project(":sadu-mapper"))
    testImplementation(testlibs.driver.postgres)
    testImplementation(testlibs.bundles.junit)
    testImplementation(project(":sadu-datasource"))
    testImplementation(project(":sadu-postgresql"))

    testImplementation(testlibs.bundles.database.postgres)
    testImplementation(testlibs.slf4j.noop)
}
