module de.chojo.sadu.queries {
    requires transitive de.chojo.sadu.core;
    requires transitive de.chojo.sadu.mapper;

    exports de.chojo.sadu.queries.api.base;
    exports de.chojo.sadu.queries.api.call;
    exports de.chojo.sadu.queries.api.call.adapter;
    exports de.chojo.sadu.queries.api.call.calls;
    exports de.chojo.sadu.queries.api.execution.reading;
    exports de.chojo.sadu.queries.api.execution.writing;
    exports de.chojo.sadu.queries.api.configuration;
    exports de.chojo.sadu.queries.api.configuration.context;
    exports de.chojo.sadu.queries.api.parameter;
    exports de.chojo.sadu.queries.api.query;
    exports de.chojo.sadu.queries.api.exception;
    exports de.chojo.sadu.queries.api.results;
    exports de.chojo.sadu.queries.api.results.reading;
    exports de.chojo.sadu.queries.api.results.writing.manipulation;
    exports de.chojo.sadu.queries.api.results.writing.insertion;
    exports de.chojo.sadu.queries.api.storage;

    exports de.chojo.sadu.queries.call;
    exports de.chojo.sadu.queries.call.adapter;

    exports de.chojo.sadu.queries.calls;

    exports de.chojo.sadu.queries.configuration;
    exports de.chojo.sadu.queries.configuration.context;

    exports de.chojo.sadu.queries.exception;

    exports de.chojo.sadu.queries.parameter;

    exports de.chojo.sadu.queries.query;

    exports de.chojo.sadu.queries.execution.reading;
    exports de.chojo.sadu.queries.execution.writing;
    exports de.chojo.sadu.queries.results.writing.manipulation;
    exports de.chojo.sadu.queries.results.writing.insertion;
    exports de.chojo.sadu.queries.results.reading;

    exports de.chojo.sadu.queries.storage;
}
