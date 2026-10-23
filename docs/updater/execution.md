---
title: Run the updater
sidebar_position: 3
---

# Run the updater

Run the updater once at startup, before your application uses the database:

```java
SqlUpdater.builder(dataSource, PostgreSql.get())
        .withClassLoader(getClass().getClassLoader())
        .execute();
```

`execute()` throws an `IOException` when a file can not be read and an `SQLException` when the database fails. A
failed patch throws an `UpdateException`, which is unchecked.

## What happens on the first start

1. PostgreSQL only: the schemas from `setSchemas(...)` are created if they are missing.
2. The version table is created if it does not exist.
3. The version table is empty, so the updater runs `setup.sql` of your current major version and stores `<major>.0`.
4. It runs every patch of that major version and stores the version after each one.

On later starts it reads the stored version and runs only the [patches](./patching.md) that are missing. If the
stored version is newer than yours, it throws an `UpdateException`.

## Options

| Method | Default | What it does |
|---|---|---|
| `withClassLoader(ClassLoader)` | the class loader of SADU | Where to look for the `database` resources. |
| `setVersion(SqlVersion)` | read from `database/version` | The version your application needs, for example `new SqlVersion(1, 3)`. |
| `setVersionTable(String)` | `version` | Name of the version table. |
| `setReplacements(QueryReplacement...)` | none | Text replacements for your scripts, see below. |
| `preUpdateHook(SqlVersion, ...)` | none | Java code that runs before the patch of that version. |
| `postUpdateHook(SqlVersion, ...)` | none | Java code that runs after the patch of that version. |
| `setSchemas(String...)` | none | PostgreSQL only: schemas to create before the setup. |

Set the class loader when SADU and your resources are loaded by different class loaders, for example in a plugin
system or when SADU is shaded into a library. Without it the updater might not find your files.

Do not change the version table name later. The updater would no longer find the stored version and would run the
setup again.

## Replacements

Replacements change the text of every script before it runs. A common use is a schema name that differs between
development and production:

```java
SqlUpdater.builder(dataSource, PostgreSql.get())
        .setReplacements(new QueryReplacement("dev_schema", "live_schema"))
        .setSchemas("live_schema")
        .execute();
```

The first argument is a regular expression. Escape characters like `.` if you mean them literally:
`new QueryReplacement("dev_schema\\.", "live_schema.")`.

Replacements apply to your scripts only, not to the queries on the version table.

## Update hooks

Some changes are easier in Java than in SQL, like moving data into a new format. Hooks run in the same transaction
as the patch of their version:

```java
SqlUpdater.builder(dataSource, PostgreSql.get())
        .postUpdateHook(new SqlVersion(1, 2), connection -> {
            try (var stmt = connection.prepareStatement("UPDATE users SET name = lower(name)")) {
                stmt.execute();
            }
        })
        .execute();
```

The connection you get can not be committed, rolled back or closed. Do not put it into a try-with-resources block.
The updater commits it after the patch.

A hook only runs when the patch of its version runs. The version of a [major version migration](./migration.md) is
`<new major>.0`, so a hook for `new SqlVersion(2, 0)` runs with the migration from 1 to 2. Hooks never run for
`setup.sql`. On a new database, the patches after the setup run with their hooks.
