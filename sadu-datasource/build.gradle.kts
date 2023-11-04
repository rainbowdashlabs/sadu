description = "SADU module to create a hikaricp datasource"

dependencies {
    api("com.zaxxer", "HikariCP", "5.1.0")
    api(project(":sadu-core"))

    testImplementation(project(":sadu-sqlite"))
    testImplementation("org.xerial:sqlite-jdbc:3.42.0.0")
}
