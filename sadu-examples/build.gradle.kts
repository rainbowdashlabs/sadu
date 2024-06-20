dependencies {
    compileOnly(project(":sadu-queries"))
    compileOnly(project(":sadu-datasource"))
    compileOnly(project(":sadu-updater"))
    compileOnly(project(":sadu-postgresql"))

    // database driver
    compileOnly("org.xerial", "sqlite-jdbc", "3.46.0.0")
    compileOnly("org.postgresql", "postgresql", "42.7.3")
    compileOnly("org.mariadb.jdbc", "mariadb-java-client", "3.4.0")
    compileOnly("mysql", "mysql-connector-java", "8.0.33")
}
