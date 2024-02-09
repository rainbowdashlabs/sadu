![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/rainbowdashlabs/sadu/publish.yml?style=for-the-badge&label=Publishing&branch=main)
![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/rainbowdashlabs/sadu/verify.yml?style=for-the-badge&label=Building&branch=main)

[![Sonatype Nexus (Releases)](https://img.shields.io/maven-central/v/de.chojo.sadu/sadu?label=Release&logo=Release&style=for-the-badge)][nexus_releases]
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/de.chojo.sadu/sadu?server=https%3A%2F%2Fs01.oss.sonatype.org%2F&style=for-the-badge&label=Snapshot&color=orange)][nexus_snapshots]

### [Javadocs](https://rainbowdashlabs.github.io/sadu/)

# SADU - Sql and damn utilitites

This project contains serveral things I use for working with sql.

The project is divided in several subprojects which allow to only import what you need.

It is by far not a replacement for large Frameworks like Hibernate, but a solid ground to reduce boilerplate code when
you work with plain SQL like I do most of the time.

# Dependency

If you want to use all projects simply import the whole thing.

```gradle
implementation("de.chojo.sadu", "sadu", "version")
```

## Database dependencies

SADU offers support for four different databases at the moment. To use them simply import:

- `sadu-postgresql`
- `sadu-mariadb`
- `sadu-mysql`
- `sadu-sqlite`

## Querybuilder

SADU offers a query builder to manage resources, error handling, result set reading and dispatching of queries.

to use it import: `sadu-queries`

Learn how to use the query builder [here](https://github.com/RainbowDashLabs/sadu/wiki/SADU-Queries)

### But why should I use it?

Before I give you a long talk about how much nicer the syntax and code is let me simple show you a comparison.

Without the query builder your code would ideally look like this:

```java
class MyQueries {

    DataSource dataSource;

    MyQueries(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public CompletableFuture<Optional<Result>> getResultOld(int id) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = source().getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT result FROM results WHERE id = ?")) {
                stmt.setInt(id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(new Result(rs.getString("result")));
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
class MyQueries {
    public Optional<Result> getResultNew(int id) {
        return Query.query("SELECT result FROM results WHERE id = ?")
                .single(Call.of().bind(id))
                .map(row -> new Result(rs.getString("result")))
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

Learn how to use it [here](https://sadu.docs.chojo.dev/queries/)


[nexus_releases]: https://search.maven.org/search?q=de.chojo.sadu

[nexus_snapshots]: https://s01.oss.sonatype.org/#nexus-search;quick~de.chojo.sadu
