module sadu.sadu.core.main {
    requires java.sql;
    requires org.jetbrains.annotations;

    exports de.chojo.sadu.base;
    exports de.chojo.sadu.conversion;
    exports de.chojo.sadu.databases;
    exports de.chojo.sadu.databases.exceptions;
    exports de.chojo.sadu.exceptions;
    exports de.chojo.sadu.jdbc;
    exports de.chojo.sadu.types;
    exports de.chojo.sadu.updater;
}
