plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://eldonexus.de/repository/maven-public/")
}

dependencies {
    // Third-party plugins used inside convention plugins
    implementation("com.diffplug.spotless:spotless-plugin-gradle:7.0.2")
    implementation("com.vanniktech:gradle-maven-publish-plugin:0.30.0")
}