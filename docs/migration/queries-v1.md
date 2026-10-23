---
title: From the old query builder
sidebar_position: 1
---

# From the old query builder

SADU 1 had a different query builder with `QueryFactory`, `QueryBuilderConfig` and `builder()`. It was replaced by
[`sadu-queries`](../queries/index.md) in SADU 2 and no longer exists. This page helps if you still have code that
uses it.

## What changed

| Old | New |
|---|---|
| `QueryFactory`, `QueryBuilderConfig` | one [`QueryConfiguration`](../queries/configuration.md) |
| `extends QueryFactory` and `builder(Type.class)` | `Query.query(...)` or `configuration.query(...)`, without a type |
| `.parameter(stmt -> stmt.setInt(...).setString(...))` | `.single(Call.of().bind(...).bind(...))` |
| `queryWithoutParams(sql)`, `emptyParams()` | `.query(sql).single()` |
| `readRow(...)` | `map(...)` |
| `firstSync()`, `allSync()` | `first()`, `all()` |
| `sendSync()` | not needed. A write runs when you call `insert()`, `update()` or `delete()`. |
| `setUuidAsBytes(uuid)`, `getUuidFromBytes(column)` | `bind(uuid, StandardValueConverter.UUID_BYTES)`, `row.get(column, StandardValueConverter.UUID_BYTES)` |
| `getKey()` | `insertAndGetKeys()`, see [Results](../queries/results.md#write-rows) |
| `de.chojo.sadu.wrapper.util.Row` | `de.chojo.sadu.mapper.wrapper.Row` |
| `de.chojo.sadu.databases.PostgreSql` and the other databases | `de.chojo.sadu.postgresql.databases.PostgreSql` and so on, one module per database |

## Async calls

The old builder could run queries asynchronously. The new one runs everything on the calling thread. Wrap the call
in a `CompletableFuture` where you need it:

```java
CompletableFuture<Optional<User>> user = CompletableFuture.supplyAsync(() -> Query.query("SELECT id, name FROM users WHERE id = ?")
        .single(Call.of().bind(1))
        .map(User.map())
        .first());
```

## Appended queries

The old builder ran appended queries in one transaction. Now each query is its own transaction. To run several
queries in one transaction, use a [single transaction](../queries/transactions.md#single-transaction). If you
appended queries in a loop, a [batch call](../queries/calls.md#batch-calls) is the better fit.

## Search and replace

These regular expressions do most of the work in your IDE:

| Find | Replace with |
|---|---|
| `builder\(.*?\)\s*\.` | nothing |
| `\.(setInt\|setString\|setLong\|setBoolean\|setEnum\|setTimestamp)\(` | `.bind(` |
| `\.parameter\(\w+ -> \w+` | `.single(Call.of()` |
| `\.setUuidAsBytes\((.+?)\)` | `.bind($1, UUID_BYTES)` |
| `getUuidFromBytes\((.*?)\)` | `get($1, UUID_BYTES)` |
| `\s*\.sendSync\(\)` | nothing |
| `readRow` | `map` |
| `firstSync` | `first` |
| `allSync` | `all` |
| `queryWithoutParams\((.+?)\)` | `query($1).single()` |
| `emptyParams\(\)` | `single()` |

`UUID_BYTES` is `de.chojo.sadu.queries.converter.StandardValueConverter.UUID_BYTES`.
