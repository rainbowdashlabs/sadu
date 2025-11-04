/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class TokenizedQuery {
    private static final Map<String, TokenizedQuery> cache = new HashMap<>();
    public static final String ALLOWED_TOKEN_CHARACTER = "a-zA-Z_";
    public static final Pattern TOKEN_PATTERN = Pattern.compile(":[" + ALLOWED_TOKEN_CHARACTER + "]+");
    public static final Pattern PARAM_TOKEN = Pattern.compile("\\?|(?:([ \t,=(])(?<token>" + TOKEN_PATTERN + "))");
    private final Map<Integer, Integer> indexToken;
    private final Map<String, List<Integer>> namedToken;
    private final String sql;
    private final String tokenizedSql;

    @Deprecated(forRemoval = true, since = "2.3.4")
    public TokenizedQuery(String sql, Map<Integer, Integer> indexToken, Map<String, List<Integer>> namedToken) {
        this(sql, PARAM_TOKEN.matcher(sql).replaceAll("$1?"), indexToken, namedToken);
    }

    private TokenizedQuery(String sql, String tokenizedSql, Map<Integer, Integer> indexToken, Map<String, List<Integer>> namedToken) {
        this.sql = sql;
        this.tokenizedSql = tokenizedSql;
        this.indexToken = indexToken;
        this.namedToken = namedToken;
    }

    public static TokenizedQuery create(String sql) {
        return cache.computeIfAbsent(sql, TokenizedQuery::parse);
    }

    private static TokenizedQuery parse(String sql){
        var matcher = PARAM_TOKEN.matcher(sql);
        var index = 1;
        var currIndexToken = 1;
        Map<Integer, Integer> indexToken = new HashMap<>();
        Map<String, List<Integer>> namedToken = new HashMap<>();


        while (matcher.find()) {
            if ("?".equals(matcher.group())) {
                indexToken.put(currIndexToken++, index++);
            } else {
                namedToken.computeIfAbsent(matcher.group("token"), key -> new ArrayList<>()).add(index++);
            }
        }
        var tokenizedSql = PARAM_TOKEN.matcher(sql).replaceAll("$1?");
        return new TokenizedQuery(sql, tokenizedSql, indexToken, namedToken);
    }

    public List<Integer> getNamedTokenIndex(String token) {
        return namedToken.getOrDefault(token, Collections.emptyList());
    }

    public Set<String> getNamedTokens() {
        return new HashSet<>(namedToken.keySet());
    }

    public int getIndexTokenIndex(int index) {
        return indexToken.get(index);
    }

    public int indexSize() {
        return indexToken.size();
    }

    public String sql() {
        return sql;
    }

    public String tokenizedSql() {
        return tokenizedSql;
    }
}
