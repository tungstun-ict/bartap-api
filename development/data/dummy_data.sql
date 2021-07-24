insert into "user"(id, password, username, mail, first_name, last_name)
values (1, '$2a$10$FAmWZOw692jCu0bkdVkj3ecSqlBqijjj5I3QMznGp2uLYiQXGr5hW', 'admin', 'admin@mail.com', 'Admin', 'aDMIN'),
       (2, '$2a$10$FAmWZOw692jCu0bkdVkj3ecSqlBqijjj5I3QMznGp2uLYiQXGr5hW', 'user', 'test@mail.com', 'testFirst', 'testLast'),
       (3, '$2a$04$dKIufHL4vTOUG7e2f4.plu3tHUqbGdhuf14Ss.GZyuPUTOUp.r4wi', 'Bart', 'bartschouten02@live.nl', 'Bart', 'Schouten');

insert into bar(id, deleted, address, mail, name, phone_number)
VALUES  (1, false, 'Molenstraat 15', 'molenstraat@staat.nl', 'De Kerker Bar', '7812344562'),
        (2, false, 'Dorpsstraat 192', 'dorpsstraat@staat.nl', 'Bartjes Bar', '1122334455'),
        (3, false, 'Kerkstraat 6', 'kerkstraat@staat.nl', 'De Kerker Bar', '0987654321'),
        (4, false, 'Schoolstraat 1a', 'schoolstraat@staat.nl', 'De School Pub', '1234567890'),
        (5, false, 'Molenweg 1', 'molenweg@staat.nl', 'De Molen', '6677889900');

insert into user_bar_authorization(id, bar_id, role, user_id)
values (1, 1, 'ROLE_BAR_OWNER', 1),
       (2, 2, 'ROLE_BAR_OWNER', 3),
       (3, 3, 'ROLE_BAR_OWNER', 2),
       (4, 4, 'ROLE_BAR_OWNER', 2),
       (5, 5, 'ROLE_BAR_OWNER', 2);

insert into user_user_bar_authorizations (user_id, user_bar_authorizations_id)
values (3, 2),
       (1, 1),
       (2, 3),
       (2, 4),
       (2, 5);

insert into person (id, name, phone_number, user_id)
values (1, 'Bart Schouten', '66772323332', 3),
       (2, 'Jort Willemsen', '0912897344', null),
       (3, 'Jona Leeflang', '4567890044', null),
       (4, 'Sep de Geus', '0632334233', null),
       (5, 'Fee Sanders', '0633144444', null),
       (6, 'admin', '1234670000', 1);

