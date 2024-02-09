create table users
(
    id   serial
        constraint users_pk
            primary key,
    uuid uuid not null,
    name text
);

create table birthdays
(
    user_id    integer not null
        constraint birthdays_pk
            primary key,
    birth_date date    not null
);
