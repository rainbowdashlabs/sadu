---
title: Examples
sidebar_position: 6
---

# Examples

Common reads and writes with the query builder. All examples use the [default configuration](./configuration.md#the-default-configuration)
through `Query.query(...)`. With your own configuration, write `config.query(...)` instead.

The examples read users into this record:

```java
public record User(int id, UUID uuid, String name) {
    public static RowMapping<User> map() {
        return row -> new User(
                row.getInt("id"),
                row.get("uuid", StandardReader.UUID_FROM_STRING),
                row.getString("name"));
    }
}
```

## Read all matching rows

```java
List<User> users = Query.query("SELECT id, uuid, name FROM users WHERE name LIKE :name")
        .single(Call.of().bind("name", "li%"))
        .map(User.map())
        .all();
```

## Read the first row

```java
Optional<User> user = Query.query("SELECT id, uuid, name FROM users WHERE id = :id")
        .single(Call.of().bind("id", 1))
        .map(User.map())
        .first();
```

## Read a single value

```java
int count = Query.query("SELECT count(*) AS count FROM users")
        .single()
        .map(row -> row.getInt("count"))
        .first()
        .orElse(0);
```

## Insert a row

```java
InsertionResult result = Query.query("INSERT INTO users(uuid, name) VALUES (?, ?)")
        .single(Call.of().bind(UUID.randomUUID(), UUIDAdapter.AS_STRING).bind("lilly"))
        .insert();

if (result.changed()) {
    log.info("Inserted {} row", result.rows());
}
```

## Insert a row and get its id

```java
Optional<Long> id = Query.query("INSERT INTO users(uuid, name) VALUES (?, ?)")
        .single(Call.of().bind(UUID.randomUUID(), UUIDAdapter.AS_STRING).bind("lilly"))
        .insertAndGetKeys()
        .keys()
        .stream()
        .findFirst();
```

## Insert many rows

```java
List<String> names = List.of("lilly", "chojo", "yuki");

InsertionBatchResult<InsertionResult> result = Query.query("INSERT INTO users(uuid, name) VALUES (?, ?)")
        .batch(names.stream().map(name -> Call.of().bind(UUID.randomUUID(), UUIDAdapter.AS_STRING).bind(name)))
        .insert();

log.info("Inserted {} rows", result.rows());
```

## Update rows

```java
boolean renamed = Query.query("UPDATE users SET name = :name WHERE id = :id")
        .single(Call.of().bind("name", "lilly").bind("id", 1))
        .update()
        .changed();
```

## Delete rows

```java
int deleted = Query.query("DELETE FROM users WHERE name = ?")
        .single(Call.of().bind("lilly"))
        .delete()
        .rows();
```

## Map rows with a registered mapper

With a [row mapper](../mapping/index.md) for `User`, you do not need `map(...)`:

```java
List<User> users = Query.query("SELECT id, uuid, name FROM users")
        .single()
        .mapAs(User.class)
        .all();
```

## Insert a user and a related row in one transaction

See [Transactions](./transactions.md#chain-queries).
