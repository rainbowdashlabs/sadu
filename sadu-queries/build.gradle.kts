
dependencies {
    api(project(":sadu-core"))
    api(project(":sadu-mapper"))

    testImplementation(project(":sadu-datasource"))
    testImplementation(project(":sadu-sqlite"))
    testImplementation("org.xerial", "sqlite-jdbc", "3.39.2.0")
}
