/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.testing;

import de.chojo.sadu.postgresql.databases.PostgreSql;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SaduTest {
    @Test
    @Disabled
    public void checkDatabase() throws IOException {
        SaduTests.execute(1, PostgreSql.get());
    }
}
