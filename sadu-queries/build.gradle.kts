description = "SADU module to send queries to a database."

dependencies {
    api(project(":sadu-core"))
    api(project(":sadu-mapper"))

    testImplementation(project(":sadu-datasource"))
    testImplementation(project(":sadu-sqlite"))
    testImplementation(project(":sadu-mapper"))
    testImplementation("org.xerial", "sqlite-jdbc", "3.42.0.0")
}
