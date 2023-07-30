dependencies {

    api(platform("org.junit:junit-bom:5.9.1"))
    api("org.junit.jupiter:junit-jupiter")
    api(project(":sadu-core"))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
