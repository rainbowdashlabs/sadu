/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

import de.chojo.sadu.core.databases.Database;
import de.chojo.sadu.core.updater.SqlVersion;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlUpdaterTest {
    DataSource dataSource;

    @Test
    @SuppressWarnings({"unchecked"})
    void shouldLoadCorrectFilesDuringMigration() throws Exception {
        ClassLoader loader = mock(ClassLoader.class);

        // We are at 1.1, target is 3.1
        // Expected:
        // 1.2 (patch_2.sql)
        // 2.0 (migrate.sql from 1)
        // 2.1 (patch_1.sql)
        // 2.2 (patch_2.sql)
        // 3.0 (migrate.sql from 2)
        // 3.1 (patch_1.sql)

        when(loader.getResource(anyString())).thenAnswer(invocation -> {
            String name = invocation.getArgument(0, String.class);
            return switch (name) {
                case "database/postgres/1/patch_2.sql",
                     "database/postgres/1/migrate.sql",
                     "database/postgres/2/patch_1.sql",
                     "database/postgres/2/patch_2.sql",
                     "database/postgres/2/migrate.sql",
                     "database/postgres/3/patch_1.sql" -> SqlUpdaterTest.class.getResource("SqlUpdaterTest.class");
                default -> null;
            };
        });

        when(loader.getResourceAsStream(anyString())).thenAnswer(invocation -> {
            String name = invocation.getArgument(0, String.class);
            return switch (name) {
                case "database/postgres/1/patch_2.sql" -> new ByteArrayInputStream("patch 1.2".getBytes());
                case "database/postgres/1/migrate.sql" -> new ByteArrayInputStream("migrate 1->2".getBytes());
                case "database/postgres/2/patch_1.sql" -> new ByteArrayInputStream("patch 2.1".getBytes());
                case "database/postgres/2/patch_2.sql" -> new ByteArrayInputStream("patch 2.2".getBytes());
                case "database/postgres/2/migrate.sql" -> new ByteArrayInputStream("migrate 2->3".getBytes());
                case "database/postgres/3/patch_1.sql" -> new ByteArrayInputStream("patch 3.1".getBytes());
                default -> null;
            };
        });

        Database database = mock(Database.class);
        when(database.name()).thenReturn("postgres");

        SqlUpdater updater = new SqlUpdater(mock(DataSource.class), "version", new QueryReplacement[0], new SqlVersion(3, 1), database, Map.of(), Map.of(), loader);

        var method = SqlUpdater.class.getDeclaredMethod("getPatchesFrom", int.class, int.class);
        method.setAccessible(true);

        List<Patch> patches = (List<Patch>) method.invoke(updater, 1, 1);

        assertEquals(6, patches.size());
        assertEquals(new SqlVersion(1, 2), patches.get(0).version());
        assertEquals("patch 1.2", patches.get(0).query());

        assertEquals(new SqlVersion(2, 0), patches.get(1).version());
        assertEquals("migrate 1->2", patches.get(1).query());

        assertEquals(new SqlVersion(2, 1), patches.get(2).version());
        assertEquals("patch 2.1", patches.get(2).query());

        assertEquals(new SqlVersion(2, 2), patches.get(3).version());
        assertEquals("patch 2.2", patches.get(3).query());

        assertEquals(new SqlVersion(3, 0), patches.get(4).version());
        assertEquals("migrate 2->3", patches.get(4).query());

        assertEquals(new SqlVersion(3, 1), patches.get(5).version());
        assertEquals("patch 3.1", patches.get(5).query());
    }
}
