--liquibase formatted sql

--changeset karina:1

create table if not exists data_table (
    id serial primary key,
    time timestamp,
    speed int,
    registration_plate varchar(255)
);