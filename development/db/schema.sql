
    create table "user"
(
    id       bigint not null
        constraint user_pkey
            primary key,
    password varchar(255),
    role     varchar(255),
    username varchar(255)
);



    create table user_bar_authorization
(
    id      bigint not null
        constraint user_bar_authorization_pkey
            primary key,
    bar_id  bigint,
    role    varchar(255),
    user_id bigint
);

create table bar
(
    id           bigint not null
        constraint bar_pkey
            primary key,
    address      varchar(255),
    mail         varchar(255),
    name         varchar(255),
    phone_number varchar(255)
);

create table category
(
    id           bigint not null
        constraint category_pkey
            primary key,
    name         varchar(255),
    product_type varchar(255)
);



create table bar_categories
(
    bar_id        bigint not null
        constraint fkb6wvu7vgbvvyjn2ycvy2iyjc7
            references bar,
    categories_id bigint not null
        constraint uk_hlkhodyyl4d9x1sgs76p49a1g
            unique
        constraint fkflspv66n9geafj6wyobuvjn3n
            references category
);



create table person
(
    id      bigint not null
        constraint person_pkey
            primary key,
    name    varchar(255),
    user_id bigint
        constraint fkdunok34g8d08mu7mtocmhfptn
            references "user"
);

create table bartender
(
    user_id bigint not null
        constraint bartender_pkey
            primary key
        constraint fkrriold829ilpiungxpau2iomq
            references person
);


create table customer
(
    phone_number varchar(255),
    user_id      bigint not null
        constraint customer_pkey
            primary key
        constraint fk3hnutqe63n4hp8qda9vp5yxd7
            references person
);

create table bar_people
(
    bar_id    bigint not null
        constraint fktitff4pi26msucas9die8d4w9
            references bar,
    people_id bigint not null
        constraint uk_645i581qjajxmhayi7x7vcl1q
            unique
        constraint fk3kykcllirre3g54vg13p74a6e
            references person
);
create table session
(
    id            bigint  not null
        constraint session_pkey
            primary key,
    closed_date   timestamp,
    creation_date timestamp,
    locked        boolean not null,
    name          varchar(255)
);


    create table product
(
    id           bigint not null
        constraint product_pkey
            primary key,
    brand        varchar(255),
    is_favorite  boolean,
    name         varchar(255),
    price        double precision,
    product_type varchar(255),
    size         double precision,
    category_id  bigint
        constraint fk1mtsbur82frn64de7balymq9s
            references category
);


    create table bar_products
(
    bar_id      bigint not null
        constraint fkqj2f4i2vn2nkvyw5813280not
            references bar,
    products_id bigint not null
        constraint uk_ijgjfo3wjhd4uyfhryhm8byr6
            unique
        constraint fk4jnkvxrq1beu5ub8rrnjfeipe
            references product
);


    create table session_bartender
(
    user_id bigint not null
        constraint fkdwtnwd7rj67yrria9seq3n9km
            references bartender,
    id      bigint not null
        constraint fks9hrjajhvlbe354djdr4nus4a
            references session
);

create table bar_sessions
(
    bar_id      bigint not null
        constraint fkrsat47b1vob5rv5g7onn93c0b
            references bar,
    sessions_id bigint not null
        constraint uk_a8iu547r18g8iregmbx7rj1aa
            unique
        constraint fkldkxmr8ohrf4g8irv56lo483i
            references session
);

    create table bill
(
    id          bigint not null
        constraint bill_pkey
            primary key,
    is_payed    boolean,
    customer_id bigint
        constraint fkcdveik90g4pvk7m249scu73pg
            references customer,
    session_id  bigint not null
        constraint fknpdke1pihy8cr0lddxntf4hl2
            references session
);

create table "order"
(
    id                bigint not null
        constraint order_pkey
            primary key,
    amount            integer,
    creation_date     timestamp,
    bartender_user_id bigint
        constraint fk65ngp9nfr0jp3nrta80k7g3vu
            references bartender,
    product_id        bigint
        constraint fkknjaoi59nxmpxhr452bj95tgg
            references product
);

create table bill_orders
(
    bill_id   bigint not null
        constraint fk5cux4tsugmy29yb7mutwceibl
            references bill,
    orders_id bigint not null
        constraint uk_eq6noll9kl3m9xelx1hho1rr6
            unique
        constraint fk1b0lpoex8ikeh2vm2k5b6022
            references "order"
);


create table user_user_bar_authorizations
(
    user_id                    bigint not null
        constraint fkbk2srn0hyaaxfnruc5ll5qwdo
            references "user",
    user_bar_authorizations_id bigint not null
        constraint uk_7orid7iw1ljt2renofrt7uqnk
            unique
        constraint fkjbfxkryjvkflmaph6jg9yoouk
            references user_bar_authorization
);
--
-- alter table bill_orders
--     owner to "bar-api-user";
--
-- alter table "order"
--     owner to "bar-api-user";
--
-- alter table bill
--     owner to "bar-api-user";
--
-- alter table session
--     owner to "bar-api-user";
--
-- alter table bar_sessions
--     owner to "bar-api-user";
--
-- alter table session_bartender
--     owner to "bar-api-user";
--
-- alter table bar_products
--     owner to "bar-api-user";
--
-- alter table bar_people
--     owner to "bar-api-user";
--
-- alter table customer
--     owner to "bar-api-user";
--
-- alter table bartender
--     owner to "bar-api-user";
--
-- alter table person
--     owner to "bar-api-user";
--
-- alter table bar_categories
--     owner to "bar-api-user";
--
-- alter table category
--     owner to "bar-api-user";
--
-- alter table "user"
--     owner to "bar-api-user";
--
-- alter table bar
--     owner to "bar-api-user";
--
-- alter table user_bar_authorization
--         owner to "bar-api-user";
--
-- alter table user_user_bar_authorizations
--     owner to "bar-api-user";
