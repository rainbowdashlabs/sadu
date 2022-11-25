/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries;

import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.calls.Calls;
import org.intellij.lang.annotations.Language;

import javax.sql.DataSource;

public class Query<T> {

    private DataSource dataSource;
    private TokenizedQuery sql;
    private Calls calls;

    public Query<?> query(@Language("sql") String sql) {
        this.sql = TokenizedQuery.create(sql);
        return this;
    }

    public Query<?> query(@Language("sql") String sql, Object... format) {
        return query(sql.formatted(format));
    }

    public Query<?> parameter(Calls param) {
        calls = param;
        return this;
    }

    public Query<?> parameter(Call... calls) {
        this.calls = Calls.batch(calls);
        return this;
    }

    public Query<?> parameter(Call param) {
        calls = Calls.call(param);
        return this;
    }
}
