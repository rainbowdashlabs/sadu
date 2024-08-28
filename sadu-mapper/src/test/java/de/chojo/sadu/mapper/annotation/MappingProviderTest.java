/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.annotation;

import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.mapper.exceptions.InvalidMappingException;
import de.chojo.sadu.mapper.rowmapper.RowMapping;
import de.chojo.sadu.mapper.wrapper.Row;
import de.chojo.sadu.postgresql.databases.PostgreSql;
import de.chojo.sadu.postgresql.mapper.PostgresqlMapper;
import de.chojo.sadu.queries.api.configuration.QueryConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static de.chojo.sadu.mapper.PostgresDatabase.createContainer;
import static de.chojo.sadu.queries.api.query.Query.query;

public class MappingProviderTest {


    private static GenericContainer<?> pg;

    @BeforeAll
    static void beforeAll() throws IOException {
        pg = createContainer("postgres", "postgres");
        DataSource dc = DataSourceCreator.create(PostgreSql.get())
                .configure(c -> c.host(pg.getHost()).port(pg.getFirstMappedPort())).create()
                .usingPassword("postgres")
                .usingUsername("postgres")
                .build();
        QueryConfiguration.setDefault(QueryConfiguration.builder(dc)
                .setRowMapperRegistry(new RowMapperRegistry().register(PostgresqlMapper.getDefaultMapper()))
                .build());
    }

    @Test
    public void testMethodMapperRegistration() {
        MethodMappedClass methodMappedClass = query("Select 'test' as test")
                .single()
                .mapAs(MethodMappedClass.class)
                .first()
                .get();
        Assertions.assertEquals(methodMappedClass.test, "test");
    }

    @Test
    public void testConstructorMapperRegistration() {
        ConstructorMappedClass methodMappedClass = query("Select 'test' as test")
                .single()
                .mapAs(ConstructorMappedClass.class)
                .first()
                .get();
        Assertions.assertEquals(methodMappedClass.test, "test");
    }

    @Test
    public void testInvalidRegistration() {
        Assertions.assertThrows(InvalidMappingException.class, () -> {
            query("Select 'test' as test")
                    .single()
                    .mapAs(InvalidClass.class)
                    .first();
        });
    }

    @AfterAll
    static void afterAll() throws IOException {
        pg.close();
    }

    public static class MethodMappedClass {

        String test;

        public MethodMappedClass(String test) {
            this.test = test;
        }

        @MappingProvider({"test"})
        public static RowMapping<MethodMappedClass> map() {
            return row -> new MethodMappedClass(row.getString(1));
        }
    }

    public static class ConstructorMappedClass {

        String test;

        public ConstructorMappedClass(String test) {
            this.test = test;
        }

        @MappingProvider({"test"})
        public ConstructorMappedClass(Row row) throws SQLException {
            this.test = row.getString("test");
        }

    }

    public static class InvalidClass {

        String test;

        @MappingProvider({"test"})
        public InvalidClass(String test) {
            this.test = test;
        }
    }
}
