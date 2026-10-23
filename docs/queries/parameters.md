---
title: Parameters
sidebar_position: 3
---

# Parameters

Parameters keep user input out of your SQL. You mark them in the query and bind the values on a `Call`.

## Indexed parameters

Write `?` for each parameter and bind the values in the same order:

```java
Query.query("SELECT * FROM users WHERE id = ? AND name = ?")
        .single(Call.of().bind(1).bind("lilly"))
        .map(User.map())
        .first();
```

## Named parameters

Write `:name` instead of `?` and bind the value with its name. The name is given without the colon:

```java
Query.query("SELECT * FROM users WHERE id = :id AND name = :name")
        .single(Call.of().bind("id", 1).bind("name", "lilly"))
        .map(User.map())
        .first();
```

A named parameter can appear more than once. Every place gets the same value:

```sql
SELECT * FROM users WHERE name = :name OR nickname = :name
```

Rules for names:

- A name contains only letters and underscores: `:user_name` works, `:name2` does not.
- The colon must come right after a space, a tab, a comma, `=` or `(`.

So `id = :id`, `(:a, :b)` and `name=:name` work. A cast like `:id::uuid` also works.

:::warning
A named parameter at the start of a line, directly after a line break, is not found. SADU then throws an
`IllegalQueryParameterException` when you bind it. Put a space or a tab before it.
:::

## Mixing both

You can mix indexed and named parameters in one query. Indexed values are counted on their own, so only their order
matters:

```java
Query.query("SELECT * FROM users WHERE id = :id AND name = ?")
        .single(Call.of().bind("lilly").bind("id", 1));
```

## Checks

Before a statement runs, SADU checks the call against the query and throws an `IllegalQueryParameterException` if:

- fewer or more indexed values are bound than there are `?` in the query,
- a named parameter is bound that is not in the query,
- a named parameter in the query has no value.

SADU finds `?` and `:name` everywhere in the SQL, also inside string literals and comments.

## Types

`bind` has an overload for the usual Java types, each with and without a name:

| Java type | Written with |
|---|---|
| `String` | `setString` |
| `Short`, `Integer`, `Long`, `Float`, `Double`, `BigDecimal`, `Boolean`, `Byte` | the matching setter |
| `byte[]` | `setBytes` |
| `Enum` | `setString` with the name of the constant |
| `java.sql.Date`, `Time`, `Timestamp` | the matching setter |
| `LocalDate`, `LocalTime`, `LocalDateTime` | converted to `Date`, `Time`, `Timestamp` |
| `ZonedDateTime`, `OffsetDateTime` | converted to a `Timestamp` of the same instant |
| `OffsetTime` | converted to a `Time` with the local time part |
| `Ref`, `Blob`, `Clob`, `URL`, `RowId` | the matching setter |

`null` is fine for every type. SADU then calls `setNull` with a matching SQL type.

## Arrays and lists

To bind an SQL array, pass the values and the column type of your database:

```java
Query.query("INSERT INTO scores(user_id, points) VALUES (?, ?)")
        .single(Call.of().bind(1).bind(List.of(10, 20, 30), PostgreSqlTypes.INTEGER))
        .insert();
```

This works with a `Collection` or an `Object[]`. The column types are listed on [Databases](../databases.mdx#column-types).
Your database and its driver must support arrays. PostgreSQL does.

## Adapters

For types without an overload, or when you want a different format, pass an adapter as the last argument. A good
example is `UUID`, which you can store as text or as bytes:

```java
Call.of().bind(uuid, UUIDAdapter.AS_STRING);
Call.of().bind("id", uuid, UUIDAdapter.AS_BYTES);
```

`StandardAdapter` has adapters for more cases:

| Adapter | Writes |
|---|---|
| `INSTANT_AS_TIMESTAMP` | an `Instant` as `Timestamp` |
| `INSTANT_AS_MILLIS`, `INSTANT_AS_SECONDS` | an `Instant` as epoch milliseconds or seconds (`long`) |
| `localDate(Calendar)`, `localTime(Calendar)`, `localDateTime(Calendar)`, `offsetTime(Calendar)`, `offsetDateTime(Calendar)`, `zonedDateTime(Calendar)` | the time types with a calendar, for example to store in UTC |

The [value converters](./results.md#value-converters) work as adapters too. They write and read the same format, so
both sides stay in sync.

### Your own adapter

`Adapter.create` builds an adapter from a setter and the SQL type to use for `null`:

```java
Adapter<Color> colorAdapter = Adapter.create(
        (stmt, index, color) -> stmt.setInt(index, color.getRGB()),
        Types.INTEGER);

Call.of().bind(Color.RED, colorAdapter);
```

If an existing setter fits, pass a mapping function instead:

```java
Adapter<Color> colorAdapter = Adapter.create(PreparedStatement::setInt, Types.INTEGER, Color::getRGB);
```

## Formatting the query

`Query.query` takes extra arguments that are put into the SQL with `String.formatted(...)` before the parameters are
parsed. Use it for table or column names that you can not pass as a parameter:

```java
Query.query("SELECT * FROM %s WHERE id = ?", tableName)
        .single(Call.of().bind(1));
```

:::danger
Never put user input into the SQL this way. Formatted values are not escaped.
:::

When you pass format arguments, write a literal `%` in your SQL as `%%`.

SADU parses each distinct SQL text once and keeps the result for the lifetime of the application. Formatting the
query with changing values creates a new entry every time. Use parameters for values.
