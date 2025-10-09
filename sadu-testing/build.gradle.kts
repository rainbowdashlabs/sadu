description = "SADU module for checking validity of updater files"

dependencies {

    api(project(":sadu-core"))
    api(testlibs.junit.jupiter)
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(testlibs.junit.jupiter)
    testImplementation(project(":sadu-postgresql"))
}
