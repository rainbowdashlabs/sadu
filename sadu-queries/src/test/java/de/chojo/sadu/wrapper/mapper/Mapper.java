/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper;

public class Mapper {
    public static final RowMapper<Result> wildcard = RowMapper.forClass(Result.class)
                                                              .setMapper(row -> new Result(row.getInt("id"),
                                                                      row.getString("result"),
                                                                      row.getString("meta")))
                                                              .build();

    public static final RowMapper<Result> sparse = RowMapper.forClass(Result.class)
                                                            .setMapper(row -> new Result(row.getInt("id"),
                                                                    row.getString("result"),
                                                                    row.getString("meta")))
                                                            .addColumn("id")
                                                            .addColumn("result")
                                                            .build();
    public static final RowMapper<Result> full = RowMapper.forClass(Result.class)
                                                          .setMapper(row -> new Result(row.getInt("id"),
                                                                  row.getString("result")))
                                                          .addColumn("id")
                                                          .addColumn("result")
                                                          .addColumn("meta")
                                                          .build();
}
