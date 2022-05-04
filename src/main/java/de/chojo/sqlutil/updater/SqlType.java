/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.updater;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public interface SqlType {

    SqlType POSTGRES = new Postgres();

    SqlType MARIADB = new MariaDb();

    SqlType SQLITE = new SqLite();

    SqlType MYSQL = new MySql();

    String createVersionTableQuery(String table);

    String getVersion(String table);

    String getName();

    String insertVersion(String table);

    String deleteVersion(String table);

    default boolean useJdbcUrl() {
        return false;
    }

    default String buildJdbcUrl(Properties properties) {
        if (!useJdbcUrl()) return "";
        throw new RuntimeException("build url is not implementd, but a jdbc url is required");
    }

    default String[] splitStatements(String queries) {
        return new String[]{queries};
    }

    default String[] cleanStatements(String[] queries) {
        return Arrays.stream(queries).filter(query -> !query.isBlank()).toArray(String[]::new);
    }

    abstract class DefaultType implements SqlType {

        @Override
        public String getVersion(String table) {
            return String.format("SELECT major, patch FROM %s LIMIT 1", table);
        }

        @Override
        public String insertVersion(String table) {
            return String.format("INSERT INTO %s VALUES (?, ?)", table);
        }

        @Override
        public String deleteVersion(String table) {
            return String.format("DELETE FROM %s;", table);
        }

        @Override
        public String createVersionTableQuery(String table) {
            return String.format("CREATE TABLE IF NOT EXISTS %s(major INTEGER, patch INTEGER);", table);
        }
    }

    class MariaDb extends DefaultType {

        @Override
        public String getName() {
            return "mariadb";
        }

        @Override
        public String[] splitStatements(String queries) {
            return cleanStatements(queries.split(";"));
        }

        @Override
        public boolean useJdbcUrl() {
            return true;
        }

        @Override
        public String buildJdbcUrl(Properties properties) {
            // jdbc:mariadb:[replication:|loadbalance:|sequential:]//<hostDescription>[,<hostDescription>...]/[database][?<key1>=<value1>[&<key2>=<value2>]]
            var jdbc = new StringBuilder("jdbc:mariadb://");

            var props = properties.entrySet().stream()
                    .filter(entry -> ((String) entry.getKey()).startsWith("dataSource."))
                    .collect(Collectors.toMap(
                            entry -> ((String) entry.getKey()).replace("dataSource.", ""),
                            entry -> (String) entry.getValue()));

            if (props.containsKey("serverName")) {
                jdbc.append(props.remove("serverName"));
            }

            if (props.containsKey("portNumber")) {
                jdbc.append(":").append(props.remove("portNumber"));
            }

            if (props.containsKey("databaseName")) {
                jdbc.append("/").append(props.remove("databaseName"));
            }

            if(!props.isEmpty()){
                jdbc.append("?");
            }

            var values = props.entrySet().stream()
                    .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining("&"));

            jdbc.append(values);

            return jdbc.toString();
        }
    }

    class MySql extends DefaultType {

        @Override
        public String getName() {
            return "mysql";
        }

        @Override
        public String[] splitStatements(String queries) {
            return cleanStatements(queries.split(";"));
        }
    }

    class Postgres extends DefaultType {

        @Override
        public String getName() {
            return "postgres";
        }
    }

    class SqLite extends DefaultType {
        @Override
        public String createVersionTableQuery(String table) {
            return String.format("CREATE TABLE IF NOT EXISTS %s(major INT, patch INT);", table);
        }

        @Override
        public String getName() {
            return "sqlite";
        }

        @Override
        public String[] splitStatements(String queries) {
            return cleanStatements(queries.split(";"));
        }
    }
}
