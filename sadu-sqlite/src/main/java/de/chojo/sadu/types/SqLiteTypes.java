/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.types;

import static de.chojo.sadu.types.SqlType.ofName;

public interface SqLiteTypes {
    SqlType TEXT = ofName("TEXT");
    SqlType INTEGER = ofName("INTEGER");
    SqlType REAL = ofName("REAL");
    // Internally interpreted as an INTEGER
    SqlType BOOLEAN = ofName("BOOLEAN");
    SqlType BLOB = ofName("BOOLEAN");
}
