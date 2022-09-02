-- Rename old, date  filled tables
alter table bar
    rename to bar_o;
alter table bar_categories
    rename to bar_categories_o;
alter table bar_people
    rename to bar_people_o;
alter table bar_products
    rename to bar_products_o;
alter table bar_sessions
    rename to bar_sessions_o;
alter table bill
    rename to bill_o;
alter table "bill_orders"
    rename to "bill_orders_o";
alter table category
    rename to category_o;
alter table "order"
    rename to "order_o";
alter table person
    rename to person_o;
alter table product
    rename to product_o;
alter table session
    rename to session_o;
alter table stock
    rename to stock_o;
alter table "user"
    rename to "user_o";
alter table user_bar_authorization
    rename to user_bar_authorization_o;
alter table "user_user_bar_authorizations"
    rename to "user_user_bar_authorizations_o";

-- Update deleted column types to boolean (if database was imported using method that conserves column type)
alter table bar_o
    alter column deleted type bool
        using deleted::boolean;
alter table bill_o
    alter column is_payed type bool
        using is_payed::boolean;
alter table category_o
    alter column deleted type bool
        using deleted::boolean;
alter table product_o
    alter column deleted type bool
        using deleted::boolean;
alter table order_o
    alter column creation_date type timestamp
        using creation_date::timestamp;
alter table product_o
    alter column is_favorite type bool
        using is_favorite::boolean;
alter table session_o
    alter column creation_date type timestamp
        using creation_date::timestamp,
    alter column closed_date type timestamp
        using closed_date::timestamp,
    alter column locked type bool
        using locked::boolean;


-- Create new tables
create table "authorization"
(
    id        text not null,
    bar_id    text,
    role      varchar(255),
    person_id text
);

create table bar
(
    id           text not null,
    deleted      BOOLEAN default false,
    address      varchar(255),
    mail         varchar(255),
    name         varchar(255),
    phone_number varchar(255)
);

create table bar_categories
(
    bar_id        text not null,
    categories_id text not null
);

create table bar_people
(
    bar_id    text not null,
    people_id text not null
);

create table bar_products
(
    bar_id      text not null,
    products_id text not null
);

create table bar_sessions
(
    bar_id      text not null,
    sessions_id text not null
);

create table bill
(
    id          text not null,
    deleted     BOOLEAN default false,
    is_payed    boolean,
    customer_id text,
    session_id  text
);

create table bill_history
(
    bill_id    text not null,
    history_id text not null
);

create table "bill_orders"
(
    bill_id     text not null,
    "orders_id" text not null
);

create table category
(
    id      text not null,
    deleted boolean,
    name    varchar(255)
);

create table "order"
(
    id              text not null,
    amount          int4,
    creation_date   timestamp,
    brand           varchar(255),
    product_id      text,
    name            varchar(255),
    money_amount    numeric(19, 2),
    currency_code   varchar(255),
    currency_symbol varchar(255),
    bartender_id    text
);

create table order_history_entry
(
    id                 text not null,
    amount             int4,
    order_date         timestamp,
    product_id         text,
    product_name       varchar(255),
    order_history_type varchar(255),
    bartender_id       text,
    customer_id        text
);

create table person
(
    id        text not null,
    name      varchar(255),
    "user_id" text
);

create table price
(
    id              text not null,
    from_date       timestamp,
    money_amount    numeric(19, 2),
    currency_code   varchar(255),
    currency_symbol varchar(255),
    to_date         timestamp
);

create table product
(
    id          text not null,
    brand       varchar(255),
    deleted     boolean,
    is_favorite boolean,
    name        varchar(255),
    size        float8,
    type        varchar(255),
    category_id text
);

create table product_price
(
    product_id text not null,
    price_id   text not null
);

create table session
(
    id            text    not null,
    creation_date timestamp,
    deleted       BOOLEAN default false,
    end_date      timestamp,
    locked        boolean not null,
    name          varchar(255)
);

create table "user"
(
    id           text not null,
    first_name   varchar(255),
    last_name    varchar(255),
    mail         varchar(255),
    password     varchar(255),
    phone_number varchar(255),
    username     varchar(255)
);

