/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper;

import de.chojo.sadu.base.QueryFactory;
import de.chojo.sadu.databases.MariaDb;
import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.wrapper.QueryBuilderConfig;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.containers.GenericContainer;

import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Stream;

import static de.chojo.sadu.MariaDbDatabase.createContainer;

class MariaDbMapperTest {
    @Language("mariadb")
    static final String TEST_TABLE = """
            CREATE TABLE test
            (
                _bigint     BIGINT,
                _integer    INTEGER,
                _mediumint  MEDIUMINT,
                _smallint   SMALLINT,
                _tinyint    TINYINT,
                _char       CHAR,
                _tinytext   TINYTEXT,
                _text       TEXT,
                _mediumtext MEDIUMTEXT,
                _longtext   LONGTEXT,
                _decimal    DECIMAL(30, 5),
                _double     DOUBLE,
                _float      FLOAT,
                _bool       BOOLEAN,
                _varchar    VARCHAR(1000),
                _date       DATE,
                _time       TIME,
                _timestamp  TIMESTAMP
            );
            """;
    @Language("mariadb")
    static final String INSERTS = """
            INSERT INTO test(_bigint, _integer, _mediumint, _smallint, _tinyint, _char, _tinytext, _text, _mediumtext, _longtext, _decimal, _double, _float, _bool, _varchar, _date, _time, _timestamp)
            VALUES(9223372036854775807, 2147483647, 8388607,32767, 127,
                   'a', 'text', 'text', 'text', 'text',
                   3276745687654.87845,
                   3276745687.1265487845,
                   3267.13,
                   TRUE,
                   'text',
                   '2000-01-01',
                   '12:00:00',
                   '2000-01-01 12:00:00'
                   );
            """;
    static final String BIGINT = "_bigint";
    static final String INTEGER = "_integer";
    static final String MEDIUMINT = "_mediumint";
    static final String SMALLINT = "_smallint";
    static final String TINYINT = "_tinyint";
    static final String CHAR = "_char";
    static final String TINYTEXT = "_tinytext";
    static final String TEXT = "_text";
    static final String MEDIUMTEXT = "_mediumtext";
    static final String LONGTEXT = "_longtext";
    static final String DECIMAL = "_decimal";
    static final String DOUBLE = "_double";
    static final String FLOAT = "_float";
    static final String BOOL = "_bool";
    static final String VARCHAR = "_varchar";
    static final String DATE = "_date";
    static final String TIME = "_time";
    static final String TIMESTAMP = "_timestamp";
    static final long maxLong = 9223372036854775807L;
    static final int maxInt = 2147483647;
    static final int maxMedium = 8388607;
    static final short maxShort = 32767;
    static final short maxTiny = 127;
    static final float floatVal = 3267.13f;
    static final double doubleVal = 3276745687.1265487845;
    static final BigDecimal bigDecimal = BigDecimal.valueOf(327674568765487845L, 5);
    static final String text = "text";
    private static GenericContainer<?> db;
    private static QueryFactory factory;

    @BeforeAll
    static void beforeAll() throws IOException {
        db = createContainer("root", "root");
        DataSource dc = DataSourceCreator.create(MariaDb.get())
                .configure(c -> c.host(db.getHost()).port(db.getFirstMappedPort()).database("mysql")).create()
                .usingPassword("root")
                .usingUsername("root")
                .build();
        var config = QueryBuilderConfig.builder()
                .rowMappers(new RowMapperRegistry().register(MariaDbMapper.getDefaultMapper()))
                .build();
        factory = new QueryFactory(dc, config);
        factory.builder().query(TEST_TABLE).emptyParams().insert().sendSync();
        assert factory.builder().query(INSERTS).emptyParams().insert().sendSync().rows() > 0 : "Could not insert data.";
    }

    static Stream<Arguments> shortTestInput() {
        return Stream.of(
                // TINY_INT
                Arguments.of(Short.class, TINYINT, maxTiny),
                Arguments.of(Integer.class, TINYINT, (int) maxTiny),
                Arguments.of(Long.class, TINYINT, (long) maxTiny),
                // SMALL_INT
                Arguments.of(Short.class, SMALLINT, maxShort),
                Arguments.of(Integer.class, SMALLINT, (int) maxShort),
                Arguments.of(Long.class, SMALLINT, (long) maxShort),
                // MEDIUM_INT
                Arguments.of(Integer.class, MEDIUMINT, maxMedium),
                Arguments.of(Long.class, MEDIUMINT, (long) maxMedium),
                // INTEGER
                Arguments.of(Integer.class, INTEGER, maxInt),
                Arguments.of(Long.class, INTEGER, (long) maxInt),
                // BIGINT
                Arguments.of(Long.class, BIGINT, maxLong),
                // FLOAT
                Arguments.of(Float.class, FLOAT, floatVal),
                // DOUBLE PRECISION
                Arguments.of(Double.class, DOUBLE, doubleVal),
                // DECIMAL
                Arguments.of(BigDecimal.class, DECIMAL, bigDecimal),
                // TEXT
                Arguments.of(String.class, TINYTEXT, text),
                Arguments.of(String.class, MEDIUMTEXT, text),
                Arguments.of(String.class, TEXT, text),
                Arguments.of(String.class, LONGTEXT, text),
                // VARCHAR
                Arguments.of(String.class, VARCHAR, text),
                // CHAR
                Arguments.of(String.class, CHAR, "a"),
                Arguments.of(Boolean.class, BOOL, true)
        );
    }

    @AfterAll
    static void afterAll() throws IOException {
        db.close();
    }

    @ParameterizedTest
    @MethodSource("shortTestInput")
    <T> void testAutoParsing(Class<T> clazz, String _col, T expected) {
        var val = factory.builder(clazz)
                .query("SELECT %s FROM test", _col)
                .emptyParams()
                .map()
                .firstSync();
        Assertions.assertTrue(val.isPresent());
        Assertions.assertEquals(expected, val.get());
    }
}
