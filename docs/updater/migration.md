---
title: Major versions
sidebar_position: 5
---

# Major versions

After many patches, a new database has to run all of them one by one. A new major version gives you a fresh start:
a new `setup.sql` with the whole current schema, and a migration script for databases on the old major version.

```text
src/main/resources
└── database
    ├── postgresql
    │   ├── 1
    │   │   ├── setup.sql
    │   │   ├── patch_1.sql
    │   │   ├── patch_2.sql
    │   │   ├── patch_3.sql
    │   │   ├── patch_4.sql     # 1.4
    │   │   └── migrate.sql     # 1.4 to 2.0
    │   └── 2
    │       ├── setup.sql       # 2.0
    │       └── patch_1.sql     # 2.1
    └── version                 # 2.1
```

To start a new major version:

1. Add `migrate.sql` to the directory of the old major version. It changes a database on the last patch of that
   version into the state of `2.0`.
2. Add the directory `2` with a `setup.sql` that creates the same state from scratch.
3. Set `database/version` to `2.0`, and raise the patch number with each new patch in `2`.

`migrate.sql` and the new `setup.sql` must lead to the same schema. Otherwise old and new installations drift apart.

## What the updater does

For a database on version `1.3` and your version `2.1`:

1. It runs `1/patch_4.sql` and stores `1.4`.
2. There is no `1/patch_5.sql`, so it runs `1/migrate.sql` and stores `2.0`.
3. It runs `2/patch_1.sql` and stores `2.1`.

A new database skips directory `1` entirely. It runs `2/setup.sql` and then the patches in `2`.

You can keep several old major versions. Each of them needs its own `migrate.sql` to the next one.

:::note
Migrations between major versions work like this from SADU 2.3.8 on. Older versions looked for the migration script
in the wrong place. Use 2.3.8 or newer if you have more than one major version.
:::
