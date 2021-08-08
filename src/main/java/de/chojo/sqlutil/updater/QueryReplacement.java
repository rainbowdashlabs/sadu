package de.chojo.sqlutil.updater;

public class QueryReplacement {
    String target;
    String replacement;

    public QueryReplacement(String target, String replacement) {
        this.target = target;
        this.replacement = replacement;
    }

    public String apply(String source) {
        return source.replaceAll(target, replacement);
    }
}
