/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.exceptions;

import de.chojo.sadu.mapper.util.Results;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MappingException extends RuntimeException {

    public MappingException(String message) {
        super(message);
    }

    public static MappingException create(ResultSetMetaData meta) throws SQLException {
        return new MappingException("No mapper present for " + String.join(", ", Results.columnNames(meta)));
    }
}
