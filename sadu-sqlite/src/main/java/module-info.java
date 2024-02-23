module sadu.sadu.sqlite.main {
    requires transitive sadu.sadu.updater.main;
    requires transitive sadu.sadu.mapper.main;

    exports de.chojo.sadu.sqlite.databases;
    exports de.chojo.sadu.sqlite.jdbc;
    exports de.chojo.sadu.sqlite.mapper;
    exports de.chojo.sadu.sqlite.types;
}
