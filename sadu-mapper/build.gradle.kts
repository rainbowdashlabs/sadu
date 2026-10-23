plugins {
    id("sadu.java-conventions")
    id("sadu.publishing-conventions")
}

description = "SADU module to map values received from a database to java objects using the queries module."

dependencies {
    api(project(":sadu-core"))

    testImplementation(project(":sadu-queries"))
    testImplementation(project(":sadu-datasource"))
    testImplementation(project(":sadu-postgresql"))

    testImplementation(libs.bundles.database.postgresql)
}
