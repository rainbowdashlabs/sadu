module sadu.sadu.queries2.main {
    requires java.sql;
    requires transitive sadu.sadu.core.main;
    requires transitive sadu.sadu.mapper.main;

    exports de.chojo.sadu.queries.api.base;
    exports de.chojo.sadu.queries.api.call;
    exports de.chojo.sadu.queries.api.call.adapter;
    exports de.chojo.sadu.queries.api.call.calls;
    exports de.chojo.sadu.queries.api.execution.reading;
    exports de.chojo.sadu.queries.api.execution.writing;
    exports de.chojo.sadu.queries.api.parameter;
    exports de.chojo.sadu.queries.api.query;
    exports de.chojo.sadu.queries.api.results.reading;
    exports de.chojo.sadu.queries.api.results.writing;
    exports de.chojo.sadu.queries.api.storage;

    exports de.chojo.sadu.queries.call;
    exports de.chojo.sadu.queries.call.adapter;

    exports de.chojo.sadu.queries.calls;

    exports de.chojo.sadu.queries.configuration;

    exports de.chojo.sadu.queries.exception;

    exports de.chojo.sadu.queries.parameter;

    exports de.chojo.sadu.queries.query;

    exports de.chojo.sadu.queries.stages.execution.reading;
    exports de.chojo.sadu.queries.stages.execution.writing;
    exports de.chojo.sadu.queries.stages.results.reading;
    exports de.chojo.sadu.queries.stages.results.writing;

    exports de.chojo.sadu.queries.storage;
}
