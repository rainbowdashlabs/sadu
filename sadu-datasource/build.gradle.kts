description = "SADU module to create a hikaricp datasource"

dependencies {
    api("com.zaxxer", "HikariCP", "5.1.0")
    api(project(":sadu-core"))

    testImplementation(project(":sadu-sqlite"))
    testImplementation(testlibs.sqlite)
    testImplementation(testlibs.testcontainers.postgres)
    testImplementation(testlibs.testcontainers.core)
    testImplementation(testlibs.testcontainers.junit)
}
