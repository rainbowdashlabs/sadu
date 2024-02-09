description = "SADU module for checking validity of updater files"

dependencies {

    api(platform("org.junit:junit-bom:5+"))
    api("org.junit.jupiter:junit-jupiter")
    api(project(":sadu-core"))
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(project(":sadu-postgresql"))
}
