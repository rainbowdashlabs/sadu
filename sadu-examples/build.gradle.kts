dependencies {
    compileOnly(project(":"))

    // database driver
    compileOnly("org.xerial", "sqlite-jdbc", "3.39.4.1")
    compileOnly("org.postgresql", "postgresql", "42.5.1")
    compileOnly("org.mariadb.jdbc", "mariadb-java-client", "3.1.1")
    compileOnly("mysql", "mysql-connector-java", "8.0.32")
}

publishing {
    publications.clear()
}