create table "user_authorizations"
(
    "user_id"           text not null,
    "authorizations_id" text not null
);

-- Move old data to new unconstrained tables
insert into "authorization"(id, bar_id, role, person_id)
select id, bar_id, role, user_id
from user_bar_authorization_o;

insert into bar(id, deleted, address, mail, name, phone_number)
select id, deleted, address, mail, name, phone_number
from bar_o;

insert into bar_categories(bar_id, categories_id)
select bar_id, categories_id
from bar_categories_o;

insert into bar_people(bar_id, people_id)
select bar_id, people_id
from bar_people_o;

insert into bar_products(bar_id, products_id)
select bar_id, products_id
from bar_products_o;

insert into bar_sessions(bar_id, sessions_id)
select bar_id, sessions_id
from bar_sessions_o;

insert into bill(id, is_payed, customer_id, session_id, deleted)
select id, is_payed, customer_id, session_id, false
from bill_o;

insert into bill_history(bill_id, history_id)
select distinct on (b.id) b.id, o.id
from order_o as o
         inner join bill_orders_o as bo
                    on bo.orders_id = o.id
         inner join bill_o as b
                    on b.id = bo.bill_id;

insert into bill_orders(bill_id, orders_id)
select bill_id, orders_id
from bill_orders_o;

insert into category(id, deleted, name)
select id, deleted, name
from category_o;

insert into "order"(id, amount, creation_date, bartender_id, product_id,
                    brand, name, money_amount, currency_code, currency_symbol)
select o.id,
       o.amount,
       o.creation_date,
       o.bartender_id,
       o.product_id,
       p.brand,
       p.name,
       p.price,
       'EUR',
       '€'
from order_o o,
     product_o p
where o.product_id = p.id;

insert into order_history_entry(id, amount, order_date, product_id, product_name,
                                order_history_type, bartender_id, customer_id)
select o.id,
       o.amount,
       o.creation_date,
       o.product_id,
       p.brand || ' ' || p.name,
       'ADD',
       o.bartender_id,
       b.customer_id
from order_o as o
         inner join bill_orders_o as bo
                    on bo.orders_id = o.id
         inner join bill_o as b
                    on b.id = bo.bill_id
         inner join product_o as p
                    on p.id = o.product_id;

insert into person(id, name, user_id)
select id, name, user_id
from person_o;

insert into price(from_date, money_amount, currency_code, currency_symbol, to_date, id)
select to_timestamp(
               '2021-01-01 12:00:00',
               'YYYY-MM-DD HH:MI:SS'
           ),
       p.price,
       'EUR',
       '€',
       null,
       p.id
from product_o as p;

insert into product(id, brand, deleted, is_favorite, name, size, category_id, type)
select p.id,
       p.brand,
       p.deleted,
       p.is_favorite,
       p.name,
       p.size,
       p.category_id,
       c.product_type
from product_o as p,
     category_o as c
where p.category_id = c.id;

insert into product_price(product_id, price_id)
select id, id
from product;

insert into session(id, creation_date, deleted, end_date, locked, name)
select id, creation_date, false, closed_date, locked, name
from session_o;

insert into "user"(id, first_name, last_name, mail, password, username, phone_number)
select distinct on (u.id) u.id, u.first_name, u.last_name, u.mail, u.password, u.username, p.phone_number
from user_o as u
         left join person_o as p
                   on u.id = p.user_id;

insert into user_authorizations(user_id, authorizations_id)
select user_id, user_bar_authorizations_id
from user_user_bar_authorizations_o;

-- Update role enum values
update "authorization"
set role = 'OWNER'
where role = 'ROLE_BAR_OWNER';

update "authorization"
set role = 'BARTENDER'
where role = 'ROLE_BARTENDER';

update "authorization"
set role = 'CUSTOMER'
where role = 'ROLE_CUSTOMER';

-- Add all Primary Key constraints
alter table "authorization"
    add constraint authorization_pk primary key (id);
alter table bar
    add constraint bar_pk primary key (id);
alter table bill
    add constraint bill_pk primary key (id);
alter table category
    add constraint category_pk primary key (id);
alter table "order"
    add constraint order_pk primary key (id);
