create table `order-info` (
    cart_id integer,
    order_id integer not null auto_increment,
    result_sum integer not null, date_of_order datetime(6),
    number_of_order varchar(255) not null,
    primary key (order_id)
) engine=InnoDB