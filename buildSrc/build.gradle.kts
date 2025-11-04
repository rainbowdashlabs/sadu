plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("com.diffplug.spotless:spotless-plugin-gradle:8.0.0")
    implementation("com.vanniktech:gradle-maven-publish-plugin:0.34.0")
}