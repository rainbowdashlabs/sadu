plugins {
    id("sadu.java-conventions")
    id("sadu.publishing-conventions")
}

description = "SADU module for interaction with a SqLite database"

dependencies {
    api(project(":sadu-updater"))
    api(project(":sadu-mapper"))
}
