---
title: Patches
sidebar_position: 4
---

# Patches

To change an existing database, add a patch. A patch is a file `patch_<number>.sql` in the directory of the current
major version. Number them from 1 without gaps, and raise the patch number in `database/version`.

```text
src/main/resources
└── database
    ├── postgresql
    │   └── 1
    │       ├── setup.sql     # 1.0
    │       ├── patch_1.sql   # 1.1
    │       ├── patch_2.sql   # 1.2
    │       └── patch_3.sql   # 1.3
    └── version               # 1.3
```

A patch holds only the change, for example:

```sql
ALTER TABLE users ADD COLUMN email TEXT;
```

Never change a patch that is already released. Databases that ran it will not run it again. Add a new patch instead.

## What the updater does

For a database on version `1.0` and your version `1.3`:

1. It reads the stored version `1.0`.
2. It runs `patch_1.sql` and stores `1.1` in the same transaction.
3. It does the same for `patch_2.sql` and `patch_3.sql`.
4. It stops when the stored version equals your version.

If a patch fails, the updater stops with an `UpdateException`. The database keeps the last version that worked, and
the next start continues from there.

If a patch file is missing, for example `patch_2.sql` while `version` says `1.3`, the updater throws an
`UpdateException` that names the file before it runs any patch. [Test your patch files](./testing.mdx) to catch this
early.

## setup.sql and patches

A new database runs `setup.sql` and then every patch. So you do not need to update `setup.sql` for each patch. Once
there are many patches, start a [new major version](./migration.md) with a fresh `setup.sql`.
