dependencies {
    compileOnly(project(":sadu-queries"))
    compileOnly(project(":sadu-datasource"))
    compileOnly(project(":sadu-updater"))
    compileOnly(project(":sadu-postgresql"))

    // database driver
    compileOnly(testlibs.driver.sqlite)
    compileOnly(testlibs.driver.postgres)
    compileOnly(testlibs.driver.mariadb)
    compileOnly(testlibs.driver.mysql)
}
