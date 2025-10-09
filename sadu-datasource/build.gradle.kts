description = "SADU module to create a hikaricp datasource"

dependencies {
    api(libs.hikari)
    api(project(":sadu-core"))

    testImplementation(project(":sadu-sqlite"))
    testImplementation(testlibs.driver.sqlite)
}
