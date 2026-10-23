---
title: Transactions
sidebar_position: 7
---

# Transactions

By default every execution is its own transaction. A single call or a whole batch is committed when it is done. This
page shows how to run several queries in one transaction.

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
   nothing is committed.

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

:::warning
`query(...)` after `storeOneAndAppend` or `storeAllAndAppend` always formats the SQL, even without format
arguments. A `%` in that SQL, like in `LIKE 'a%'`, then throws an `UnknownFormatConversionException`. Write it as
`%%`, or pass the pattern as a parameter.
:::

## Use your own connection

If you already have a connection, for example from another library, hand it to SADU with `withConnection`:

```java
Connection connection = dataSource.getConnection();
connection.setAutoCommit(false);

try (ConnectedQueryConfiguration tx = config.withConnection(connection)) {
    tx.query("INSERT INTO users(name) VALUES (?)")
            .single(Call.of().bind("lilly"))
            .insert();
}
```

At the end of the block SADU commits, if no query failed, and closes the connection. `close()` of
`ConnectedQueryConfiguration` is declared to throw `Exception`, so the surrounding code has to handle it.

:::warning
SADU does not turn off auto commit on a connection you pass in. Turn it off yourself as shown above. Otherwise every
statement is committed right away, and the commit at the end fails and reports an `SQLException` to your exception
handler.
:::

Calling `withConnection` on a configuration that already has a connection commits and closes the old one first.
