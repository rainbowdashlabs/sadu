![GitHub Workflow Status](https://img.shields.io/github/workflow/status/RainbowDashLabs/sadu/Verify%20state?style=for-the-badge&label=Build)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/RainbowDashLabs/sadu/Publish%20to%20Nexus?style=for-the-badge&label=Publish)
[![Sonatype Nexus (Releases)](https://img.shields.io/nexus/maven-releases/de.chojo/sadu?label=Release&logo=Release&server=https%3A%2F%2Feldonexus.de&style=for-the-badge)][nexus_releases]
[![Sonatype Nexus (Development)](https://img.shields.io/nexus/maven-dev/de.chojo/sadu?label=DEV&logo=Release&server=https%3A%2F%2Feldonexus.de&style=for-the-badge)][nexus_dev]
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/de.chojo/sadu?color=orange&label=Snapshot&server=https%3A%2F%2Feldonexus.de&style=for-the-badge)][nexus_releases]

### [Javadocs](https://rainbowdashlabs.github.io/sadu/)

# SADU - Sql and damn utilitites

This project contains serveral things I use for working with sql.

The project is divided in several subprojects which allow to only import what you need.

It is by far not a replacement for large Frameworks like Hibernate, but a solid ground to reduce boilerplate code when
you work with plain SQL like I do most of the time.

# Dependency

If you want to use all projects simply import the whole thing.

```gradle
maven("https://eldonexus.de/repository/maven-public")

implementation("de.chojo.sadu", "sadu", "version")
```

## Database dependencies

SADU offers support for four different databases at the moment. To use them simply import:

- `sadu-postgres`
- `sadu-mariadb`
- `sadu-mysql`
- `sadu-sqlite`

## Querybuilder
SADU offers a query builder to manage resources, error handling, result set reading and dispatching of queries.

to use it import: `sadu-queries`

Learn how to use the query builder [here](https://github.com/RainbowDashLabs/sadu/wiki/SADU-Queries)

### But why should I use it?

# Why use the query builder?
Before I give you a long talk about how much nicer the syntax and code is let me simple show you a comparison.

Without the query builder your code would ideally look like this:
```java
class MyQueries {
    
    DataSource dataSource;
    
    MyQueries(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public CompletableFuture<Optional<Result>> getResultOld(int id) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = source().getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT result FROM results WHERE id = ?")) {
                stmt.setInt(id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(new Result(rs.getString("result"));
                }
            } catch (SQLException e) {
                logger.error("Something went wrong", e);
            }
            return Optional.empty();
        });
    }
}
```

But using the query builder your code becomes this:
```java
class MyQueries extends QueryFactory {
    MyQueries(DataSource dataSource){
        super(dataSource);
    }

    public CompletableFuture<Optional<Result>> getResultNew(int id) {
        return builder(Result.class)
                .query("SELECT result FROM results WHERE id = ?")
                .parameters(stmt -> stmt.setInt(id))
                .readRow(rs -> new Result(rs.getString("result")))
                .first();
    }
}
```

Beautiful isnt it? The query builder will enforce try with resources, set parameters in the order defined by you,
read the result set and additionally handle the exceptions for you.

[How does it work?](https://github.com/RainbowDashLabs/sadu/wiki/SADU-Queries#how-does-it-work)

## Datasource Builder
SADU offsers a data source builder to create data sources for the databases listed above.

to use it import: `sadu-datasource`

Note that in order to use this, you need at least one of the listed databases from above.

Learn how to use the datasource builder [here](https://github.com/RainbowDashLabs/sadu/wiki/SADU-Datasource)

## Updater

SADU offers a simple sql updater which deploys upgrade and migration scripts to your database.

to use it import: `sadu-updater`

Learn how to use it [here](https://github.com/RainbowDashLabs/sadu/wiki/SADU-Updater)


[nexus_releases]: https://eldonexus.de/#browse/browse:maven-releases:de%2Fchojo%2Fsadu
[nexus_snapshots]: https://eldonexus.de/#browse/browse:maven-snapshots:de%2Fchojo%2Fsadu
[nexus_dev]: https://eldonexus.de/#browse/browse:maven-dev:de%2Fchojo%2Fsadu
