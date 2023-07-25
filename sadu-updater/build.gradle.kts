description = "SADU updater to setup and update your database using patch files."

dependencies {
    api(project(":sadu-queries"))

    testImplementation(project(":sadu-postgresql"))
}
