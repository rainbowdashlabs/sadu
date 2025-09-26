module de.chojo.sadu.mysql {
    requires transitive de.chojo.sadu.mapper;
    requires transitive de.chojo.sadu.updater;

    exports de.chojo.sadu.mysql.databases;
    exports de.chojo.sadu.mysql.jdbc;
    exports de.chojo.sadu.mysql.mapper;
    exports de.chojo.sadu.mysql.types;
}
