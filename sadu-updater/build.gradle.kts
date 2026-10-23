plugins {
    id("sadu.java-conventions")
    id("sadu.publishing-conventions")
}

description = "SADU updater to setup and update your database using patch files."

dependencies {
    api(project(":sadu-core"))

    testImplementation(project(":sadu-postgresql"))
}

tasks{
    test{
        // TODO: Remove once tests are present
        failOnNoDiscoveredTests = false
    }
}
