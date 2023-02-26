
dependencies {
    api(project(":sadu-core"))
    api(project(":sadu-mapper"))

    testImplementation(project(":sadu-datasource"))
    testImplementation(project(":sadu-sqlite"))
    testImplementation(project(":sadu-mapper"))
    testImplementation("org.xerial", "sqlite-jdbc", "3.41.0.0")
}
