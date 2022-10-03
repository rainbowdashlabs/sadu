dependencies {
    api(project(":sadu-updater"))

    testImplementation("org.mariadb.jdbc", "mariadb-java-client", "3.0.8")
    testImplementation(project(":sadu-queries"))
}
