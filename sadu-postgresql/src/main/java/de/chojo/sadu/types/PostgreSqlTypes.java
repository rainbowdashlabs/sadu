/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.types;

import static de.chojo.sadu.types.SqlType.ofName;

/**
 * Types present in a PostgreSQL database.
 */
public interface PostgreSqlTypes {
    /**
     * Up to 1 GB
     */
    SqlType CHAR = ofName("CHAR", "bpchar");
    /**
     * A text with "unlimited" length
     */
    SqlType TEXT = ofName("TEXT");

    /**
     * A text with "unlimited" length
     */
    SqlType VARCHAR = ofName("VARCHAR");
    /**
     * -32,768 and 32,767
     */
    SqlType SMALLINT = ofName("SMALLINT", "int2", "int4", "int8");
    /**
     * -2,147,483,648 and 2,147,483,647
     */
    SqlType INTEGER = ofName("INTEGER", "int16", "int32");
    /**
     * "Unlimited" size
     */
    SqlType BIGINT = ofName("BIGINT", "int64");
    /**
     * exact fixed point
     */
    SqlType DECIMAL = ofName("DECIMAL");
    /**
     * exact fixed point
     */
    SqlType NUMERIC = ofName("NUMERIC");
    /**
     * exact fixed point
     */
    SqlType REAL = ofName("REAL", "float4");
    /**
     * double precision
     */
    SqlType DOUBLE = ofName("DOUBLE", "DOUBLE PRECISION", "float8");
    /**
     * Boolean
     */
    SqlType BOOLEAN = ofName("BOOLEAN");
    /**
     * Byte array
     */
    SqlType BYTEA = ofName("BYTEA");
    /**
     * JSON
     */
    SqlType JSON = ofName("json");
    /**
     * JSONB
     */
    SqlType JSONB = ofName("jsonb");
    /**
     * Date
     */
    SqlType DATE = ofName("DATE");
    /**
     * Time
     */
    SqlType TIME = ofName("TIME");
    /**
     * Timestamp with timezone
     */
    SqlType TIMESTAMPTZ = ofName("TIMESTAMPTZ");
    /**
     * Timestamp without timezone
     */
    SqlType TIMESTAMP = ofName("TIMESTAMP");
}
