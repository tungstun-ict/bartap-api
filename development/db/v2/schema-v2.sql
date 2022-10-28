
create table "authorization" (
                                 id uuid not null,
                                 bar_id uuid,
                                 role varchar(255),
                                 person_id uuid,
                                 primary key (id)
);

create table bar (
                     id uuid not null,
                     deleted BOOLEAN default false,
                     address varchar(255),
                     mail varchar(255),
                     name varchar(255),
                     phone_number varchar(255),
                     primary key (id)
);

create table bar_categories (
                                bar_id uuid not null,
                                categories_id uuid not null
);

create table bar_people (
                            bar_id uuid not null,
                            people_id uuid not null
);

create table bar_products (
                              bar_id uuid not null,
                              products_id uuid not null
);

create table bar_sessions (
                              bar_id uuid not null,
                              sessions_id uuid not null
);

create table bill (
                      id uuid not null,
                      deleted BOOLEAN default false,
                      is_payed boolean,
                      customer_id uuid,
                      session_id uuid,
                      primary key (id)
);

create table bill_history (
                              bill_id uuid not null,
                              history_id uuid not null
);

create table "bill_orders" (
                               bill_id uuid not null,
                               "orders_id" uuid not null
);

create table category (
                          id uuid not null,
                          deleted boolean,
                          name varchar(255),
                          primary key (id)
);

create table "order" (
                         id uuid not null,
                         amount int4,
                         creation_date timestamp,
                         brand varchar(255),
                         product_id uuid,
                         name varchar(255),
                         money_amount numeric(19, 2),
                         currency_code varchar(255),
                         currency_symbol varchar(255),
                         bartender_id uuid,
                         primary key (id)
);

create table order_history_entry (
                                     id uuid not null,
                                     amount int4,
                                     order_date timestamp,
                                     product_id uuid,
                                     product_name varchar(255),
                                     order_history_type varchar(255),
                                     bartender_id uuid,
                                     customer_id uuid,
                                     primary key (id)
);

create table person (
                        id uuid not null,
                        name varchar(255),
                        "user_id" uuid,
                        primary key (id)
);

create table price (
                       id uuid not null,
                       from_date timestamp,
                       money_amount numeric(19, 2),
                       currency_code varchar(255),
                       currency_symbol varchar(255),
                       to_date timestamp,
                       primary key (id)
);

create table product (
                         id uuid not null,
                         brand varchar(255),
                         deleted boolean,
                         is_favorite boolean,
                         name varchar(255),
                         size float8,
                         type varchar(255),
                         category_id uuid,
                         primary key (id)
);

create table product_price (
                               product_id uuid not null,
                               price_id uuid not null
);

create table session (
                         id uuid not null,
                         creation_date timestamp,
                         deleted BOOLEAN default false,
                         end_date timestamp,
                         locked boolean not null,
                         name varchar(255),
                         primary key (id)
);

create table "user" (
                        id uuid not null,
                        created_on timestamp,
                        first_name varchar(255),
                        last_name varchar(255),
                        mail varchar(255),
                        password varchar(255),
                        phone_number varchar(255),
                        username varchar(255),
                        primary key (id)
);

create table "user_authorizations" (
                                       "user_id" uuid not null,
                                       "authorizations_id" uuid not null
);

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
    add constraint UK_6sou31qus5dnws6dwfu61e71v unique (mail);

alter table if exists "user"
    add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);

alter table if exists "user_authorizations"
    add constraint UK_psay7qxx06n99o9go05xgk1qe unique ("authorizations_id");

alter table if exists "authorization"
    add constraint FKj87br7wyioy5cqa50mpspyq0r
        foreign key (person_id)
            references person;

alter table if exists bar_categories
    add constraint FKflspv66n9geafj6wyobuvjn3n
        foreign key (categories_id)
            references category;

alter table if exists bar_categories
    add constraint FKb6wvu7vgbvvyjn2ycvy2iyjc7
        foreign key (bar_id)
            references bar;

alter table if exists bar_people
    add constraint FK3kykcllirre3g54vg13p74a6e
        foreign key (people_id)
            references person;

alter table if exists bar_people
    add constraint FKtitff4pi26msucas9die8d4w9
        foreign key (bar_id)
            references bar;

alter table if exists bar_products
    add constraint FK4jnkvxrq1beu5ub8rrnjfeipe
        foreign key (products_id)
            references product;

alter table if exists bar_products
    add constraint FKqj2f4i2vn2nkvyw5813280not
        foreign key (bar_id)
            references bar;

alter table if exists bar_sessions
    add constraint FKldkxmr8ohrf4g8irv56lo483i
        foreign key (sessions_id)
            references session;

alter table if exists bar_sessions
    add constraint FKrsat47b1vob5rv5g7onn93c0b
        foreign key (bar_id)
            references bar;

alter table if exists bill
    add constraint FKgvyl51yuvu113jveh8neokhm7
        foreign key (customer_id)
            references person;

alter table if exists bill
    add constraint FKnpdke1pihy8cr0lddxntf4hl2
        foreign key (session_id)
            references session;

alter table if exists bill_history
    add constraint FK4x35qf54ffgsmb3fl58bn9ej9
        foreign key (history_id)
            references order_history_entry;

alter table if exists bill_history
    add constraint FKmmy76ye6xref3lv9h1ot1fdbk
        foreign key (bill_id)
            references bill;

alter table if exists "bill_orders"
    add constraint FK1b0lpoex8ikeh2vm2k5b6022
        foreign key ("orders_id")
            references "order";

alter table if exists "bill_orders"
    add constraint FK5cux4tsugmy29yb7mutwceibl
        foreign key (bill_id)
            references bill;

alter table if exists "order"
    add constraint FKarejjug8k6j3jm4h7pf24yhs6
        foreign key (bartender_id)
            references person;

alter table if exists order_history_entry
    add constraint FKdk16yr32ecuteew7ghv8hj7ym
        foreign key (bartender_id)
            references person;

alter table if exists order_history_entry
    add constraint FKo62ig3p1nuu6aabygod5ddrdg
        foreign key (customer_id)
            references person;

alter table if exists person
    add constraint FKdunok34g8d08mu7mtocmhfptn
        foreign key ("user_id")
            references "user";

alter table if exists product
    add constraint FK1mtsbur82frn64de7balymq9s
        foreign key (category_id)
            references category;

alter table if exists product_price
    add constraint FKxfgbtbbrfa24m2881bw2k0rv
        foreign key (price_id)
            references price;

alter table if exists product_price
    add constraint FKeupemu63ifqfc4txkskyy1hyi
        foreign key (product_id)
            references product;

alter table if exists "user_authorizations"
    add constraint FK9bc82jn10l4h06y9q5ff1sk3l
        foreign key ("authorizations_id")
            references "authorization";

alter table if exists "user_authorizations"
    add constraint FKri3ggsr4mfi5v7bf2tneu8k6x
        foreign key ("user_id")
            references "user";
