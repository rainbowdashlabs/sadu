module de.chojo.sadu.sqlite {
    requires transitive de.chojo.sadu.updater;
    requires transitive de.chojo.sadu.mapper;

    exports de.chojo.sadu.sqlite.databases;
    exports de.chojo.sadu.sqlite.jdbc;
    exports de.chojo.sadu.sqlite.mapper;
    exports de.chojo.sadu.sqlite.types;
}
