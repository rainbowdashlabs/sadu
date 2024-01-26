/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TokenizedQuery {
    private static final Pattern PARAM_TOKEN = Pattern.compile("\\?|(?:([ \t,=(])(?<token>:[a-zA-Z_]+))");
    private final Map<Integer, Integer> indexToken;
    private final Map<String, List<Integer>> namedToken;
    private final String sql;
    private final String tokenizedSql;

    public TokenizedQuery(String sql, Map<Integer, Integer> indexToken, Map<String, List<Integer>> namedToken) {
        this.sql = sql;
        tokenizedSql = PARAM_TOKEN.matcher(sql).replaceAll("$1?");
        this.indexToken = indexToken;
        this.namedToken = namedToken;
    }

    public static TokenizedQuery create(String sql) {
        var matcher = PARAM_TOKEN.matcher(sql);
        var index = 1;
        var currIndexToken = 1;
        Map<Integer, Integer> indexToken = new HashMap<>();
        Map<String, List<Integer>> namedToken = new HashMap<>();


        while (matcher.find()) {
            if ("?".equals(matcher.group())) {
                indexToken.put(currIndexToken++, index++);
            } else {
                namedToken.computeIfAbsent(matcher.group("token"), k -> new ArrayList<>()).add(index++);
            }
        }
        return new TokenizedQuery(sql, indexToken, namedToken);
    }

    public List<Integer> getNamedTokenIndex(String token) {
        return namedToken.get(token);
    }

    public int getIndexTokenIndex(int index) {
        return indexToken.get(index);
    }

    public String sql() {
        return sql;
    }

    public String tokenizedSql() {
        return tokenizedSql;
    }
}
