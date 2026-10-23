---
title: Data source
sidebar_position: 3
---

# Data source

`sadu-datasource` builds a [HikariCP](https://github.com/brettwooldridge/HikariCP) connection pool for your database.
It creates the JDBC URL for you, so you do not need to remember the URL format of each driver.

Add `de.chojo.sadu:sadu-datasource:2.4.0` and the module of your database (see [Databases](./databases.mdx)).

## Create a data source

The `DataSourceCreator` works in two stages:

1. `configure(...)` sets up the connection: host, port, database, login and driver options.
2. After `create()` you set up the pool, then call `build()`.

```java
HikariDataSource dataSource = DataSourceCreator.create(PostgreSql.get())
        .configure(config -> config
                .host("localhost")
                .port(5432)
                .database("app")
                .user("app")
                .password("secret")
                .currentSchema("app")
                .applicationName("my-app"))
        .create()
        .withMaximumPoolSize(3)
        .withMinimumIdle(1)
        .build();
```

The methods in `configure(...)` depend on the database you pass to `create(...)`. `currentSchema` and
`applicationName` exist only for PostgreSQL, for example.

Every value you do not set keeps the default of the JDBC driver or of HikariCP.

## Connection settings

These work for PostgreSQL, MariaDB and MySQL:

| Method | What it sets |
|---|---|
| `host(String)` | Host name. The default is `localhost`. |
| `localhost()` | Host `localhost`. |
| `ipv4(String)`, `ipv6(String)` | Host as an IP address. The address is checked, and an IPv6 address gets its brackets. |
| `port(int)`, `port(String)` | Port, from 1 to 65535. |
| `database(String)` | Database name. Must not be blank. |
| `user(String)`, `password(String)` | Login. |
| `login(String user, String password)` | Both at once. |
| `withConfig(DatabaseConfig)` | Host, port, user, password and database from one object. |
| `addParameter(String key, value)` | Any other driver parameter. Each key can be set once. |
| `driverClass(...)` | The driver class, see [Relocation](#relocation). |

The database modules add the options that matter for their driver:

- PostgreSQL: `currentSchema`, `applicationName`, `connectTimeout`, `ssl`, `sslMode`, `sslFactory`, `sslcert`,
  `sslkey`, `sslrootcert`, `sslhostnameverifier`, `sslpasswordcallback`, `sslpassword`, `allowMultiQueries`,
  `dumpQueriesOnException`.
- MariaDB: `connectTimeout`, `useServerPrepStmts`, `allowLocalInfile`, `sslMode`, `serverSslCert`, `keyStore`,
  `keyStorePassword`, `keyStoreType`, `enabledSslCipherSuites`, `maxQuerySizeToLog`, `allowMultiQueries`,
  `dumpQueriesOnException`.
- MySQL: `connectTimeout`.

The [Javadoc](pathname:///sadu/javadoc/) describes each of them.

How the parameters reach the driver depends on the database:

- PostgreSQL and MySQL: in the JDBC URL, with URL encoded values. The drivers decode them, so any value works.
- MariaDB and SQLite: as driver properties of the pool (`dataSourceProperties` in HikariCP), unencoded, because these
  drivers take URL values literally. Their JDBC URL holds no parameters. MariaDB passes user and password to the pool
  as `username` and `password`.

### Config objects

If your application reads its connection settings from a file, pass them as one object. `SimpleDatabaseConfig`
holds host, port, user, password and database. `SchemaDatabaseConfig` adds a schema, which PostgreSQL sets as the
current schema.

```java
DatabaseConfig settings = new SimpleDatabaseConfig("localhost", "5432", "app", "secret", "app");

HikariDataSource dataSource = DataSourceCreator.create(PostgreSql.get())
        .configure(config -> config.withConfig(settings))
        .create()
        .build();
```

You can also implement `DatabaseConfig` (and `SchemaProvider` for the schema) in your own configuration class.

## SQLite

SQLite has no host and no login. Set the file instead:

```java
HikariDataSource dataSource = DataSourceCreator.create(SqLite.get())
        .configure(config -> config.path(Path.of("data.db")))
        .create()
        .build();
```

Without a path, SQLite runs in memory (`memory()`). Every pooled connection then gets its own empty database. Use a
file for anything you want to keep.

## Pool settings

After `create()` you can change the HikariCP settings:

| Method | HikariCP setting |
|---|---|
| `withMaximumPoolSize(int)` | `maximumPoolSize` |
| `withMinimumIdle(int)` | `minimumIdle` |
| `withConnectionTimeout(long)` | `connectionTimeout` in milliseconds |
| `withIdleTimeout(long)` | `idleTimeout` in milliseconds |
| `withMaxLifetime(long)` | `maxLifetime` in milliseconds |
| `withKeepaliveTime(long)` | `keepaliveTime` in milliseconds |
| `withAutoCommit(boolean)` | `autoCommit` |
| `withPoolName(String)` | `poolName` |
| `forSchema(String)` | `schema` |
| `usingUsername(String)`, `usingPassword(String)` | `username`, `password` |
| `withDataSourceClassName(Class)` | `dataSourceClassName` |
| `withScheduledExecutor(...)`, `withThreadFactory(...)` | `scheduledExecutor`, `threadFactory` |

For everything else, change the `HikariConfig` directly:

```java
.create()
.editHikariConfig(hikari -> hikari.setLeakDetectionThreshold(10_000))
.build();
```

`withHikariConfig(HikariConfig)` replaces the whole config, including the JDBC URL and driver properties that SADU
created. Use `editHikariConfig` unless you want that.

When the pool is created, SADU logs the JDBC URL with the password replaced by `******`. For MariaDB and SQLite the
logged URL shows no parameters, since they are passed as driver properties.

## Relocation

`create()` loads the driver class of your database. If you shade and relocate the driver into your own package, SADU
can not find it under its usual name and throws a `DriverClassNotFoundException`. Tell SADU the new class:

```java
DataSourceCreator.create(PostgreSql.get())
        .configure(config -> config
                .host("localhost")
                .driverClass(org.postgresql.Driver.class))
        .create()
        .build();
```

`driverClass(String)` takes the fully qualified class name instead.

These are the default driver classes:

| Database | Driver class |
|---|---|
| PostgreSQL | `org.postgresql.Driver` |
| MariaDB | `org.mariadb.jdbc.Driver` |
| MySQL | `com.mysql.cj.jdbc.Driver` |
| SQLite | `org.sqlite.JDBC` |

:::note
SADU 2.3.9 and older use the old MySQL driver class `com.mysql.jdbc.Driver`, which makes the MySQL driver log a
deprecation warning. Set `driverClass(com.mysql.cj.jdbc.Driver.class)` there to avoid it.
:::
