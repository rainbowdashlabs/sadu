module sadu.sadu.mariadb.main {
    requires transitive sadu.sadu.updater.main;
    requires transitive sadu.sadu.mapper.main;

    exports de.chojo.sadu.mariadb.databases;
    exports de.chojo.sadu.mariadb.jdbc;
    exports de.chojo.sadu.mariadb.mapper;
    exports de.chojo.sadu.mariadb.types;
}
