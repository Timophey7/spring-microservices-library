--liquibase formatted sql

create table app_users (
    id integer not null,
    email varchar(255),
    firstname varchar(255),
    lastname varchar(255),
    password varchar(255),
    role enum ('USER','ADMIN'),
    primary key (id)
) engine=InnoDB