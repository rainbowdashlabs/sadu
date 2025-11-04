plugins {
    java
    `java-library`
    id("com.diffplug.spotless")
}

repositories {
    mavenCentral()
    maven("https://eldonexus.de/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-proxies/")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

spotless {
    java {
        licenseHeaderFile(rootProject.file("HEADER.txt"))
        target("**/*.java")
    }
}

dependencies {
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "6.0.1")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "6.0.1")
    testRuntimeOnly("org.junit.platform", "junit-platform-launcher", "6.0.1")
    testImplementation("org.mockito", "mockito-core", "5.+")
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    withType<JavaCompile>().configureEach {
        dependsOn(spotlessApply)
    }

    withType<Javadoc>().configureEach {
        val options = options as StandardJavadocDocletOptions
        val version = project.extensions
            .findByType(JavaPluginExtension::class.java)
            ?.toolchain?.languageVersion?.orNull?.asInt()

        options.links(
            "https://javadoc.io/doc/org.jetbrains/annotations/latest/",
            "https://docs.oracle.com/en/java/javase/${version ?: 17}/docs/api/"
        )
    }
}
