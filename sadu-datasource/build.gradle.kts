dependencies {
    api("com.zaxxer", "HikariCP", "5.0.1")
    api(project(":sadu-core"))

    testImplementation(project(":sadu-sqlite"))
    testImplementation("org.xerial:sqlite-jdbc:3.41.0.1")
}
