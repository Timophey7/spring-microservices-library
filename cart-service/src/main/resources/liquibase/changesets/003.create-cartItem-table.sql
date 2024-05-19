--liquibase formatted sql

-- changeset liquibase/changesets/003.create-cartItmes-table.sql::create-cart-items-table::includeAll

create table cart_items (
                            product_id integer not null,
                            cart_id bigint not null,
                            id bigint not null auto_increment,
                            primary key (id)
) engine=InnoDB;

-- changeset liquibase/changesets/003.create-cartItmes-table.sql::add-fk-cart-id::includeAll

alter table cart_items add constraint FKpcttvuq4mxppo8sxggjtn5i2c foreign key (cart_id) references carts (id);

-- changeset liquibase/changesets/003.create-cartItmes-table.sql::add-fk-product-id::includeAll

alter table cart_items add constraint FK1re40cjegsfvw58xrkdp6bac6 foreign key (product_id) references products (product_id);



