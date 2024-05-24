package de.chojo.sadu.queries.converter;

import de.chojo.sadu.mapper.reader.StandardReader;
import de.chojo.sadu.queries.api.call.adapter.Adapter;
import de.chojo.sadu.queries.call.adapter.StandardAdapter;
import de.chojo.sadu.queries.call.adapter.UUIDAdapter;

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

public final class StandardValueConverter {
    public static final ValueConverter<Instant, Long> INSTANT_MILLIS = create(StandardAdapter.INSTANT_AS_MILLIS, StandardReader.INSTANT_FROM_MILLIS);
    public static final ValueConverter<Instant, Long> INSTANT_SECONDS = create(StandardAdapter.INSTANT_AS_SECONDS, StandardReader.INSTANT_FROM_SECONDS);
    public static final ValueConverter<Instant, Timestamp> INSTANT_TIMESTAMP = create(StandardAdapter.INSTANT_AS_TIMESTAMP, StandardReader.INSTANT_FROM_TIMESTAMP);
    public static final ValueConverter<LocalDateTime, Timestamp> LOCAL_DATE_TIME = create(StandardAdapter.LOCAL_DATE_TIME, StandardReader.LOCAL_DATE_TIME);
    public static final ValueConverter<OffsetDateTime, Timestamp> OFFSET_DATE_TIME = create(StandardAdapter.OFFSET_DATE_TIME, StandardReader.OFFSET_DATE_TIME);
    public static final ValueConverter<ZonedDateTime, Timestamp> ZONED_DATE_TIME = create(StandardAdapter.ZONED_DATE_TIME, StandardReader.ZONED_DATE_TIME);
    public static final ValueConverter<OffsetTime, Time> OFFSET_TIME = create(StandardAdapter.OFFSET_TIME, StandardReader.OFFSET_TIME);
    public static final ValueConverter<LocalDate, Date> LOCAL_DATE = create(StandardAdapter.LOCAL_DATE, StandardReader.LOCAL_DATE);
    public static final ValueConverter<LocalTime, Time> LOCAL_TIME = create(StandardAdapter.LOCAL_TIME, StandardReader.LOCAL_TIME);
    public static final ValueConverter<UUID, String> UUID_STRING = create(UUIDAdapter.AS_STRING, StandardReader.UUID_FROM_STRING);
    public static final ValueConverter<UUID, byte[]> UUID_BYTES = create(UUIDAdapter.AS_BYTES, StandardReader.UUID_FROM_BYTES);

    public static <T extends Enum<T>> ValueConverter<T, String> forEnum(Class<T> clazz) {
        // Normally an explicit type isn't required to write an enum, but in this case we need it.
        return create(Adapter.create(PreparedStatement::setString, Types.VARCHAR, Enum::name), StandardReader.forEnum(clazz));
    }

    public static ValueConverter<LocalDate, Date> localDate(Calendar calendar) {
        return create(StandardAdapter.localDate(calendar), StandardReader.localDate(calendar));
    }

    public static ValueConverter<LocalTime, Time> localTime(Calendar calendar) {
        return create(StandardAdapter.localTime(calendar), StandardReader.localTime(calendar));
    }

    public static ValueConverter<OffsetTime, Time> offsetTime(Calendar calendar) {
        return create(StandardAdapter.offsetTime(calendar), StandardReader.offsetTime(calendar));
    }

    public static ValueConverter<LocalDateTime, Timestamp> localDateTime(Calendar calendar) {
        return create(StandardAdapter.localDateTime(calendar), StandardReader.localDateTime(calendar));
    }

    public static ValueConverter<OffsetDateTime, Timestamp> offsetDateTime(Calendar calendar) {
        return create(StandardAdapter.offsetDateTime(calendar), StandardReader.offsetDateTime(calendar));
    }

    public static ValueConverter<ZonedDateTime, Timestamp> zonedDateTime(Calendar calendar) {
        return create(StandardAdapter.zonedDateTime(calendar), StandardReader.zonedDateTime(calendar));
    }
}
