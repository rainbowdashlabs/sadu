module sadu.sadu.mapper.main {
    requires transitive java.sql;
    requires org.slf4j;
    requires transitive sadu.sadu.core.main;
    requires transitive org.jetbrains.annotations;

    exports de.chojo.sadu.mapper;
    exports de.chojo.sadu.mapper.exceptions;
    exports de.chojo.sadu.mapper.rowmapper;
    exports de.chojo.sadu.mapper.util;
    exports de.chojo.sadu.wrapper.util;
}
