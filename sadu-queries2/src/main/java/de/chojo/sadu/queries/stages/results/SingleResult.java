/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.results;

import de.chojo.sadu.base.DataSourceProvider;

import javax.sql.DataSource;
import javax.xml.transform.Result;

public class SingleResult<V> implements DataSourceProvider {
    private V result;

    public SingleResult(V result) {
        this.result = result;
    }

    @Override
    public DataSource source() {
        return null;
    }
}
