dependencies {
    api(project(":sadu-updater"))

    testImplementation("org.mariadb.jdbc", "mariadb-java-client", "3.1.3")
    testImplementation(project(":sadu-queries"))
}
