plugins {
    id("sadu.java-conventions")
    id("sadu.maven-publish-conventions")
}

description = "SADU core module, containing common logic between modules."

dependencies {
    api(libs.slf4j.api)
    api("org.jetbrains", "annotations", "26.0.2")
}
