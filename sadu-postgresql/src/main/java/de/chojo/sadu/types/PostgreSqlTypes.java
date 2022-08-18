/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.types;

import static de.chojo.sadu.types.SqlType.ofName;

public class PostgreSqlTypes {
    SqlType CHAR = ofName("CHAR");
    SqlType TEXT = ofName("TEXT");
    SqlType VARCHAR = ofName("VARCHAR");
    SqlType SMALLINT = ofName("SMALLINT");
    SqlType INTEGER = ofName("INTEGER");
    SqlType BIGINT = ofName("BIGINT");
    SqlType DECIMAL = ofName("DECIMAL");
    SqlType NUMERIC = ofName("NUMERIC");
    SqlType DOUBLE = ofName("DOUBLE");
    SqlType BOOLEAN = ofName("BOOLEAN");
    SqlType BYTEA = ofName("BYTEA");
    SqlType DATE = ofName("DATE");
    SqlType TIME = ofName("TIME");
    SqlType TIMESTAMPTZ = ofName("TIMESTAMPTZ");
    SqlType TIMESTAMP = ofName("TIMESTAMP");
}
