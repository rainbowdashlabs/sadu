dependencies {
    api("com.zaxxer", "HikariCP", "5.0.1")
    api(project(":sadu-core"))

    testImplementation(project(":sadu-sqlite"))
    testImplementation("org.xerial:sqlite-jdbc:3.42.0.0")
}
