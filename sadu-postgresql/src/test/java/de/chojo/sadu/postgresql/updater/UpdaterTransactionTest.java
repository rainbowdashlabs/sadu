/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.postgresql.updater;

import de.chojo.sadu.core.updater.SqlVersion;
import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.postgresql.databases.PostgreSql;
import de.chojo.sadu.updater.SqlUpdater;
import de.chojo.sadu.updater.UpdateException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import javax.sql.DataSource;

import static de.chojo.sadu.PostgresDatabase.createContainer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UpdaterTransactionTest {
    private static GenericContainer<?> pg;
    private static DataSource dataSource;

    @BeforeAll
    static void beforeAll() {
        pg = createContainer("postgres", "postgres");
        dataSource = DataSourceCreator.create(PostgreSql.get())
                .configure(c -> c.host(pg.getHost()).port(pg.getFirstMappedPort()))
                .create()
                .usingPassword("postgres")
                .usingUsername("postgres")
                .build();
    }

    @AfterAll
    static void afterAll() {
        pg.close();
    }

    @Test
    void failedVersionWriteRollsBackTheSetup(@TempDir Path resources) throws Exception {
        write(resources, "1/setup.sql", "CREATE TABLE setup_part(id INT);\nDROP TABLE setup_version;");

        assertThrows(
                SQLException.class,
                () -> SqlUpdater.builder(dataSource, PostgreSql.get())
                        .setVersion(new SqlVersion(1, 0))
                        .setVersionTable("setup_version")
                        .withClassLoader(loader(resources))
                        .execute());

        assertFalse(tableExists("setup_part"));
        assertEquals(0, count("setup_version"));
    }

    @Test
    void failedVersionWriteRollsBackThePatch(@TempDir Path resources) throws Exception {
        write(resources, "1/setup.sql", "CREATE TABLE patch_base(id INT);");
        write(resources, "1/patch_1.sql", "CREATE TABLE patch_part(id INT);");

        assertThrows(
                UpdateException.class,
                () -> SqlUpdater.builder(dataSource, PostgreSql.get())
                        .setVersion(new SqlVersion(1, 1))
                        .setVersionTable("patch_version")
                        .withClassLoader(loader(resources))
                        .postUpdateHook(new SqlVersion(1, 1), conn -> {
                            try (var stmt = conn.prepareStatement("DROP TABLE patch_version")) {
                                stmt.execute();
                            }
                        })
                        .execute());

        assertFalse(tableExists("patch_part"));
        assertEquals(1, count("patch_version"));
    }

    private static void write(Path resources, String file, String content) throws IOException {
        var path = resources.resolve("database/postgresql").resolve(file);
        Files.createDirectories(path.getParent());
        Files.writeString(path, content);
    }

    private static ClassLoader loader(Path resources) throws IOException {
        return new URLClassLoader(new URL[] {resources.toUri().toURL()}, null);
    }

    private static boolean tableExists(String table) throws SQLException {
        try (var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement("SELECT to_regclass(?) IS NOT NULL")) {
            stmt.setString(1, table);
            var result = stmt.executeQuery();
            result.next();
            return result.getBoolean(1);
        }
    }

    private static int count(String table) throws SQLException {
        try (var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement("SELECT count(*) FROM " + table)) {
            var result = stmt.executeQuery();
            result.next();
            return result.getInt(1);
        }
    }
}
