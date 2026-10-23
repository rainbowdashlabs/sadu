plugins {
    id("sadu.java-conventions")
    id("sadu.publishing-conventions")
}

description = "SADU module for interaction with a PostgreSQL database"

dependencies {
    api(project(":sadu-updater"))
    api(project(":sadu-mapper"))

    testImplementation(project(":sadu-queries"))
    testImplementation(project(":sadu-datasource"))

    testImplementation(libs.bundles.database.postgresql)
}
