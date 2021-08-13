package de.chojo.sqlutil.updater;

import java.util.Arrays;

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

    default String[] splitStatements(String queries) {
        return new String[]{queries};
    }

    default String[] cleanStatements(String[] queries) {
        return Arrays.stream(queries).filter(query -> !query.isBlank()).toArray(String[]::new);
    }

    abstract class DefaultType implements SqlType {

        @Override
        public String getVersion(String table) {
            return "SELECT major, patch FROM " + table + " LIMIT 1";
        }

        @Override
        public String insertVersion(String table) {
            return "INSERT INTO " + table + " VALUES (?, ?)";
        }

        @Override
        public String deleteVersion(String table) {
            return "DELETE FROM " + table;
        }

        @Override
        public String createVersionTableQuery(String table) {
            return "CREATE TABLE IF NOT EXISTS " + table + "(major INTEGER, patch INTEGER);";
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
            return "CREATE TABLE IF NOT EXISTS " + table + "(major int, patch int);";
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
