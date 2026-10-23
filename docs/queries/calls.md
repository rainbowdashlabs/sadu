---
title: Calls
sidebar_position: 4
---

# Calls

A call says how often a query runs and with which [parameters](./parameters.md). A `Call` holds one set of
parameters. `single(...)` runs the query once, `batch(...)` runs it once per call.

## Single calls

```java
// No parameters
Query.query("SELECT * FROM users").single();

// With parameters
Query.query("SELECT * FROM users WHERE id = ?").single(Call.of().bind(1));
```

`single()` is the same as `single(Calls.empty())`. `Call.call()` is another name for `Call.of()`, which reads well
as a static import.

A single call can read or write. See [Results](./results.md).

## Batch calls

A batch runs the same statement with many sets of parameters on one connection, in one transaction:

```java
Query.query("INSERT INTO users(name) VALUES (?)")
        .batch(Call.of().bind("lilly"), Call.of().bind("chojo"))
        .insert();
```

You can pass the calls in several ways:

```java
.batch(Call.of().bind("a"), Call.of().bind("b"));      // varargs
.batch(List.of(Call.of().bind("a"), Call.of().bind("b")));  // a list
.batch(names.stream().map(name -> Call.of().bind(name)));   // a stream
```

A stream of calls can also be collected with `Calls.collect()` into a `BatchCall`.

A batch can only write: `insert()`, `insertAndGetKeys()`, `update()` and `delete()`. It can not read rows.

Each call of a batch is sent as its own statement. If one of them fails, the exception goes to your handler. On
MariaDB, MySQL and SQLite the other calls still run and are committed. PostgreSQL aborts the whole transaction after
the first error. Set `setThrowExceptions(true)` on the [configuration](./configuration.md) to
stop and commit nothing instead.

## Calls from stored results

When you chain queries (see [Transactions](./transactions.md#chain-queries)), the next call can use results that an
earlier query stored. Pass a function that gets the storage:

```java
.single(storage -> Call.of()
        .bind("user_id", storage.getAs("user", User.class).orElseThrow().id())
        .asSingleCall())
```

For a batch, return a `BatchCall`, for example with `asBatchCall()`.

The storage has three getters:

| Method | Returns |
|---|---|
| `get(key)` | the stored value, cast to what you assign it to |
| `getAs(key, Class)` | the stored value as an `Optional` |
| `getList(key, Class)` | a stored list, or an empty list |

Each of them throws an `IllegalArgumentException` if nothing was stored under that key.
