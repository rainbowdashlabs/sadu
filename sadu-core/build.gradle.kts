plugins {
    id("sadu.java-conventions")
    id("sadu.publishing-conventions")
}

description = "SADU core module, containing common logic between modules."

dependencies {
    api(libs.slf4j.api)
    api(libs.jetbrains.annotations)
}
