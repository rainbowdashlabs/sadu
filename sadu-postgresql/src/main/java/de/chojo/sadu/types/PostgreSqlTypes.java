/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
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
    SqlType CHAR = ofName("CHAR");
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
    SqlType SMALLINT = ofName("SMALLINT");
    /**
     * -2,147,483,648 and 2,147,483,647
     */
    SqlType INTEGER = ofName("INTEGER");
    /**
     * "Unlimited" size
     */
    SqlType BIGINT = ofName("BIGINT");
    /**
     * exact fixed point
     */
    SqlType DECIMAL = ofName("DECIMAL");
    /**
     * exact fixed point
     */
    SqlType NUMERIC = ofName("NUMERIC");
    /**
     * double precision
     */
    SqlType DOUBLE = ofName("DOUBLE");
    /**
     * Boolean
     */
    SqlType BOOLEAN = ofName("BOOLEAN");
    /**
     * Byte array
     */
    SqlType BYTEA = ofName("BYTEA");
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
