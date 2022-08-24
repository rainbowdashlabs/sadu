/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper;

import de.chojo.sadu.wrapper.mapper.rowmapper.RowMapper;

public class Mapper {
    public static final RowMapper<Result> wildcard = RowMapper.forClass(Result.class)
                                                              .mapper(row -> new Result(row.getInt("id"),
                                                                      row.getString("result")))
                                                              .build();

    public static final RowMapper<Result> sparse = RowMapper.forClass(Result.class)
                                                            .mapper(row -> new Result(row.getInt("id"),
                                                                    row.getString("result")))
                                                            .addColumn("id")
                                                            .addColumn("result")
                                                            .build();
    public static final RowMapper<Result> full = RowMapper.forClass(Result.class)
                                                          .mapper(row -> new MetaResult(row.getInt("id"),
                                                                  row.getString("result"),
                                                                  row.getString("meta")))
                                                          .addColumn("id")
                                                          .addColumn("result")
                                                          .addColumn("meta")
                                                          .build();
}
