/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.types;

import static de.chojo.sadu.types.SqlType.ofName;

/**
 * Types present in a MySQL database.
 */
public interface MySqlTypes {
    /**
     * Fixed {@literal <} 255 with padding
     */
    SqlType CHAR = ofName("CHAR");

    /**
     * {@literal <} 65,353 Bytes
     */
    SqlType VARCHAR = ofName("VARCHAR");

    /**
     * {@literal <} 255 Bytes
     */
    SqlType TINYTEXT = ofName("TINYTEXT");
    /**
     * {@literal <} 65,353 Bytes
     */
    SqlType TEXT = ofName("TEXT");
    /**
     * {@literal <} 16,777,215 Bytes
     */
    SqlType MEDIUMTEXT = ofName("MEDIUMTEXT");
    /**
     * {@literal <} 4,294,967,295 Bytes
     */
    SqlType LONGTEXT = ofName("LONGTEXT");

    /**
     * -128 and 127
     */
    SqlType TINYINT = ofName("TINYINT");
    /**
     * -32,768 and 32,767
     */
    SqlType SMALLINT = ofName("SMALLINT");
    /**
     * -8,288,608 and 8,388,607
     */
    SqlType MEDIUMINT = ofName("MEDIUMINT");
    /**
     * -2,147,483,648 and 2,147,483,647
     */
    SqlType INT = ofName("INT", "INTEGER");
    /**
     * "Unlimited"
     */
    SqlType BIGINT = ofName("BIGINT");

    /**
     * exact fixed point
     */
    SqlType DECIMAL = ofName("DECIMAL");
    /**
     * double precision
     */
    SqlType DOUBLE = ofName("DOUBLE");
    /**
     * single precision
     */
    SqlType FLOAT = ofName("FLOAT");

    /**
     * Boolean representation
     */
    SqlType BOOLEAN = ofName("BOOLEAN", "INTEGER");

    /**
     * Fixed {@literal <} 255 with padding
     */
    SqlType BINARY = ofName("BINARY");
    /**
     * {@literal <} 65,353 Bytes
     */
    SqlType VARBINARY = ofName("VARBINARY");

    /**
     * {@literal <} 255 Bytes
     */
    SqlType TINYBLOB = ofName("TINYBLOB");
    /**
     * {@literal <} 65,353
     */
    SqlType BLOB = ofName("BLOB");
    /**
     * {@literal <} 16,777,215 Bytes
     */
    SqlType MEDIUMBLOB = ofName("MEDIUMBLOB");
    /**
     * {@literal <} 4,294,967,295
     */
    SqlType LONGBLOB = ofName("LONGBLOB");

    /**
     * Date
     */
    SqlType DATE = ofName("DATE");
    /**
     * Time
     */
    SqlType TIME = ofName("TIME");
    /**
     * Timestamp
     */
    SqlType TIMESTAMP = ofName("TIMESTAMP");

}
