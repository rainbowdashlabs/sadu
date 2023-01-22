
dependencies {
    api(project(":sadu-core"))
    api(project(":sadu-mapper"))

    testImplementation(project(":sadu-datasource"))
    testImplementation(project(":sadu-sqlite"))
    testImplementation(project(":sadu-mapper"))
    testImplementation("org.xerial", "sqlite-jdbc", "3.40.0.0")
}
