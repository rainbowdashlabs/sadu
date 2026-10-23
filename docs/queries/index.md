---
title: Queries
sidebar_position: 1
---

# Queries

`sadu-queries` is a query builder for plain SQL. You write the SQL, SADU does the rest: it opens and closes the
connection and the statement, sets your parameters, reads the result and handles exceptions.

Add `de.chojo.sadu:sadu-queries:2.4.0` to your dependencies. It brings `sadu-mapper` with it.

## Why use it?

This is a simple read in plain JDBC:

```java
public Optional<User> findUser(int id) {
    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM users WHERE id = ?")) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return Optional.of(new User(rs.getInt("id"), rs.getString("name")));
        }
    } catch (SQLException e) {
        log.error("Could not read user", e);
    }
    return Optional.empty();
}
```

The same read with SADU:

```java
public Optional<User> findUser(int id) {
    return Query.query("SELECT id, name FROM users WHERE id = ?")
            .single(Call.of().bind(id))
            .map(row -> new User(row.getInt("id"), row.getString("name")))
            .first();
}
```

## How a query is built

Every query goes through the same steps. Each step only offers the methods that make sense at that point.

1. **Query.** `Query.query("SQL")` or `configuration.query("SQL")` takes your SQL.
2. **Call.** `single(...)` runs the query once, `batch(...)` runs it once for each set of parameters. The
   [call](./calls.md) holds the [parameters](./parameters.md).
3. **Execute.** For reads you map the rows with `map(...)` or `mapAs(...)` and get them with `first()` or `all()`.
   For writes you call `insert()`, `insertAndGetKeys()`, `update()` or `delete()`. See [Results](./results.md).

Nothing is sent to the database before the last step.

## What happens on execution

Each execution takes a connection from your data source, turns off auto commit, runs the statement and commits. A
batch runs all its calls on one connection and commits once at the end. Then the connection goes back to the pool.

If something goes wrong, the `SQLException` goes to the exception handler of your
[configuration](./configuration.md). By default SADU then returns an empty result: an empty `Optional`, an empty
list or zero changed rows. You can make SADU throw instead.

To run several queries in one transaction, see [Transactions](./transactions.md).

## Next steps

- [Configuration](./configuration.md): data source, exception handling and the default configuration.
- [Parameters](./parameters.md): indexed and named parameters, types and adapters.
- [Calls](./calls.md): single and batch calls.
- [Results](./results.md): read rows and check what a write changed.
- [Examples](./examples.md): common reads and writes.
- [Transactions](./transactions.md): several queries in one transaction.
