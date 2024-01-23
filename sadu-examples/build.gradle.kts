dependencies {
    compileOnly(project(":"))

    // database driver
    compileOnly("org.xerial", "sqlite-jdbc", "3.44.0.0")
    compileOnly("org.postgresql", "postgresql", "42.7.1")
    compileOnly("org.mariadb.jdbc", "mariadb-java-client", "3.3.2")
    compileOnly("mysql", "mysql-connector-java", "8.0.33")
}
