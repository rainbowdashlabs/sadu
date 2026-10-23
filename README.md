# SADU

[![Maven Central](https://img.shields.io/maven-central/v/de.chojo.sadu/sadu?style=for-the-badge&label=Release)](https://central.sonatype.com/artifact/de.chojo.sadu/sadu)
[![Snapshot](https://img.shields.io/nexus/s/de.chojo.sadu/sadu?server=https%3A%2F%2Feldonexus.de&style=for-the-badge&label=Snapshot&color=orange)](https://eldonexus.de/#browse/browse:maven-snapshots:de%2Fchojo%2Fsadu)

SQL and damn utilities. SADU removes the boilerplate of plain JDBC while you keep writing your own SQL: HikariCP data
sources, a query builder that handles resources, parameters, results and errors, row mapping, and an updater for
your database schema. It supports PostgreSQL, MariaDB, MySQL and SQLite and is split into modules, so you only pull
in what you use.

**Documentation: [docs.chojo.dev/sadu](https://docs.chojo.dev/sadu)**

## Requirements

- Java 21 or newer
- The JDBC driver of your database

## Dependency

Gradle (Kotlin DSL):

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("de.chojo.sadu:sadu-postgresql:2.4.0")
    implementation("de.chojo.sadu:sadu-queries:2.4.0")
}
```

Maven:

```xml
<dependency>
    <groupId>de.chojo.sadu</groupId>
    <artifactId>sadu-postgresql</artifactId>
    <version>2.4.0</version>
</dependency>
```

`de.chojo.sadu:sadu` brings every module except `sadu-testing`. Snapshots are on EldoNexus:
`https://eldonexus.de/repository/maven-snapshots/`.

## Contributing and releases

See [CONTRIBUTING.md](CONTRIBUTING.md). Versions come from publishdata: a `vX.Y.Z` tag publishes the release, `main`
publishes snapshots.

## License

LGPL-3.0-or-later, see [LICENSE.md](LICENSE.md).
