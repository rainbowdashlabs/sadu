dependencies {
    compileOnly(project(":sadu-queries"))
    compileOnly(project(":sadu-datasource"))
    compileOnly(project(":sadu-updater"))
    compileOnly(project(":sadu-postgresql"))

    // database driver
    compileOnly("org.xerial", "sqlite-jdbc", "3.47.0.0")
    compileOnly("org.postgresql", "postgresql", "42.7.4")
    compileOnly("org.mariadb.jdbc", "mariadb-java-client", "3.5.1")
    compileOnly("mysql", "mysql-connector-java", "8.0.33")
}
