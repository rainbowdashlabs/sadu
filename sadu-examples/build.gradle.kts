plugins {
    id("sadu.java-conventions")
}

dependencies {
    compileOnly(project(":sadu-queries"))
    compileOnly(project(":sadu-datasource"))
    compileOnly(project(":sadu-updater"))
    compileOnly(project(":sadu-postgresql"))

    compileOnly(libs.driver.sqlite)
    compileOnly(libs.driver.postgresql)
    compileOnly(libs.driver.mariadb)
    compileOnly(libs.driver.mysql)
}
