CREATE TABLE users (
    id   SERIAL
        CONSTRAINT users_pk
            PRIMARY KEY,
    uuid UUID NOT NULL,
    name TEXT
);

CREATE TABLE birthdays (
    user_id    INTEGER NOT NULL
        CONSTRAINT birthdays_pk
            PRIMARY KEY,
    birth_date DATE    NOT NULL
);

DROP TABLE IF EXISTS time_test;
CREATE TABLE time_test (
    as_epoch_seconds BIGINT,
    as_epoch_millis  BIGINT,
    as_timestamp     TIMESTAMP WITHOUT TIME ZONE,
    as_timestamp_tz  TIMESTAMP WITH TIME ZONE,
    as_time          TIME,
    as_date          DATE
);
