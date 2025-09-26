module de.chojo.sadu.postgresql {
    requires transitive de.chojo.sadu.updater;
    requires transitive de.chojo.sadu.mapper;

    exports de.chojo.sadu.postgresql.databases;
    exports de.chojo.sadu.postgresql.jdbc;
    exports de.chojo.sadu.postgresql.mapper;
    exports de.chojo.sadu.postgresql.types;
    exports de.chojo.sadu.postgresql.updater;
}