alter table order_history_entry
    add constraint order_history_entry_pk primary key (id);
alter table person
    add constraint person_pk primary key (id);
alter table price
    add constraint price_pk primary key (id);
alter table product
    add constraint product_pk primary key (id);
alter table session
    add constraint session_pk primary key (id);
alter table "user"
    add constraint user_pk primary key (id);

-- Clear out incorrect and test bar authorizations and the couple table values

delete
from "authorization" as a
where not exists(select null
                 from bar b
                 where b.id = a.bar_id);

delete
from "authorization" as a
where not exists(select null
                 from person p
                 where p.id = a.person_id);

delete
from user_authorizations as ua
where not exists(select null
                 from "authorization" a
                 where a.id = ua.authorizations_id);

-- Add Foreign Key update cascading constraints
alter table "authorization"
    add constraint bar_id_fk_cascade
        foreign key (bar_id)
            references bar
            on update cascade,
    add constraint person_id_fk_cascade
        foreign key (person_id)
            references person
            on update cascade;

alter table bar_categories
    add constraint bar_id_fk_cascade
        foreign key (bar_id)
            references bar
            on update cascade,
    add constraint categories_id_fk_cascade
        foreign key (categories_id)
            references category
            on update cascade;

alter table bar_people
    add constraint bar_id_fk_cascade
        foreign key (bar_id)
            references bar
            on update cascade,
    add constraint people_id_fk_cascade
        foreign key (people_id)
            references person
            on update cascade;

alter table bar_products
    add constraint bar_id_fk_cascade
        foreign key (bar_id)
            references bar
            on update cascade,
    add constraint products_id_fk_cascade
        foreign key (products_id)
            references product
            on update cascade;

alter table bar_sessions
    add constraint bar_id_fk_cascade
        foreign key (bar_id)
            references bar
            on update cascade,
    add constraint sessions_id_fk_cascade
        foreign key (sessions_id)
            references session
            on update cascade;

alter table bill
    add constraint customer_id_fk_cascade
        foreign key (customer_id)
            references person
            on update cascade,
    add constraint session_id_fk_cascade
        foreign key (session_id)
            references session
            on update cascade;

alter table bill_history
    add constraint bill_id_fk_cascade
        foreign key (bill_id)
            references bill
            on update cascade,
    add constraint history_id_fk_cascade
        foreign key (history_id)
            references order_history_entry
            on update cascade;

alter table "bill_orders"
    add constraint bill_id_fk_cascade
        foreign key (bill_id)
            references bill
            on update cascade,
    add constraint orders_id_fk_cascade
        foreign key (orders_id)
            references "order"
            on update cascade;

alter table "order"
    add constraint product_id_fk_cascade
        foreign key (product_id)
            references product
            on update cascade,
    add constraint bartender_id_fk_cascade
        foreign key (bartender_id)
            references person
            on update cascade;

alter table order_history_entry
    add constraint product_id_fk_cascade
        foreign key (product_id)
            references product
            on update cascade,
    add constraint bartender_id_fk_cascade
        foreign key (bartender_id)
            references person
            on update cascade,
    add constraint customer_id_fk_cascade
        foreign key (customer_id)
            references person
            on update cascade;

alter table person
    add constraint user_id_fk_cascade
        foreign key (user_id)
            references "user"
            on update cascade;

alter table product
    add constraint category_id_fk_cascade
        foreign key (category_id)
            references category
            on update cascade;

alter table product_price
    add constraint product_id_fk_cascade
        foreign key (product_id)
            references product
            on update cascade,
    add constraint price_id_fk_cascade
        foreign key (price_id)
            references price
            on update cascade;

alter table user_authorizations
    add constraint user_id_fk_cascade
        foreign key (user_id)
            references "user"
            on update cascade,
    add constraint authorization_id_fk_cascade
        foreign key (authorizations_id)
            references "authorization"
            on update cascade;

-- Change id's to UUID's + bill_history/order_history_id/price/product_price should update too
update "authorization"
set id = gen_random_uuid();

update bar
set id = gen_random_uuid();

update bill
set id = gen_random_uuid();

update category
set id = gen_random_uuid();

update "order"
set id = gen_random_uuid();

