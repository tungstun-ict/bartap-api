create sequence hibernate_sequence start 1000 increment 1;

    create table bar (
       id int8 not null,
        address varchar(255),
        mail varchar(255),
        name varchar(255),
        phone_number varchar(255),
        primary key (id)
    )

    create table bar_categories (
       bar_id int8 not null,
        categories_id int8 not null
    )

    create table bar_people (
       bar_id int8 not null,
        people_id int8 not null
    )

    create table bar_products (
       bar_id int8 not null,
        products_id int8 not null
    )

    create table bar_sessions (
       bar_id int8 not null,
        sessions_id int8 not null
    )

    create table bill (
       id int8 not null,
        is_payed boolean,
        customer_id int8,
        session_id int8 not null,
        primary key (id)
    )

    create table "bill_orders" (
       bill_id int8 not null,
        "orders_id" int8 not null
    )

    create table category (
       id int8 not null,
        name varchar(255),
        product_type varchar(255),
        primary key (id)
    )

    create table "order" (
       id int8 not null,
        amount int4,
        creation_date timestamp,
        bartender_id int8,
        product_id int8,
        primary key (id)
    )

    create table person (
       id int8 not null,
        name varchar(255),
        phone_number varchar(255),
        "user_id" int8,
        primary key (id)
    )

    create table product (
       id int8 not null,
        brand varchar(255),
        is_favorite boolean,
        name varchar(255),
        price float8,
        size float8,
        category_id int8,
        primary key (id)
    )

    create table session (
       id int8 not null,
        closed_date timestamp,
        creation_date timestamp,
        locked boolean not null,
        name varchar(255),
        primary key (id)
    )

    create table "user" (
       id int8 not null,
        first_name varchar(255),
        last_name varchar(255),
        mail varchar(255),
        password varchar(255),
        username varchar(255),
        primary key (id)
    )

    create table user_bar_authorization (
       id int8 not null,
        bar_id int8,
        role varchar(255),
        user_id int8,
        primary key (id)
    )

    create table "user_user_bar_authorizations" (
       "user_id" int8 not null,
        user_bar_authorizations_id int8 not null
    )

    alter table if exists bar_categories 
       add constraint UK_hlkhodyyl4d9x1sgs76p49a1g unique (categories_id)

    alter table if exists bar_people 
       add constraint UK_645i581qjajxmhayi7x7vcl1q unique (people_id)

    alter table if exists bar_products 
       add constraint UK_ijgjfo3wjhd4uyfhryhm8byr6 unique (products_id)

    alter table if exists bar_sessions 
       add constraint UK_a8iu547r18g8iregmbx7rj1aa unique (sessions_id)

    alter table if exists "bill_orders" 
       add constraint UK_eq6noll9kl3m9xelx1hho1rr6 unique ("orders_id")

    alter table if exists "user" 
       add constraint UK_6sou31qus5dnws6dwfu61e71v unique (mail)

    alter table if exists "user" 
       add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username)

    alter table if exists "user_user_bar_authorizations" 
       add constraint UK_7orid7iw1ljt2renofrt7uqnk unique (user_bar_authorizations_id)

    alter table if exists bar_categories 
       add constraint FKflspv66n9geafj6wyobuvjn3n 
       foreign key (categories_id) 
       references category

    alter table if exists bar_categories 
       add constraint FKb6wvu7vgbvvyjn2ycvy2iyjc7 
       foreign key (bar_id) 
       references bar

    alter table if exists bar_people 
       add constraint FK3kykcllirre3g54vg13p74a6e 
       foreign key (people_id) 
       references person

    alter table if exists bar_people 
       add constraint FKtitff4pi26msucas9die8d4w9 
       foreign key (bar_id) 
       references bar

    alter table if exists bar_products 
       add constraint FK4jnkvxrq1beu5ub8rrnjfeipe 
       foreign key (products_id) 
       references product

    alter table if exists bar_products 
       add constraint FKqj2f4i2vn2nkvyw5813280not 
       foreign key (bar_id) 
       references bar

    alter table if exists bar_sessions 
       add constraint FKldkxmr8ohrf4g8irv56lo483i 
       foreign key (sessions_id) 
       references session

    alter table if exists bar_sessions 
       add constraint FKrsat47b1vob5rv5g7onn93c0b 
       foreign key (bar_id) 
       references bar

    alter table if exists bill 
       add constraint FKgvyl51yuvu113jveh8neokhm7 
       foreign key (customer_id) 
       references person

    alter table if exists bill 
       add constraint FKnpdke1pihy8cr0lddxntf4hl2 
       foreign key (session_id) 
       references session

    alter table if exists "bill_orders" 
       add constraint FK1b0lpoex8ikeh2vm2k5b6022 
       foreign key ("orders_id") 
       references "order"

    alter table if exists "bill_orders" 
       add constraint FK5cux4tsugmy29yb7mutwceibl 
       foreign key (bill_id) 
       references bill

    alter table if exists "order" 
       add constraint FKarejjug8k6j3jm4h7pf24yhs6 
       foreign key (bartender_id) 
       references person

    alter table if exists "order" 
       add constraint FKknjaoi59nxmpxhr452bj95tgg 
       foreign key (product_id) 
       references product

    alter table if exists person 
       add constraint FKdunok34g8d08mu7mtocmhfptn 
       foreign key ("user_id") 
       references "user"

    alter table if exists product 
       add constraint FK1mtsbur82frn64de7balymq9s 
       foreign key (category_id) 
       references category

    alter table if exists "user_user_bar_authorizations" 
       add constraint FKjbfxkryjvkflmaph6jg9yoouk 
       foreign key (user_bar_authorizations_id) 
       references user_bar_authorization

    alter table if exists "user_user_bar_authorizations" 
       add constraint FKbk2srn0hyaaxfnruc5ll5qwdo 
       foreign key ("user_id") 
       references "user"


    create table bar_categories (
       bar_id int8 not null,
        categories_id int8 not null
    )

    create table bar_people (
       bar_id int8 not null,
        people_id int8 not null
    )

    create table bar_products (
       bar_id int8 not null,
        products_id int8 not null
    )

    create table bar_sessions (
       bar_id int8 not null,
        sessions_id int8 not null
    )

    create table bill (
       id int8 not null,
        is_payed boolean,
        customer_id int8,
        session_id int8 not null,
        primary key (id)
    )

    create table "bill_orders" (
       bill_id int8 not null,
        "orders_id" int8 not null
    )

    create table category (
       id int8 not null,
        name varchar(255),
        product_type varchar(255),
        primary key (id)
    )

    create table "order" (
       id int8 not null,
        amount int4,
        creation_date timestamp,
        bartender_id int8,
        product_id int8,
        primary key (id)
    )

    create table person (
       id int8 not null,
        name varchar(255),
        phone_number varchar(255),
        "user_id" int8,
        primary key (id)
    )

    create table product (
       id int8 not null,
        brand varchar(255),
        is_favorite boolean,
        name varchar(255),
        price float8,
        size float8,
        category_id int8,
        primary key (id)
    )

    create table session (
       id int8 not null,
        closed_date timestamp,
        creation_date timestamp,
        locked boolean not null,
        name varchar(255),
        primary key (id)
    )

    create table "user" (
       id int8 not null,
        first_name varchar(255),
        last_name varchar(255),
        mail varchar(255),
        password varchar(255),
        username varchar(255),
        primary key (id)
    )

    create table user_bar_authorization (
       id int8 not null,
        bar_id int8,
        role varchar(255),
        user_id int8,
        primary key (id)
    )

    create table "user_user_bar_authorizations" (
       "user_id" int8 not null,
        user_bar_authorizations_id int8 not null
    )

    alter table if exists bar_categories 
       add constraint UK_hlkhodyyl4d9x1sgs76p49a1g unique (categories_id)

    alter table if exists bar_people 
       add constraint UK_645i581qjajxmhayi7x7vcl1q unique (people_id)

    alter table if exists bar_products 
       add constraint UK_ijgjfo3wjhd4uyfhryhm8byr6 unique (products_id)

    alter table if exists bar_sessions 
       add constraint UK_a8iu547r18g8iregmbx7rj1aa unique (sessions_id)

    alter table if exists "bill_orders" 
       add constraint UK_eq6noll9kl3m9xelx1hho1rr6 unique ("orders_id")

    alter table if exists "user" 
       add constraint UK_6sou31qus5dnws6dwfu61e71v unique (mail)

    alter table if exists "user" 
       add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username)

    alter table if exists "user_user_bar_authorizations" 
       add constraint UK_7orid7iw1ljt2renofrt7uqnk unique (user_bar_authorizations_id)

    alter table if exists bar_categories 
       add constraint FKflspv66n9geafj6wyobuvjn3n 
       foreign key (categories_id) 
       references category

    alter table if exists bar_categories 
       add constraint FKb6wvu7vgbvvyjn2ycvy2iyjc7 
       foreign key (bar_id) 
       references bar

    alter table if exists bar_people 
       add constraint FK3kykcllirre3g54vg13p74a6e 
       foreign key (people_id) 
       references person

    alter table if exists bar_people 
       add constraint FKtitff4pi26msucas9die8d4w9 
       foreign key (bar_id) 
       references bar

    alter table if exists bar_products 
       add constraint FK4jnkvxrq1beu5ub8rrnjfeipe 
       foreign key (products_id) 
       references product

    alter table if exists bar_products 
       add constraint FKqj2f4i2vn2nkvyw5813280not 
       foreign key (bar_id) 
       references bar

    alter table if exists bar_sessions 
       add constraint FKldkxmr8ohrf4g8irv56lo483i 
       foreign key (sessions_id) 
       references session

    alter table if exists bar_sessions 
       add constraint FKrsat47b1vob5rv5g7onn93c0b 
       foreign key (bar_id) 
       references bar

    alter table if exists bill 
       add constraint FKgvyl51yuvu113jveh8neokhm7 
       foreign key (customer_id) 
       references person

    alter table if exists bill 
       add constraint FKnpdke1pihy8cr0lddxntf4hl2 
       foreign key (session_id) 
       references session

    alter table if exists "bill_orders" 
       add constraint FK1b0lpoex8ikeh2vm2k5b6022 
       foreign key ("orders_id") 
       references "order"

    alter table if exists "bill_orders" 
       add constraint FK5cux4tsugmy29yb7mutwceibl 
       foreign key (bill_id) 
       references bill

    alter table if exists "order" 
       add constraint FKarejjug8k6j3jm4h7pf24yhs6 
       foreign key (bartender_id) 
       references person

    alter table if exists "order" 
       add constraint FKknjaoi59nxmpxhr452bj95tgg 
       foreign key (product_id) 
       references product

    alter table if exists person 
       add constraint FKdunok34g8d08mu7mtocmhfptn 
       foreign key ("user_id") 
       references "user"

    alter table if exists product 
       add constraint FK1mtsbur82frn64de7balymq9s 
       foreign key (category_id) 
       references category

    alter table if exists "user_user_bar_authorizations" 
       add constraint FKjbfxkryjvkflmaph6jg9yoouk 
       foreign key (user_bar_authorizations_id) 
       references user_bar_authorization

    alter table if exists "user_user_bar_authorizations" 
       add constraint FKbk2srn0hyaaxfnruc5ll5qwdo 
       foreign key ("user_id") 
       references "user"
