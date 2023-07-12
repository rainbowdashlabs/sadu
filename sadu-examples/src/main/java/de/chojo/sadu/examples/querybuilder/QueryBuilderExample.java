/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.examples.querybuilder;

import de.chojo.sadu.base.QueryFactory;
import de.chojo.sadu.wrapper.QueryBuilder;
import de.chojo.sadu.wrapper.QueryBuilderConfig;

import javax.sql.DataSource;

public class QueryBuilderExample extends QueryFactory {
    public QueryBuilderExample(DataSource dataSource) {
        super(dataSource);
    }

    public static void main(String[] args) {
        QueryBuilder.builder(null)
                .configure(QueryBuilderConfig.builder().build())
                .query("INSERT INTO")
                .emptyParams()
                .readRow(null)
                .all();

        QueryBuilder.builder(null)
                .configure(QueryBuilderConfig.builder().build())
                .query("INSERT INTO")
                .emptyParams()
                .delete()
                .sendSync()
                .rows();

        QueryBuilder.builder(null)
                .configure(QueryBuilderConfig.builder().build())
                .query("INSERT INTO")
                .emptyParams()
                .insert()

                .key();
    }

    public void createUser(String name, int age) {
        builder().query("INSERT INTO user(name, age) VALUES(?,?)")
                .parameter(p -> p.setString(name).setInt(age))
                .insert()
                .send();
    }
}
