--liquibase formatted sql

create table carts (
    date_of_create datetime(6),
    id bigint not null auto_increment,
    primary key (id)
) engine=InnoDB