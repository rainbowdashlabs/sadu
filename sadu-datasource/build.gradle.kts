description = "SADU module to create a hikaricp datasource"

dependencies {
    api("com.zaxxer", "HikariCP", "6.2.1")
    api(project(":sadu-core"))

    testImplementation(project(":sadu-sqlite"))
    testImplementation(testlibs.driver.sqlite)
}
