---
title: Configuration
sidebar_position: 2
---

# Configuration

Every query needs a `QueryConfiguration`. It holds the data source and tells SADU how to handle exceptions and how
to map rows.

Use `de.chojo.sadu.queries.api.configuration.QueryConfiguration`. The interface with the same name in
`de.chojo.sadu.queries.configuration` is deprecated and will be removed.

## Build a configuration

```java
QueryConfiguration config = QueryConfiguration.builder(dataSource)
        .setExceptionHandler(e -> log.error("Database request failed", e))
        .setThrowExceptions(false)
        .setRowMapperRegistry(new RowMapperRegistry().register(PostgresqlMapper.getDefaultMapper()))
        .build();
```

| Setting | Default | What it does |
|---|---|---|
| `setExceptionHandler(Consumer<SQLException>)` | prints the exception to `System.err` | Gets every `SQLException` of a query. |
| `setThrowExceptions(boolean)` | `false` | When `true`, a failed query throws a `WrappedQueryExecutionException` (unchecked) after the handler ran. In an atomic configuration nothing of that execution is committed. |
| `setRowMapperRegistry(RowMapperRegistry)` | an empty registry | The mappers used by `mapAs(...)`. See [Row mapping](../mapping/index.md). |
| `setAtomic(boolean)` | `true` | When `true`, every execution and every [single transaction](./transactions.md) runs in a transaction that is committed at the end and rolled back on an error. When `false`, the connection keeps auto commit on and each statement is committed on its own. |

Set an exception handler that logs to your logger. The default one prints to standard error.

:::warning
In a batch, a call that fails is skipped and the other calls are still committed, unless you set
`setThrowExceptions(true)`.
:::

Errors in your own code are not `SQLException`s and are always thrown, for example a parameter that is missing or
bound twice (`IllegalQueryParameterException`).

## Run queries with a configuration

Start a query from the configuration:

```java
List<String> names = config.query("SELECT name FROM users")
        .single()
        .map(row -> row.getString("name"))
        .all();
```

## The default configuration

Passing the configuration around is not always handy. Set it once as the default:

```java
QueryConfiguration.setDefault(config);
```

`Query.query(...)` then uses it:

```java
List<String> names = Query.query("SELECT name FROM users")
        .single()
        .map(row -> row.getString("name"))
        .all();
```

`Query.query(...)` without a default throws a `NullPointerException` that tells you to call `setDefault` first.
`QueryConfiguration.getDefault()` returns the current default.

## Change a configuration

Configurations can not be changed. `edit()` gives you a builder with the same settings, so you can create a variant:

```java
QueryConfiguration throwing = config.edit()
        .setThrowExceptions(true)
        .build();
```

`edit(DataSource)` does the same for another data source.

## Single transactions

`withSingleTransaction()` and `withConnection(Connection)` create configurations that run several queries in one
transaction. See [Transactions](./transactions.md).
