plugins {
    id("sadu.java-conventions")
    id("sadu.publishing-conventions")
}

description = "SADU module to create a hikaricp datasource"

dependencies {
    api(libs.hikari)
    api(project(":sadu-core"))

    testImplementation(project(":sadu-sqlite"))
    testImplementation(libs.driver.sqlite)
}
