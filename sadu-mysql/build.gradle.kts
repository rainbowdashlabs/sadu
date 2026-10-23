plugins {
    id("sadu.java-conventions")
    id("sadu.publishing-conventions")
}

description = "SADU module for interaction with a MySQL database"

dependencies {
    api(project(":sadu-updater"))
    api(project(":sadu-mapper"))
    testImplementation(project(":sadu-datasource"))
    testImplementation(libs.bundles.database.mysql)

}
