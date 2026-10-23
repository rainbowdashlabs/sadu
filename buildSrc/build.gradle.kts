plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://eldonexus.de/repository/maven-public/")
}

dependencies {
    implementation(libs.gradle.publishdata)
    implementation(libs.gradle.vanniktech)
    implementation(libs.gradle.spotless)
}
