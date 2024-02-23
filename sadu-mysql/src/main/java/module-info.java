module sadu.sadu.mysql.main {
    requires transitive sadu.sadu.mapper.main;
    requires transitive sadu.sadu.updater.main;

    exports de.chojo.sadu.mysql.databases;
    exports de.chojo.sadu.mysql.jdbc;
    exports de.chojo.sadu.mysql.mapper;
    exports de.chojo.sadu.mysql.types;
}
