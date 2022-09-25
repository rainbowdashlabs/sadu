dependencies {
    api(project(":sadu-core"))
    api(project(":sadu-mapper"))

    testImplementation("org.mariadb.jdbc", "mariadb-java-client", "3.0.8")
    testImplementation(project(":sadu-queries"))
}
