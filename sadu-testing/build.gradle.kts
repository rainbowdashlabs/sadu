description = "SADU module for checking validity of updater files"

dependencies {

    api(project(":sadu-core"))
    api(platform(testlibs.junit.bom))
    api(testlibs.junit.jupiter)
    testImplementation(platform(testlibs.junit.bom))
    testImplementation(testlibs.junit.jupiter)
    testImplementation(project(":sadu-postgresql"))
}
