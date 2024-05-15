--liquibase formatted sql

create table books (
    id serial not null,
    price integer not null,
    author varchar(255),
    description varchar(255),
    isbn varchar(255),
    photo_url varchar(255),
    published_date varchar(255),
    publisher varchar(255),
    title varchar(255),
    primary key (id)
)