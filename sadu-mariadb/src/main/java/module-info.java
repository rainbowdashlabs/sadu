module de.chojo.sadu.mariadb {
    requires transitive de.chojo.sadu.updater;
    requires transitive de.chojo.sadu.mapper;

    exports de.chojo.sadu.mariadb.databases;
    exports de.chojo.sadu.mariadb.jdbc;
    exports de.chojo.sadu.mariadb.mapper;
    exports de.chojo.sadu.mariadb.types;
}
