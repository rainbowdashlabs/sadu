dependencies {
    api(project(":sadu-mapper"))
    api("com.fasterxml.jackson.core", "jackson-databind", "2.17.1")

    testImplementation(project(":sadu-queries"))
    testImplementation(project(":sadu-postgresql"))
    testImplementation(project(":sadu-mariadb"))
    testImplementation(project(":sadu-mysql"))
    testImplementation(project(":sadu-sqlite"))
    testImplementation(project(":sadu-datasource"))
    testImplementation(testlibs.bundles.database.postgres)
    testImplementation(testlibs.bundles.database.mariadb)
    testImplementation(testlibs.bundles.database.mysql)
    testImplementation(testlibs.driver.sqlite)
    testImplementation(testlibs.slf4j.noop)
    testImplementation(testlibs.bundles.junit)
    testImplementation(libs.jackson.jsr310)
}
