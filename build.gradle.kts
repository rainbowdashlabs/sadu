plugins {
    java
    `maven-publish`
    `java-library`
}

group = "de.chojo"
version = "1.1.0"
val testContainersVersion = "1.15.3"

repositories {
    mavenCentral()
}

dependencies {
    api("org.slf4j", "slf4j-api", "1.7.30")

    api("com.zaxxer", "HikariCP", "4.0.3")
    api("org.jetbrains", "annotations", "21.0.1")

    testImplementation("org.postgresql", "postgresql", "42.2.22")
    testImplementation("org.mariadb.jdbc", "mariadb-java-client", "2.7.3")
    testImplementation("mysql", "mysql-connector-java", "8.0.25")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // testcontainers
    testImplementation("org.testcontainers", "testcontainers", testContainersVersion)
    implementation(platform("org.testcontainers:testcontainers-bom:$testContainersVersion"))
    // container
    testImplementation("org.testcontainers", "mysql", testContainersVersion)
    testImplementation("org.testcontainers", "mariadb", testContainersVersion)
    testImplementation("org.testcontainers", "postgresql", testContainersVersion)
}

publishing {
    val publishData = PublishData(project)
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        groupId = project.group as String?
        artifactId = project.name
        version = publishData.getVersion()
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
            url = uri(publishData.getRepository())
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()

    sourceCompatibility = JavaVersion.VERSION_11;
}


tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
class PublishData(private val project: Project) {
    var type: Type = getReleaseType()
    var hashLength: Int = 7

    private fun getReleaseType(): Type {
        val branch = getCheckedOutBranch()
        return when {
            branch.contentEquals("master") -> Type.RELEASE
            branch.startsWith("dev") -> Type.DEV
            else -> Type.SNAPSHOT
        }
    }

    private fun getCheckedOutGitCommitHash(): String = System.getenv("GITHUB_SHA")?.substring(0, hashLength) ?: "local"

    private fun getCheckedOutBranch(): String = System.getenv("GITHUB_REF")?.replace("refs/heads/", "") ?: "local"

    fun getVersion(): String = getVersion(false)

    fun getVersion(appendCommit: Boolean): String =
        type.append(getVersionString(), appendCommit, getCheckedOutGitCommitHash())

    private fun getVersionString(): String = (project.version as String).replace("-SNAPSHOT", "").replace("-DEV", "")

    fun getRepository(): String = type.repo

    enum class Type(private val append: String, val repo: String, private val addCommit: Boolean) {
        RELEASE("", "https://eldonexus.de/repository/maven-releases/", false),
        DEV("-DEV", "https://eldonexus.de/repository/maven-dev/", true),
        SNAPSHOT("-SNAPSHOT", "https://eldonexus.de/repository/maven-snapshots/", true);

        fun append(name: String, appendCommit: Boolean, commitHash: String): String =
            name.plus(append).plus(if (appendCommit && addCommit) "-".plus(commitHash) else "")
    }
}
