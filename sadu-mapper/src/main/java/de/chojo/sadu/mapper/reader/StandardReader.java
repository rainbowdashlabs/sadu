/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.reader;

import de.chojo.sadu.core.conversion.UUIDConverter;
import de.chojo.sadu.mapper.wrapper.Row;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.UUID;

/**
 * Default implementations of {@link ValueReader}.
 */
public final class StandardReader {
    public static final ValueReader<Instant, Long> INSTANT_FROM_MILLIS = ValueReader.create(Instant::ofEpochMilli, Row::getLong, Row::getLong);
    public static final ValueReader<Instant, Long> INSTANT_FROM_SECONDS = ValueReader.create(Instant::ofEpochSecond, Row::getLong, Row::getLong);
    public static final ValueReader<Instant, Timestamp> INSTANT_FROM_TIMESTAMP = ValueReader.create(Timestamp::toInstant, Row::getTimestamp, Row::getTimestamp);
    public static final ValueReader<LocalDateTime, Timestamp> LOCAL_DATE_TIME = ValueReader.create(Timestamp::toLocalDateTime, Row::getTimestamp, Row::getTimestamp);
    public static final ValueReader<OffsetDateTime, Timestamp> OFFSET_DATE_TIME = ValueReader.create(t -> OffsetDateTime.ofInstant(t.toInstant(), ZoneId.systemDefault()), Row::getTimestamp, Row::getTimestamp);
    public static final ValueReader<ZonedDateTime, Timestamp> ZONED_DATE_TIME = ValueReader.create(t -> t.toInstant().atZone(ZoneId.systemDefault()), Row::getTimestamp, Row::getTimestamp);
    public static final ValueReader<OffsetTime, Time> OFFSET_TIME = ValueReader.create(t -> t.toLocalTime().atOffset(OffsetTime.now().getOffset()), Row::getTime, Row::getTime);
    public static final ValueReader<LocalDate, Date> LOCAL_DATE = ValueReader.create(Date::toLocalDate, Row::getDate, Row::getDate);
    public static final ValueReader<LocalTime, Time> LOCAL_TIME = ValueReader.create(Time::toLocalTime, Row::getTime, Row::getTime);
    public static final ValueReader<UUID, String> UUID_FROM_STRING = ValueReader.create(UUID::fromString, Row::getString, Row::getString);
    public static final ValueReader<UUID, byte[]> UUID_FROM_BYTES = ValueReader.create(UUIDConverter::convert, Row::getBytes, Row::getBytes);

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ValueReader<LocalDate, Date> localDate(Calendar calendar) {
        return ValueReader.create(Date::toLocalDate, (row, name) -> row.getDate(name, calendar), (row, integer) -> row.getDate(integer, calendar));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ValueReader<LocalTime, Time> localTime(Calendar calendar) {
        return ValueReader.create(Time::toLocalTime, (row, name) -> row.getTime(name, calendar), (row, integer) -> row.getTime(integer, calendar));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ValueReader<OffsetTime, Time> offsetTime(Calendar calendar) {
        return ValueReader.create(t -> t.toLocalTime().atOffset(OffsetTime.now().getOffset()), (row, name) -> row.getTime(name, calendar), (row, integer) -> row.getTime(integer, calendar));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ValueReader<LocalDateTime, Timestamp> localDateTime(Calendar calendar) {
        return ValueReader.create(Timestamp::toLocalDateTime, (row, name) -> row.getTimestamp(name, calendar), (row, integer) -> row.getTimestamp(integer, calendar));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ValueReader<OffsetDateTime, Timestamp> offsetDateTime(Calendar calendar) {
        return ValueReader.create(t -> OffsetDateTime.ofInstant(t.toInstant(), ZoneId.systemDefault()), (row, name) -> row.getTimestamp(name, calendar), (row, integer) -> row.getTimestamp(integer, calendar));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ValueReader<ZonedDateTime, Timestamp> zonedDateTime(Calendar calendar) {
        return ValueReader.create(t -> t.toInstant().atZone(ZoneId.systemDefault()), (row, name) -> row.getTimestamp(name, calendar), (row, integer) -> row.getTimestamp(integer, calendar));
    }

    @Contract(value = "_ -> new", pure = true)
    public static <T extends Enum<T>> @NotNull ValueReader<T, String> forEnum(Class<T> clazz) {
        return ValueReader.create(v -> Enum.valueOf(clazz, v), Row::getString, Row::getString);
    }
}
