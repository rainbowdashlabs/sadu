---
title: Transactions
sidebar_position: 7
---

# Transactions

By default every execution is its own transaction. A single call or a whole batch is committed when it is done, and a
batch with a failed call is rolled back. This page shows how to run several queries in one transaction.

## Single transaction

`withSingleTransaction()` creates a configuration that keeps one connection for all its queries. Use it in a
try-with-resources block:

```java
try (var tx = config.withSingleTransaction()) {
    tx.query("UPDATE accounts SET balance = balance - ? WHERE id = ?")
            .single(Call.of().bind(100).bind(1))
            .update();
    tx.query("UPDATE accounts SET balance = balance + ? WHERE id = ?")
            .single(Call.of().bind(100).bind(2))
            .update();
}
```

What happens:

1. The first query takes a connection from the pool and turns off auto commit.
2. Every following query of `tx` uses the same connection.
3. At the end of the block SADU commits and closes the connection, but only if no query failed. If one failed,
   SADU rolls back and nothing is committed.

This needs an atomic configuration, which is the default. With `setAtomic(false)` the connection keeps auto commit on,
so each statement is committed right away and a failed query does not undo the others.

With the default configuration, start with `QueryConfiguration.getDefault().withSingleTransaction()`.

## Chain queries

A read can store its result and start the next query. The next call can then use the stored value. In a single
transaction both run in the same transaction:

```java
try (var tx = config.withSingleTransaction()) {
    tx.query("INSERT INTO users(uuid, name) VALUES (?, ?) RETURNING id, uuid, name")
            .single(Call.of().bind(UUID.randomUUID(), UUIDAdapter.AS_STRING).bind("lilly"))
            .map(User.map())
            .storeOneAndAppend("user")
            .query("INSERT INTO birthdays(user_id, birthday) VALUES (:id, :date)")
            .single(storage -> Call.of()
                    .bind("id", storage.getAs("user", User.class).orElseThrow().id())
                    .bind("date", LocalDate.of(1990, 1, 1))
                    .asSingleCall())
            .insert();
}
```

`storeOneAndAppend(key)` stores the first row, `storeAllAndAppend(key)` stores all rows as a list. How to read
the storage is explained in [Calls](./calls.md#calls-from-stored-results). The example uses `RETURNING`, which
PostgreSQL supports.

Outside a single transaction you can chain queries too, but then each query is committed on its own.

`query(...)` after `storeOneAndAppend` or `storeAllAndAppend` takes [format arguments](./parameters.md#formatting-the-query) like
`Query.query(...)`. The SQL is only formatted when you pass some, so a `%` like in `LIKE 'a%'` needs no escaping
otherwise.

## Use your own connection

If you already have a connection, for example from another library, hand it to SADU with `withConnection`:

```java
Connection connection = dataSource.getConnection();

try (ConnectedQueryConfiguration tx = config.withConnection(connection)) {
    tx.query("INSERT INTO users(name) VALUES (?)")
            .single(Call.of().bind("lilly"))
            .insert();
}
```

SADU turns off auto commit on the connection before the first query, so all queries run in one transaction. At the
end of the block SADU commits, or rolls back if a query failed, and closes the connection. With `setAtomic(false)`
SADU turns auto commit on instead and only closes the connection. `close()` of
`ConnectedQueryConfiguration` is declared to throw `Exception`, so the surrounding code has to handle it.

Calling `withConnection` on a configuration that already has a connection commits and closes the old one first.
