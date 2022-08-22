/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper.exceptions;

import de.chojo.sadu.wrapper.mapper.util.Result;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MappingException extends RuntimeException {

    public MappingException(ResultSetMetaData meta) throws SQLException {
        super("No mapper present for " + String.join(", ", Result.columnNames(meta)));
    }
}