update order_history_entry
set id = gen_random_uuid();

update person
set id = gen_random_uuid();

update price
set id = gen_random_uuid();

update product
set id = gen_random_uuid();

update session
set id = gen_random_uuid();

update "user"
set id = gen_random_uuid();

-- Remove Foreign key constraints
alter table "authorization"
    drop constraint bar_id_fk_cascade,
    drop constraint person_id_fk_cascade;

alter table bar_categories
    drop constraint bar_id_fk_cascade,
    drop constraint categories_id_fk_cascade;

alter table bar_people
    drop constraint bar_id_fk_cascade,
    drop constraint people_id_fk_cascade;

alter table bar_products
    drop constraint bar_id_fk_cascade,
    drop constraint products_id_fk_cascade;

alter table bar_sessions
    drop constraint bar_id_fk_cascade,
    drop constraint sessions_id_fk_cascade;

alter table bill
    drop constraint customer_id_fk_cascade,
    drop constraint session_id_fk_cascade;

alter table bill_history
    drop constraint bill_id_fk_cascade,
    drop constraint history_id_fk_cascade;

alter table "bill_orders"
    drop constraint bill_id_fk_cascade,
    drop constraint orders_id_fk_cascade;

alter table "order"
    drop constraint product_id_fk_cascade,
    drop constraint bartender_id_fk_cascade;

alter table order_history_entry
    drop constraint product_id_fk_cascade,
    drop constraint bartender_id_fk_cascade,
    drop constraint customer_id_fk_cascade;

alter table person
    drop constraint user_id_fk_cascade;

alter table product
    drop constraint category_id_fk_cascade;

alter table product_price
    drop constraint product_id_fk_cascade,
    drop constraint price_id_fk_cascade;

alter table user_authorizations
    drop constraint user_id_fk_cascade,
    drop constraint authorization_id_fk_cascade;

-- Change id field types from text to UUID
alter table "authorization"
    alter column id type uuid using id::uuid,
    alter column bar_id type uuid using bar_id::uuid,
    alter column person_id type uuid using person_id::uuid;

alter table bar
    alter column id type uuid using id::uuid;

alter table bar_categories
    alter column bar_id type uuid using bar_id::uuid,
    alter column categories_id type uuid using categories_id::uuid;

alter table bar_people
    alter column bar_id type uuid using bar_id::uuid,
    alter column people_id type uuid using people_id::uuid;

alter table bar_products
    alter column bar_id type uuid using bar_id::uuid,
    alter column products_id type uuid using products_id::uuid;

alter table bar_sessions
    alter column bar_id type uuid using bar_id::uuid,
    alter column sessions_id type uuid using sessions_id::uuid;

alter table bill
    alter column id type uuid using id::uuid,
    alter column customer_id type uuid using customer_id::uuid,
    alter column session_id type uuid using session_id::uuid;

alter table bill_history
    alter column bill_id type uuid using bill_id::uuid,
    alter column history_id type uuid using history_id::uuid;

alter table bill_orders
    alter column bill_id type uuid using bill_id::uuid,
    alter column orders_id type uuid using orders_id::uuid;

alter table category
    alter column id type uuid using id::uuid;

alter table "order"
    alter column id type uuid using id::uuid,
    alter column product_id type uuid using product_id::uuid,
    alter column bartender_id type uuid using bartender_id::uuid;

alter table order_history_entry
    alter column id type uuid using id::uuid,
    alter column product_id type uuid using product_id::uuid,
    alter column bartender_id type uuid using bartender_id::uuid,
    alter column customer_id type uuid using customer_id::uuid;

alter table person
    alter column id type uuid using id::uuid,
    alter column user_id type uuid using user_id::uuid;

alter table price
    alter column id type uuid using id::uuid;

alter table product
    alter column id type uuid using id::uuid,
    alter column category_id type uuid using category_id::uuid;

alter table product_price
    alter column product_id type uuid using product_id::uuid,
    alter column price_id type uuid using price_id::uuid;

alter table session
    alter column id type uuid using id::uuid;

alter table "user"
    alter column id type uuid using id::uuid;

