---
title: Results
sidebar_position: 5
---

# Results

The last step of a query runs it and gives you the result. Reads return your mapped objects, writes tell you how many
rows changed.

## Read rows

Map each row to an object with a `RowMapping`, a function from `Row` to your object:

```java
Optional<User> user = Query.query("SELECT id, name FROM users WHERE id = ?")
        .single(Call.of().bind(1))
        .map(row -> new User(row.getInt("id"), row.getString("name")))
        .first();
```

Instead of `map(...)` you can use `mapAs(User.class)`. SADU then picks a registered mapper for that class. See
[Row mapping](../mapping/index.md).

Then pick what you need:

| Method | Returns |
|---|---|
| `first()` | the first row as an `Optional`, empty when there is no row |
| `all()` | all rows as a `List`, empty when there is no row |
| `firstResult()` | a `Result` with the first row |
| `allResults()` | a `Result` with all rows as a `List` |

`Result.result()` gives you the value, `get()` the value as an `Optional`.

A mapping must return your own object. Returning the `Row` or the `ResultSet` itself throws an
`IllegalQueryReturnTypeException`, because both are closed once the query is done.

## Read values from a row

`Row` wraps the JDBC `ResultSet` of the current row. It has the usual getters by column name or index, like
`getString`, `getInt`, `getLong`, `getBoolean`, `getTimestamp` and `getObject`, plus a few more:

| Method | Reads |
|---|---|
| `getEnum(column, Class)` | an enum constant from its name |
| `getLocalDate`, `getLocalTime`, `getLocalDateTime` | the `java.time` types, optionally with a `Calendar` |
| `getList(column)` | an SQL array as a `List`, empty when the column is `NULL`; `NULL` elements become `null` |
| `get(column, ValueReader)` | any value with a reader, see below |

Getters for primitives return `0` or `false` for `NULL`, just like JDBC.

### Value readers

A `ValueReader` reads a column and converts it. `StandardReader` has readers for common cases:

```java
UUID id = row.get("id", StandardReader.UUID_FROM_STRING);
Instant created = row.get("created", StandardReader.INSTANT_FROM_TIMESTAMP);
Mode mode = row.get("mode", StandardReader.forEnum(Mode.class));
```

| Reader | Reads |
|---|---|
| `UUID_FROM_STRING`, `UUID_FROM_BYTES` | a `UUID` from text or 16 bytes |
| `INSTANT_FROM_TIMESTAMP`, `INSTANT_FROM_MILLIS`, `INSTANT_FROM_SECONDS` | an `Instant` |
| `LOCAL_DATE`, `LOCAL_TIME`, `LOCAL_DATE_TIME` | the local time types |
| `OFFSET_DATE_TIME`, `ZONED_DATE_TIME`, `OFFSET_TIME` | the zoned time types in the system time zone |
| `forEnum(Class)` | an enum constant from its name |
| `localDate(Calendar)` and the other methods with a `Calendar` | the time types with a calendar |

A reader returns `null` when the column is `NULL`, also the readers of epoch numbers (`INSTANT_FROM_MILLIS`,
`INSTANT_FROM_SECONDS`).

### Value converters

A `ValueConverter` is an [adapter](./parameters.md#adapters) and a reader in one, so the same format is used for
writing and reading. `StandardValueConverter` has them for `UUID` (`UUID_STRING`, `UUID_BYTES`), `Instant`
(`INSTANT_TIMESTAMP`, `INSTANT_MILLIS`, `INSTANT_SECONDS`), the `java.time` types and enums (`forEnum(Class)`):

```java
Query.query("INSERT INTO users(id, name) VALUES (?, ?)")
        .single(Call.of().bind(id, StandardValueConverter.UUID_BYTES).bind("lilly"))
        .insert();

Optional<UUID> stored = Query.query("SELECT id FROM users WHERE name = ?")
        .single(Call.of().bind("lilly"))
        .map(row -> row.get("id", StandardValueConverter.UUID_BYTES))
        .first();
```

## Write rows

| Method | Returns |
|---|---|
| `insert()` | an `InsertionResult` |
| `insertAndGetKeys()` | an `InsertionResult` with the generated keys |
| `update()` | a `ManipulationResult` |
| `delete()` | a `ManipulationResult`. It does the same as `update()`. |

A `ManipulationResult` tells you what happened:

| Method | What it does |
|---|---|
| `rows()` | number of changed rows |
| `changed()` | `true` when at least one row changed |
| `isSuccess()` | `true` when at least one row changed and there were no exceptions |
| `ifChanged(rows -> ...)` | runs the consumer with the row count when something changed |
| `ifEmpty(result -> ...)` | runs the consumer when nothing changed |
| `ifChangedOrElse(changed, empty)` | one of the two |

The `if...` methods return `changed()`.

```java
Query.query("UPDATE users SET name = ? WHERE id = ?")
        .single(Call.of().bind("lilly").bind(1))
        .update()
        .ifChangedOrElse(
                rows -> log.info("Renamed user"),
                result -> log.info("No user with that id"));
```

`InsertionResult.keys()` returns the generated keys as a list of `Long`. SADU reads the first column of the keys
the driver returns. It is empty for `insert()`.

### Batch results

A batch returns a `ManipulationBatchResult` or an `InsertionBatchResult`. `rows()` is the sum of all calls,
`changed()` is `true` if any call changed something, and `keys()` holds the keys of all calls. `results()` gives you
the result of each call.

## Exceptions in results

Every result has `exceptions()` and `hasExceptions()`. They hold the `SQLException`s of the query that produced the
result, after they went to the exception handler of your [configuration](./configuration.md). In a
[chain of queries](./transactions.md#chain-queries), they hold the exceptions of every query of the chain so far.

```java
ManipulationResult result = Query.query("UPDATE users SET name = ? WHERE id = ?")
        .single(Call.of().bind("lilly").bind(1))
        .update();

if (result.hasExceptions()) {
    log.warn("Rename failed", result.exceptions().get(0));
}
```

With `setThrowExceptions(true)`, a failed query throws instead, so you never get a result with exceptions.
