module sadu.sadu.datasource.main {
    requires transitive sadu.sadu.core.main;
    requires transitive com.zaxxer.hikari;

    exports de.chojo.sadu.datasource.stage;
    exports de.chojo.sadu.datasource;
    exports de.chojo.sadu.datasource.exceptions;
    exports de.chojo.sadu.datasource.configuration;
}
