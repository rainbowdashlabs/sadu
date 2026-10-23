plugins {
    `java-library`
    id("com.diffplug.spotless")
}

val libs = the<VersionCatalogsExtension>().named("libs")

repositories {
    mavenCentral()
    maven("https://eldonexus.de/repository/maven-public/")
}

java {
    toolchain { languageVersion = JavaLanguageVersion.of(17) }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>().configureEach {
    (options as StandardJavadocDocletOptions).links("https://docs.oracle.com/en/java/javase/17/docs/api/")
}

dependencies {
    testImplementation(platform(libs.findLibrary("junit-bom").get()))
    testImplementation(libs.findLibrary("junit-jupiter").get())
    testImplementation(libs.findLibrary("mockito-core").get())
    testRuntimeOnly(libs.findLibrary("junit-launcher").get())
}

tasks.test {
    useJUnitPlatform()
    testLogging { events("passed", "skipped", "failed") }
}

tasks.withType<JavaCompile>().configureEach {
    dependsOn(tasks.spotlessApply)
}

spotless {
    java {
        licenseHeaderFile(rootProject.file("HEADER.txt"))
        target("**/*.java")
    }
}
