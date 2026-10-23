import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar

plugins {
    id("com.vanniktech.maven.publish")
    id("de.chojo.publishdata")
}

publishData {
    eldoNexus(dev = false)
    channels {
        clear()
        release("https://eldonexus.de/repository/maven-releases/") { publishToMavenCentral() }
        snapshot("https://eldonexus.de/repository/maven-snapshots/")
    }
}

mavenPublishing {
    configure(JavaLibrary(javadocJar = JavadocJar.Javadoc(), sourcesJar = SourcesJar.Sources()))
    publishToMavenCentral()
    signAllPublications()
    coordinates(rootProject.group.toString(), project.name, project.version.toString())
    pom {
        name = project.name
        description = project.description
        url = "https://github.com/rainbowdashlabs/sadu"
        inceptionYear = "2021"
        licenses {
            license {
                name = "LGPL-3.0-or-later"
                url = "https://www.gnu.org/licenses/lgpl-3.0.html"
            }
        }
        developers {
            developer {
                id = "rainbowdashlabs"
                name = "Nora Fülling"
                email = "mail@chojo.dev"
                url = "https://github.com/rainbowdashlabs"
            }
        }
        scm {
            url = "https://github.com/rainbowdashlabs/sadu"
            connection = "scm:git:git://github.com/rainbowdashlabs/sadu.git"
            developerConnection = "scm:git:ssh://github.com/rainbowdashlabs/sadu.git"
        }
    }
}
