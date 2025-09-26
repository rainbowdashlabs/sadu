module de.chojo.sadu.datasource {
    requires transitive de.chojo.sadu.core;
    requires transitive com.zaxxer.hikari;

    exports de.chojo.sadu.datasource.stage;
    exports de.chojo.sadu.datasource;
    exports de.chojo.sadu.datasource.exceptions;
    exports de.chojo.sadu.datasource.configuration;
}
