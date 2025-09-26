/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.sqlite.types;

import de.chojo.sadu.core.types.SqlType;

import static de.chojo.sadu.core.types.SqlType.ofName;

/**
 * Types present in a SqLite database.
 */
public interface SqLiteTypes {


    /**
     * SqLite Text type
     */
    SqlType TEXT = ofName("TEXT");
    /**
     * SqLite integer type. Up to 64 bit.
     */
    SqlType INTEGER = ofName("INTEGER");
    /**
     * SqLite real type
     */
    SqlType REAL = ofName("REAL");
    /**
     * SqLite boolean type
     * Internally interpreted as an INTEGER
     */
    SqlType BOOLEAN = ofName("BOOLEAN", "INTEGER");
    /**
     * SqLite blob type
     */
    SqlType BLOB = ofName("BLOB");
}
