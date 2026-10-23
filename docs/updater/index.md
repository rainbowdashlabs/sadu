---
title: Updater
sidebar_position: 1
---

# Updater

The updater keeps your database schema in sync with your application. You ship SQL files in your resources, and the
updater runs the ones the database still needs.

The database modules bring `sadu-updater` with them. To add it on its own, use `de.chojo.sadu:sadu-updater:2.4.0`.

## How it works

Your schema has a version made of a major and a patch number, like `1.3`. The updater keeps the version of the
database in a version table.

- On an empty database it runs the `setup.sql` of the current major version and then every patch of that version.
- On an existing database it runs every patch between the stored version and your version.
- When you start a new major version, a migration script moves the database from the old major version to the new
  one.

Read on in this order:

1. [Setup](./setup.md): the files and the version.
2. [Run the updater](./execution.md): the builder and its options.
3. [Patches](./patching.md): change an existing database.
4. [Major versions](./migration.md): start fresh with a new major version.
5. [Test your patch files](./testing.mdx): check the files in a unit test.

## Transactions

Each patch runs in its own transaction together with its update hooks and the new version. If a statement fails, that
patch is rolled back, the updater stops and throws an `UpdateException`. Patches that already ran stay applied.
`setup.sql` also runs in one transaction together with its version.

MariaDB and MySQL commit every `CREATE`, `ALTER` and `DROP` right away. A script with such statements can not be rolled
back on these databases. On PostgreSQL, statements that can not run in a transaction, like
`CREATE INDEX CONCURRENTLY`, can not be used in `setup.sql` or a patch.

Write your scripts so that they can run again where possible, for example with `CREATE TABLE IF NOT EXISTS`.
