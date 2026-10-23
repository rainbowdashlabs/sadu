---
title: Configuration
sidebar_position: 2
---

# Configuration

Mappers live in a `RowMapperRegistry`. Create one, register your mappers and pass it to your
[query configuration](../queries/configuration.md):

```java
RowMapperRegistry registry = new RowMapperRegistry()
        .register(PostgresqlMapper.getDefaultMapper())
        .register(userMapper);

QueryConfiguration config = QueryConfiguration.builder(dataSource)
        .setRowMapperRegistry(registry)
        .build();
```

`register` takes one mapper, several mappers or a list. A class can have only one mapper per column set. Registering
a second one with the same columns throws a `MappingAlreadyRegisteredException`.

`register(Class)` registers the `@MappingProvider` mappers of a class right away, see
[Custom mappers](./custom-mappers.mdx#annotations). It throws an `InvalidMappingException` if the class has none.

## Default mappers

Each database module has mappers for simple types. They read the first column whose type fits, so they are made for
results with one column:

```java
long users = config.query("SELECT count(*) FROM users")
        .single()
        .mapAs(Long.class)
        .first()
        .orElse(0L);
```

| Class | Maps to |
|---|---|
| `PostgresqlMapper` | `Boolean`, `Short`, `Integer`, `Long`, `Float`, `Double`, `BigDecimal`, `String`, `Byte[]`, `UUID` |
| `MariaDbMapper` | `Boolean`, `Short`, `Integer`, `Long`, `Float`, `Double`, `BigDecimal`, `String`, `Byte[]`, `UUID` |
| `MySqlMapper` | `Boolean`, `Integer`, `Long`, `Float`, `Double`, `String`, `Byte[]`, `UUID` |
| `SqLiteMapper` | `Boolean`, `Integer`, `Long`, `Float`, `Double`, `String`, `Byte[]`, `UUID` |

Register them with `getDefaultMapper()`, or pick single ones like `PostgresqlMapper.LONG_MAPPER`.

`UUID` is read from a text column, or from a binary column as 16 bytes.

:::warning
The default mappers find a column by the type name the driver reports. Some PostgreSQL columns are not matched: a
`serial` or `bigserial` column is not read as a number, and a column of the type `uuid` is not read as a `UUID`.
Cast the column in your query (`SELECT id::integer`) or map it yourself with `map(...)`.

On MariaDB and MySQL, the `Byte[]` mapper reads text columns, not binary ones. Read binary columns with
`row.getBytes(...)` in your own mapping.
:::
