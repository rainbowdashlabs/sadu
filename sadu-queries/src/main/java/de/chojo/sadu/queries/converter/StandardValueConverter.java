/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.converter;

import de.chojo.sadu.mapper.reader.StandardReader;
import de.chojo.sadu.queries.api.call.adapter.Adapter;
import de.chojo.sadu.queries.call.adapter.StandardAdapter;
import de.chojo.sadu.queries.call.adapter.UUIDAdapter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.UUID;

import static de.chojo.sadu.queries.converter.ValueConverter.create;

/**
 * The StandardValueConverter class provides a set of predefined ValueConverters for various types.
 * Each ValueConverter maps between a Java type and an intermediate SQL type.
 *
 */
public final class StandardValueConverter {
    /**
     * Writes and reads {@link Instant} as unix epoch in milliseconds
     */
    public static final ValueConverter<Instant, Long> INSTANT_MILLIS = create(StandardAdapter.INSTANT_AS_MILLIS, StandardReader.INSTANT_FROM_MILLIS);
    /**
     * Writes and reads {@link Instant} as unix epoch in seconds
     */
    public static final ValueConverter<Instant, Long> INSTANT_SECONDS = create(StandardAdapter.INSTANT_AS_SECONDS, StandardReader.INSTANT_FROM_SECONDS);
    /**
     * Writes and reads {@link Instant} as {@link Timestamp}
     */
    public static final ValueConverter<Instant, Timestamp> INSTANT_TIMESTAMP = create(StandardAdapter.INSTANT_AS_TIMESTAMP, StandardReader.INSTANT_FROM_TIMESTAMP);
    /**
     * Writes and reads {@link LocalDateTime} as {@link Timestamp}
     */
    public static final ValueConverter<LocalDateTime, Timestamp> LOCAL_DATE_TIME = create(StandardAdapter.LOCAL_DATE_TIME, StandardReader.LOCAL_DATE_TIME);
    /**
     * Writes and reads {@link OffsetDateTime} as {@link Timestamp}. Will be stored in UTC.
     */
    public static final ValueConverter<OffsetDateTime, Timestamp> OFFSET_DATE_TIME = create(StandardAdapter.OFFSET_DATE_TIME, StandardReader.OFFSET_DATE_TIME);
    /**
     * Writes and reads {@link ZonedDateTime} as {@link Timestamp}. Will be stored in UTC.
     */
    public static final ValueConverter<ZonedDateTime, Timestamp> ZONED_DATE_TIME = create(StandardAdapter.ZONED_DATE_TIME, StandardReader.ZONED_DATE_TIME);
    /**
     * Writes and reads {@link OffsetTime} as {@link Time}. Will be stored as {@link LocalTime}.
     */
    public static final ValueConverter<OffsetTime, Time> OFFSET_TIME = create(StandardAdapter.OFFSET_TIME, StandardReader.OFFSET_TIME);
    /**
     * Writes and reads {@link LocalDate} as {@link Date}.
     */
    public static final ValueConverter<LocalDate, Date> LOCAL_DATE = create(StandardAdapter.LOCAL_DATE, StandardReader.LOCAL_DATE);
    /**
     * Writes and reads {@link LocalTime} as {@link Time}.
     */
    public static final ValueConverter<LocalTime, Time> LOCAL_TIME = create(StandardAdapter.LOCAL_TIME, StandardReader.LOCAL_TIME);
    /**
     * Writes and reads {@link UUID} as {@link String}.
     */
    public static final ValueConverter<UUID, String> UUID_STRING = create(UUIDAdapter.AS_STRING, StandardReader.UUID_FROM_STRING);
    /**
     * Writes and reads {@link UUID} as {@link Byte}[].
     */
    public static final ValueConverter<UUID, byte[]> UUID_BYTES = create(UUIDAdapter.AS_BYTES, StandardReader.UUID_FROM_BYTES);

    /**
     * Writes and reads {@link Enum} as {@link String}.
     *
     * @param clazz enum class
     * @param <T>   enum type
     * @return A new {@link ValueConverter} instance for that specific enum type
     */
    @Contract("_ -> new")
    public static <T extends Enum<T>> @NotNull ValueConverter<T, String> forEnum(Class<T> clazz) {
        // Normally an explicit type isn't required to write an enum, but in this case we need it.
        return create(Adapter.create(PreparedStatement::setString, Types.VARCHAR, Enum::name), StandardReader.forEnum(clazz));
    }

    /**
     * Writes and reads {@link LocalDate} as {@link Date}.
     *
     * @param calendar The calendar used to construct the date
     * @return A new {@link ValueConverter} instance using the {@link Calendar}
     */
    @Contract("_ -> new")
    public static @NotNull ValueConverter<LocalDate, Date> localDate(Calendar calendar) {
        return create(StandardAdapter.localDate(calendar), StandardReader.localDate(calendar));
    }

    /**
     * Writes and reads {@link LocalTime} as {@link Time}.
     *
     * @param calendar The calendar used to construct the timestamp
     * @return A new {@link ValueConverter} instance using the {@link Calendar}
     */
    @Contract("_ -> new")
    public static @NotNull ValueConverter<LocalTime, Time> localTime(Calendar calendar) {
        return create(StandardAdapter.localTime(calendar), StandardReader.localTime(calendar));
    }

    /**
     * Writes and reads {@link OffsetTime} as {@link Time}. Will be stored as {@link LocalTime}.
     *
     * @param calendar The calendar used to construct the time
     * @return A new {@link ValueConverter} instance using the {@link Calendar}
     */
    @Contract("_ -> new")
    public static @NotNull ValueConverter<OffsetTime, Time> offsetTime(Calendar calendar) {
        return create(StandardAdapter.offsetTime(calendar), StandardReader.offsetTime(calendar));
    }

    /**
     * Writes and reads {@link LocalDateTime} as {@link Timestamp}
     *
     * @param calendar The calendar used to construct the timestamp
     * @return A new {@link ValueConverter} instance using the {@link Calendar}
     */
    @Contract("_ -> new")
    public static @NotNull ValueConverter<LocalDateTime, Timestamp> localDateTime(Calendar calendar) {
        return create(StandardAdapter.localDateTime(calendar), StandardReader.localDateTime(calendar));
    }

    /**
     * Writes and reads {@link ZonedDateTime} as {@link Timestamp}. Will be stored in UTC.
     *
     * @param calendar The calendar used to construct the timestamp
     * @return A new {@link ValueConverter} instance using the {@link Calendar}
     */
    @Contract("_ -> new")
    public static @NotNull ValueConverter<OffsetDateTime, Timestamp> offsetDateTime(Calendar calendar) {
        return create(StandardAdapter.offsetDateTime(calendar), StandardReader.offsetDateTime(calendar));
    }

    /**
     * Writes and reads {@link ZonedDateTime} as {@link Timestamp}. Will be stored in UTC.
     *
     * @param calendar The calendar used to construct the timestamp
     * @return A new {@link ValueConverter} instance using the {@link Calendar}
     */
    @Contract("_ -> new")
    public static @NotNull ValueConverter<ZonedDateTime, Timestamp> zonedDateTime(Calendar calendar) {
        return create(StandardAdapter.zonedDateTime(calendar), StandardReader.zonedDateTime(calendar));
    }
}
