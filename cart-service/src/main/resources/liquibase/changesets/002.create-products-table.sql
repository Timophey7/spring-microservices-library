--liquibase formatted sql

create table products (
    price integer not null,
    product_id integer not null,
    title varchar(255),
    primary key (product_id)
) engine=InnoDB
