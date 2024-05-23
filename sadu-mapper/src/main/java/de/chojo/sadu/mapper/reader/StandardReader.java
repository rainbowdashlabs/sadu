/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.reader;

import de.chojo.sadu.mapper.wrapper.Row;

import java.sql.Timestamp;
import java.time.Instant;

public final class StandardReader {
    public static final ValueReader<Long, Instant> INSTANT_FROM_MILLIS = ValueReader.create(Instant::ofEpochMilli, Row::getLong, Row::getLong);
    public static final ValueReader<Long, Instant> INSTANT_FROM_SECONDS = ValueReader.create(Instant::ofEpochSecond, Row::getLong, Row::getLong);
    public static final ValueReader<Timestamp, Instant> INSTANT_FROM_TIMESTAMP = ValueReader.create(Timestamp::toInstant, Row::getTimestamp, Row::getTimestamp);
}
