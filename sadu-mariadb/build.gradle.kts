description = "SADU module for interaction with a MariaDB database"

dependencies {
    api(project(":sadu-updater"))

    testImplementation("org.mariadb.jdbc", "mariadb-java-client", "3.2.0")
    testImplementation(project(":sadu-queries"))
}
