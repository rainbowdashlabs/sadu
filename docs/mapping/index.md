---
title: Row mapping
sidebar_position: 1
---

# Row mapping

`sadu-mapper` turns rows into your own classes. You register a mapper for a class once, and every query can then use
`mapAs(YourClass.class)` instead of writing the mapping again.

`sadu-queries` and the database modules bring `sadu-mapper` with them. To add it on its own, use
`de.chojo.sadu:sadu-mapper:2.4.0`.

```java
Optional<User> user = Query.query("SELECT id, name FROM users WHERE id = ?")
        .single(Call.of().bind(1))
        .mapAs(User.class)
        .first();
```

For this to work, three things are needed:

1. A `RowMapperRegistry` in your [query configuration](../queries/configuration.md). See
   [Configuration](./configuration.md).
2. A mapper for `User` in that registry, or a `@MappingProvider` on `User`. See [Custom mappers](./custom-mappers.mdx).
3. The columns of the result must fit the mapper.

## How SADU picks a mapper

A mapper names the columns it needs. A class can have several mappers for different column sets. For each query, SADU
looks at the columns of the result and picks the mapper with the most matching columns.

A mapper without columns is a wildcard. The [default mappers](./configuration.md#default-mappers) for simple types
like `String` or `Long` are wildcards.

If no mapper is registered for the class, SADU looks for a `@MappingProvider` on the class and registers it. If there
is none, `mapAs` throws a `MappingException`.

:::warning
SADU picks the best of the mappers with columns, even if none of them fits the result. A wildcard mapper of the same
class is then never used, and the query fails with an `SQLException` about a missing column. Register either
mappers with columns or a wildcard for a class, not both.
:::

## Mapper options

`mapAs` takes a `MapperConfig` as a second argument:

```java
List<DateCount> perMonth = Query.query("SELECT month, count FROM month_stats")
        .single()
        .mapAs(DateCount.class, new MapperConfig().addAlias("time", "month"))
        .all();
```

| Option | What it does |
|---|---|
| `addAlias(original, alias)` | When the mapper reads the column `original`, it gets the column `alias` instead. This way one mapper works for results with different column names. |
| `strict()` | A mapper only fits when its columns match the columns of the result exactly. |

Aliases work for the getters of the `Row` that take a column name, also in your own mapping code. `getList` is the
exception: it ignores aliases.
