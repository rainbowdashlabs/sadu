import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.MavenPublishBaseExtension

plugins {
    id("com.vanniktech.maven.publish")
}

if (project.version.toString() == "unspecified") {
    project.version = rootProject.version
}

extensions.configure<MavenPublishBaseExtension>("mavenPublishing") {
    configure(
        JavaLibrary(javadocJar = JavadocJar.Javadoc(), sourcesJar = true)
    )
}

afterEvaluate {
    extensions.configure<MavenPublishBaseExtension>("mavenPublishing") {
        publishToMavenCentral()
        signAllPublications()

        logger.warn("configured: ${rootProject.group as String}:${project.name}:${rootProject.version as String}")

        coordinates(
            groupId = rootProject.group as String,
            artifactId = project.name,
            version = rootProject.version as String
        )

        pom {
            name.set("Sadu")
            description.set(project.description)
            inceptionYear.set("2025")
            url.set("https://github.com/rainbowdashlabs/sadu")

            licenses {
                license {
                    name.set("LGPL-3.0")
                    url.set("https://opensource.org/license/lgpl-3-0")
                }
            }

            developers {
                developer {
                    id.set("rainbowdashlabs")
                    name.set("Nora Fülling")
                    email.set("mail@chojo.dev")
                    url.set("https://github.com/rainbowdashlabs")
                }
            }

            scm {
                url.set("https://github.com/rainbowdashlabs/sadu")
                connection.set("scm:git:git://github.com/rainbowdashlabs/sadu.git")
                developerConnection.set("scm:git:ssh://github.com/rainbowdashlabs/sadu.git")
            }
        }
    }
}

//logger.warn("configured: ${rootProject.group}:${project.name}:${rootProject.version}")
