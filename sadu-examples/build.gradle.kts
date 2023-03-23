dependencies {
    compileOnly(project(":"))

    // database driver
    compileOnly("org.xerial", "sqlite-jdbc", "3.41.2.0")
    compileOnly("org.postgresql", "postgresql", "42.6.0")
    compileOnly("org.mariadb.jdbc", "mariadb-java-client", "3.1.3")
    compileOnly("mysql", "mysql-connector-java", "8.0.32")
}

publishing {
    publications.clear()
}
