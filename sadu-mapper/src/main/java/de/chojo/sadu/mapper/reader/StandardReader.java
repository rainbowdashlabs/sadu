/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.reader;

import de.chojo.sadu.core.exceptions.ThrowingBiFunction;
import de.chojo.sadu.mapper.wrapper.Row;

import java.sql.SQLException;
import java.time.Instant;
import java.util.function.Function;

public class StandardReader {
    public ValueReader<Long, Instant> INSTANT_FROM_MILLIS = ValueReader.create(Instant::ofEpochMilli, Row::getLong, Row::getLong);
}
