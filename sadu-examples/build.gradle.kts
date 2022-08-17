dependencies {
    compileOnly(project(":"))

    // database driver
    compileOnly("org.xerial", "sqlite-jdbc", "3.39.2.0")
    compileOnly("org.postgresql", "postgresql", "42.4.1")
    compileOnly("org.mariadb.jdbc", "mariadb-java-client", "3.0.7")
    compileOnly("mysql", "mysql-connector-java", "8.0.30")
}

publishing {
    publications.clear()
}