alter table user_authorizations
    alter column user_id type uuid using user_id::uuid,
    alter column authorizations_id type uuid using authorizations_id::uuid;


-- Add Foreign Key constraints (without cascading)
alter table "authorization"
    add constraint bar_id_fk_cascade
        foreign key (bar_id)
            references bar,
    add constraint person_id_fk_cascade
        foreign key (person_id)
            references person;

alter table bar_categories
    add constraint bar_id_fk_cascade
        foreign key (bar_id)
            references bar,
    add constraint categories_id_fk_cascade
        foreign key (categories_id)
            references category;

alter table bar_people
    add constraint bar_id_fk_cascade
        foreign key (bar_id)
            references bar,
    add constraint people_id_fk_cascade
        foreign key (people_id)
            references person;

alter table bar_products
    add constraint bar_id_fk_cascade
        foreign key (bar_id)
            references bar,
    add constraint products_id_fk_cascade
        foreign key (products_id)
            references product;

alter table bar_sessions
    add constraint bar_id_fk_cascade
        foreign key (bar_id)
            references bar,
    add constraint sessions_id_fk_cascade
        foreign key (sessions_id)
            references session;

alter table bill
    add constraint customer_id_fk_cascade
        foreign key (customer_id)
            references person,
    add constraint session_id_fk_cascade
        foreign key (session_id)
            references session;

alter table bill_history
    add constraint bill_id_fk_cascade
        foreign key (bill_id)
            references bill,
    add constraint history_id_fk_cascade
        foreign key (history_id)
            references order_history_entry;

alter table "bill_orders"
    add constraint bill_id_fk_cascade
        foreign key (bill_id)
            references bill,
    add constraint orders_id_fk_cascade
        foreign key (orders_id)
            references "order";

alter table "order"
    add constraint product_id_fk_cascade
        foreign key (product_id)
            references product,
    add constraint bartender_id_fk_cascade
        foreign key (bartender_id)
            references person;

alter table order_history_entry
    add constraint product_id_fk_cascade
        foreign key (product_id)
            references product,
    add constraint bartender_id_fk_cascade
        foreign key (bartender_id)
            references person,
    add constraint customer_id_fk_cascade
        foreign key (customer_id)
            references person;

alter table person
    add constraint user_id_fk_cascade
        foreign key (user_id)
            references "user";

alter table product
    add constraint category_id_fk_cascade
        foreign key (category_id)
            references category;

alter table product_price
    add constraint product_id_fk_cascade
        foreign key (product_id)
            references product,
    add constraint price_id_fk_cascade
        foreign key (price_id)
            references price;

alter table user_authorizations
    add constraint user_id_fk_cascade
        foreign key (user_id)
            references "user",
    add constraint authorization_id_fk_cascade
        foreign key (authorizations_id)
            references "authorization";

-- Add other unique constraints
alter table if exists bar_categories
    add constraint UK_hlkhodyyl4d9x1sgs76p49a1g unique (categories_id);

alter table if exists bar_people
    add constraint UK_645i581qjajxmhayi7x7vcl1q unique (people_id);

alter table if exists bar_products
    add constraint UK_ijgjfo3wjhd4uyfhryhm8byr6 unique (products_id);

alter table if exists bar_sessions
    add constraint UK_a8iu547r18g8iregmbx7rj1aa unique (sessions_id);

alter table if exists bill_history
    add constraint UK_jww508bb9k0ng8jacgrp3c3rj unique (history_id);

alter table if exists "bill_orders"
    add constraint UK_eq6noll9kl3m9xelx1hho1rr6 unique ("orders_id");

alter table if exists product_price
    add constraint UK_1wkhsq2dogphcbnm6hl6dhqq8 unique (price_id);

alter table if exists "user"
    add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);


-- Remove old tables
drop table bar_o;
drop table bar_categories_o;
drop table bar_people_o;
drop table bar_products_o;
drop table bar_sessions_o;
drop table bill_o;
drop table bill_orders_o;
drop table category_o;
drop table order_o;
drop table person_o;
drop table product_o;
drop table session_o;
drop table stock_o;
drop table user_o;
drop table "user_bar_authorization_o";
drop table "user_user_bar_authorizations_o";
