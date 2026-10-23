plugins {
    id("sadu.java-conventions")
    id("sadu.publishing-conventions")
}

description = "SADU module for executing and handling queries"

dependencies {
    api(project(":sadu-core"))
    api(project(":sadu-mapper"))
    testImplementation(project(":sadu-datasource"))
    testImplementation(project(":sadu-postgresql"))

    testImplementation(libs.bundles.database.postgresql)
}
