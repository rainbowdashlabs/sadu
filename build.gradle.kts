plugins {
    java
    `maven-publish`
    `java-library`
    id("de.chojo.publishdata") version "1.0.4"
    id("org.cadixdev.licenser") version "0.6.1"
}

group = "de.chojo"
version = "1.2.0"
val testContainersVersion = "1.15.3"

repositories {
    mavenCentral()
}

license {
    header(rootProject.file("HEADER.txt"))
    include("**/*.java")
}

dependencies {
    api("org.slf4j", "slf4j-api", "1.7.36")

    api("com.zaxxer", "HikariCP", "5.0.1")
    api("org.jetbrains", "annotations", "21.0.1")

    testImplementation("org.postgresql", "postgresql", "42.2.22")
    testImplementation("org.mariadb.jdbc", "mariadb-java-client", "2.7.5")
    testImplementation("mysql", "mysql-connector-java", "8.0.28")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // testcontainers
    testImplementation("org.testcontainers", "testcontainers", testContainersVersion)
    implementation(platform("org.testcontainers:testcontainers-bom:$testContainersVersion"))
    // container
    testImplementation("org.testcontainers", "mysql", testContainersVersion)
    testImplementation("org.testcontainers", "mariadb", testContainersVersion)
    testImplementation("org.testcontainers", "postgresql", testContainersVersion)
}

publishData {
    useEldoNexusRepos()
    publishComponent("java")
}

publishing {
    publications.create<MavenPublication>("maven") {
        publishData.configurePublication(this)
    }

    repositories {
        maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    username = System.getenv("NEXUS_USERNAME")
                    password = System.getenv("NEXUS_PASSWORD")
                }
            }

            name = "EldoNexus"
            setUrl(publishData.getRepository())
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()

    sourceCompatibility = JavaVersion.VERSION_11;
}


tasks{
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
