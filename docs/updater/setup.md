---
title: Setup
sidebar_position: 2
---

# Setup

The updater reads its files from your resources. Create this structure:

```text
src/main/resources
└── database
    ├── postgresql
    │   └── 1
    │       └── setup.sql
    └── version
```

## The version file

`database/version` holds the version of your schema: the major and the patch number, separated by a dot.

```text
1.0
```

Patch `0` means only `setup.sql`. Every patch you add raises the patch number.

If `database/version` is missing, the updater fails with a `FileNotFoundException`. Set the version with
`setVersion(...)` on the builder if you do not want a version file.

## One directory per database

Each database has its own directory. Its name is the name of the database:

| Database | Directory |
|---|---|
| PostgreSQL | `database/postgresql` |
| MariaDB | `database/mariadb` |
| MySQL | `database/mysql` |
| SQLite | `database/sqlite` |

If your application supports several databases, every directory needs the same files. A file may be empty when a
database needs no change.

Inside, each major version has its own directory, starting with `1`.

## The setup script

`setup.sql` creates your schema in the state of version `<major>.0`:

```sql
CREATE TABLE IF NOT EXISTS users
(
    id   SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS birthdays
(
    user_id  INTEGER NOT NULL REFERENCES users (id),
    birthday DATE    NOT NULL
);
```

End every statement with a semicolon.

How a file is sent to the database depends on the database:

- PostgreSQL gets the whole file at once.
- MariaDB, MySQL and SQLite get one statement at a time. SADU splits the file at each `;`.

To keep a `;` inside a statement, for example in a trigger, change the delimiter with a `DELIMITER` line:

```sql
DELIMITER $$
CREATE TRIGGER trim_name BEFORE INSERT ON users
FOR EACH ROW
BEGIN
    SET NEW.name = TRIM(NEW.name);
END$$
DELIMITER ;
```

The splitter is simple. It also splits at a `;` inside a string or a comment, so avoid those there.

Next: [Run the updater](./execution.md).
