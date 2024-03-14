dependencies {
    api(project(":sadu-core"))
    api(project(":sadu-mapper"))
    testImplementation("org.postgresql", "postgresql", "42.7.3")
    testImplementation(testlibs.bundles.junit)
    testImplementation(project(":sadu-datasource"))
    testImplementation(project(":sadu-postgresql"))

    testImplementation(testlibs.bundles.database.postgres)
    testImplementation(testlibs.slf4j.noop)
}