insert into bar_people(bar_id, people_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (2, 6);

insert into category(id, name, product_type, deleted)
VALUES (1, 'Bier', 'DRINK', false),
       (2, 'Wijn', 'DRINK', false),
       (3, 'Sterk', 'DRINK', false),
       (4, 'Fris', 'DRINK', false),
       (5, 'Laag alcohol', 'DRINK', false),
       (6, 'Schnaps', 'DRINK', false),
       (7, 'Shots', 'DRINK', false),
       (8, 'Mix', 'DRINK', false),
       (9, 'Snacks', 'FOOD', false),
       (10, 'Chips', 'FOOD', false),
       (11, 'Snoep', 'FOOD', false),
       (12, 'Nootjes', 'FOOD', false),
       (13, 'Gerechten', 'FOOD', false),
       (14, 'Kaas', 'FOOD', false),
       (15, 'Art suplies', 'OTHER', false),
       (16, 'Dremmles', 'OTHER', false),
       (17, 'Go Back, I Want To be MONKE', 'OTHER', false),
       (18, 'Bier', 'DRINK', false),
       (19, 'Wijn', 'DRINK', false),
       (20, 'Sterk', 'DRINK', false),
       (21, 'Fris', 'DRINK', false),
       (22, 'Laag alcohol', 'DRINK', false),
       (23, 'Schnaps', 'DRINK', false),
       (24, 'Shots', 'DRINK', false),
       (25, 'Mix', 'DRINK', false);


insert into bar_categories(bar_id, categories_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (1, 12),
       (1, 13),
       (1, 14),
       (1, 15),
       (1, 16),
       (1, 17),
       (2, 18),
       (2, 19),
       (3, 20),
       (3, 21),
       (4, 22),
       (4, 23),
       (5, 24),
       (5, 25);

insert into stock(id, amount)
VALUES (1, 1000),
       (2, 1000),
       (3, 1000),
       (4, 1000),
       (5, 1000),
       (6, 1000),
       (7, 1000),
       (8, 1000),
       (9, 1000),
       (10, 1000),
       (11, 1000),
       (12, 1000),
       (13, 1000);

insert into product(id, brand, is_favorite, name, price, size, category_id, stock_id, deleted)
VALUES (1, 'Heiniken', true, 'Pils vaasje', 1.30, 250, 1, 1, false),
       (2, 'Heiniken', true, 'Pils glas', 1.60, 330, 1, 2, false),
       (3, 'Heiniken', false, 'Pils pitcher', 9.20, 1800, 1, 3, false),
       (4, 'Apple Bandit', false, 'Apple cider Juicy Apple', 1.50, 300, 1, 4, false),
       (5, 'Stoney Creek', true, 'Shiraz', 1.80, 200, 2, 5, false),
       (6, 'Stoney Creek', false, 'Chardonnay', 1.80, 200, 2, 6, false),
       (7, 'Johney Walker', false, 'Red Label', 2.10, 100, 3, 7, false),
       (8, 'Absolut', false, 'Vodka ', 2.00, 0.1, 7, 8, false),
       (9, 'Bartjes Bar', true, 'Watermelon Dream ', 2.20,330, 8, 9, false),
       (10, 'Coca Cola', false, 'Cola ', 1.50, 100, 7, 10, false),
       (11, 'Lays', false, 'Paprika chips ', 3.00, 350, 10, 11, false),
       (12, 'Duyvis', false, 'Nootjes ', 2.30, 230, 12, 12, false),
       (13, 'Bartjes Bar', false, 'GO BACK TO MONKE: Special ', 2.00, 0.1, 17, 13, false);

insert into bar_products (bar_id, products_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (1, 12),
       (1, 13);

insert into session(id, name, creation_date, closed_date, locked)
values (1, 'Eerst sessie', date('2021-03-06 22:45:00.0'), date('2021-03-07 01:45:00.0'), true),
       (2, 'Tweede sessie', date('2021-03-07 22:45:00.0'), date('2021-03-08 01:45:00.0'), true),
       (3, 'Derde sessie', date('2021-03-08 22:45:00.0'), date('2021-03-09 01:45:00.0'), false),
       (4, 'Vierde sessie', date('2021-03-09 22:45:00.0'), null, false);



insert into bar_sessions (bar_id, sessions_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4);

insert into bill(id, is_payed, customer_id, session_id)
values (1, true, 2, 1),
       (2, true, 3, 1),
       (3, true, 3, 2),
       (4, true, 4, 2),
       (5, false, 5, 2),
       (6, true, 2, 3),
       (7, true, 3, 3),
       (8, false, 4, 3),
       (9, false, 5, 3),
       (10, false, 2, 4),
       (11, false, 4, 4);

insert into "order"(id, amount, creation_date, bartender_id, product_id)
values (1, 1, date('2021-03-06 22:50:00.0'), 1, 1),
       (2, 3, date('2021-03-06 22:55:00.0'), 1, 1),
       (3, 2, date('2021-03-06 23:00:00.0'), 1, 1),
       (4, 5, date('2021-03-06 23:05:00.0'), 1, 1),
       (5, 1, date('2021-03-06 23:10:00.0'), 1, 4),
       (6, 2, date('2021-03-06 23:15:00.0'), 1, 1),
       (7, 1, date('2021-03-06 23:20:00.0'), 1, 7),
       (8, 1, date('2021-03-06 23:25:00.0'), 1, 4),
       (9, 2, date('2021-03-06 23:30:00.0'), 1, 1),

       (10, 2, date('2021-03-06 23:35:00.0'), 1, 1),
       (11, 1, date('2021-03-06 23:40:00.0'), 1, 10),
       (12, 5, date('2021-03-06 23:45:00.0'), 1, 1),
       (13, 1, date('2021-03-06 23:50:00.0'), 1, 1),
       (14, 4, date('2021-03-06 23:55:00.0'), 1, 12),
       (15, 2, date('2021-03-07 00:00:00.0'), 1, 1),
       (16, 1, date('2021-03-07 00:00:00.0'), 1, 1),

       (17, 5, date('2021-03-07 23:30:00.0'), 1, 1),
       (18, 1, date('2021-03-07 23:35:00.0'), 1, 4),
       (19, 1, date('2021-03-07 23:40:00.0'), 1, 7),

       (20, 1, date('2021-03-09 22:50:00.0'), 1, 1),
       (21, 3, date('2021-03-09 22:55:00.0'), 1, 1),
       (22, 2, date('2021-03-09 23:00:00.0'), 1, 1),
       (23, 5, date('2021-03-09 23:05:00.0'), 1, 1),
       (24, 1, date('2021-03-09 23:10:00.0'), 1, 4),
       (25, 2, date('2021-03-09 23:15:00.0'), 1, 1),
       (26, 1, date('2021-03-09 23:20:00.0'), 1, 7),
       (27, 1, date('2021-03-09 23:25:00.0'), 1, 4),
       (28, 2, date('2021-03-09 23:30:00.0'), 1, 1);

insert into bill_orders(bill_id, orders_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),

       (2, 10),
       (2, 11),
       (2, 12),
       (2, 13),
       (2, 14),
       (2, 15),
       (2, 16),

       (10, 20),
       (10, 21),
       (11, 22),
       (10, 23),
       (10, 24),
       (11, 25),
       (11, 26),
       (11, 27),
       (10, 28);
