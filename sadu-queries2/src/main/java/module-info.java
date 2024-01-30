module sadu.sadu.queries2.main {
    requires java.sql;
    requires transitive sadu.sadu.core.main;
    requires transitive sadu.sadu.mapper.main;

    exports de.chojo.sadu.queries.api.call;
    exports de.chojo.sadu.queries.api.call.adapter;
    exports de.chojo.sadu.queries.api.call.calls;
    exports de.chojo.sadu.queries.api.execution.reading;
    exports de.chojo.sadu.queries.api.execution.writing;
    exports de.chojo.sadu.queries.api.query;
    exports de.chojo.sadu.queries.api.results.reading;
    exports de.chojo.sadu.queries.api.results.writing;

    exports de.chojo.sadu.queries.call;
    exports de.chojo.sadu.queries.call.adapter;

    exports de.chojo.sadu.queries.calls;

    exports de.chojo.sadu.queries.configuration;

    exports de.chojo.sadu.queries.exception;

    exports de.chojo.sadu.queries.params;

    exports de.chojo.sadu.queries.query;

    exports de.chojo.sadu.queries.stages;
    exports de.chojo.sadu.queries.stages.base;
    exports de.chojo.sadu.queries.stages.calls;
    exports de.chojo.sadu.queries.stages.execution.reading;
    exports de.chojo.sadu.queries.stages.execution.writing;
    exports de.chojo.sadu.queries.stages.results.reading;
    exports de.chojo.sadu.queries.stages.results.writing;

    exports de.chojo.sadu.queries.storage;
}
