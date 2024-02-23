description = "SADU updater to setup and update your database using patch files."

dependencies {
    api(project(":sadu-core"))

    testImplementation(project(":sadu-postgresql"))
}
