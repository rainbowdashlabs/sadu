module sadu.sadu.postgresql.main {
    requires transitive sadu.sadu.updater.main;
    requires transitive sadu.sadu.mapper.main;

    exports de.chojo.sadu.postgresql.databases;
    exports de.chojo.sadu.postgresql.jdbc;
    exports de.chojo.sadu.postgresql.mapper;
    exports de.chojo.sadu.postgresql.types;
    exports de.chojo.sadu.postgresql.updater;
}
