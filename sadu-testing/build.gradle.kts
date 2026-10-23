plugins {
    id("sadu.java-conventions")
    id("sadu.publishing-conventions")
}

description = "SADU module for checking validity of updater files"

dependencies {

    api(platform(libs.junit.bom))
    api(libs.junit.jupiter)
    api(project(":sadu-core"))
    testImplementation(project(":sadu-postgresql"))
}
