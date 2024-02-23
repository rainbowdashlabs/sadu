module sadu.sadu.core.main {
    requires transitive java.sql;
    requires transitive org.jetbrains.annotations;
    requires transitive org.slf4j;

    exports de.chojo.sadu.core.base;
    exports de.chojo.sadu.core.conversion;
    exports de.chojo.sadu.core.databases;
    exports de.chojo.sadu.core.databases.exceptions;
    exports de.chojo.sadu.core.exceptions;
    exports de.chojo.sadu.core.jdbc;
    exports de.chojo.sadu.core.types;
    exports de.chojo.sadu.core.updater;
}
