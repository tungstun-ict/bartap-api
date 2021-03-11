insert into "user"(id, password, username, mail, first_name, last_name)
values (1, '$2a$10$FAmWZOw692jCu0bkdVkj3ecSqlBqijjj5I3QMznGp2uLYiQXGr5hW', 'bart', 'bart@mail.com', 'Bart', 'Schouten'),
       (2, '$2a$10$FAmWZOw692jCu0bkdVkj3ecSqlBqijjj5I3QMznGp2uLYiQXGr5hW', 'user', 'test@mail.com', 'testFirst', 'testLast');

insert into user_bar_authorization(id, bar_id, role, user_id)
values (1, 1, 'ROLE_BAR_OWNER', 1),
       (2, 2, 'ROLE_BAR_OWNER', 2),
       (3, 3, 'ROLE_BAR_OWNER', 2),
       (4, 4, 'ROLE_BAR_OWNER', 2),
       (5, 5, 'ROLE_BAR_OWNER', 2);

insert into user_user_bar_authorizations (user_id, user_bar_authorizations_id)
values (1, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5);

insert into bar(id, address, mail, name, phone_number)

VALUES  (1, 'Dorpsstraat 192', 'dorpsstraat@staat.nl', 'Bartjes Bar', '1122334455'),
        (2, 'Molenstraat 15', 'molenstraat@staat.nl', 'De Kerker Bar', '7812344562'),
        (3, 'Kerkstraat 6', 'kerkstraat@staat.nl', 'De Kerker Bar', '0987654321'),
        (4, 'Schoolstraat 1a', 'schoolstraat@staat.nl', 'De School Pub', '1234567890'),
        (5, 'Molenweg 1', 'molenweg@staat.nl', 'De Molen', '6677889900');

insert into person (id, name, phone_number, user_id)
values (1, 'Bart Schouten', '66772323332', 1),
       (2, 'Jort Willemsen', '0912897344', null),
       (3, 'Jona Leeflang', '4567890044', null),
       (4, 'Sep de Geus', '0632334233', null),
       (5, 'Fee Sanders', '0633144444', null),
       (6, 'Test User', '1234670000', 2);

insert into bar_people(bar_id, people_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (2, 6);

insert into category(id, name, product_type)
VALUES (1, 'Bier', 'DRINK'),
       (2, 'Wijn', 'DRINK'),
       (3, 'Sterk', 'DRINK'),
       (4, 'Fris', 'DRINK'),
       (5, 'Laag alcohol', 'DRINK'),
       (6, 'Schnaps', 'DRINK'),
       (7, 'Shots', 'DRINK'),
       (8, 'Mix', 'DRINK'),
       (9, 'Snacks', 'FOOD'),
       (10, 'Chips', 'FOOD'),
       (11, 'Snoep', 'FOOD'),
       (12, 'Nootjes', 'FOOD'),
       (13, 'Gerechten', 'FOOD'),
       (14, 'Kaas', 'FOOD'),
       (15, 'Art suplies', 'OTHER'),
       (16, 'Dremmles', 'OTHER'),
       (17, 'Go Back, I Want To be MONKE', 'OTHER'),
       (18, 'Bier', 'DRINK'),
       (19, 'Wijn', 'DRINK'),
       (20, 'Sterk', 'DRINK'),
       (21, 'Fris', 'DRINK'),
       (22, 'Laag alcohol', 'DRINK'),
       (23, 'Schnaps', 'DRINK'),
       (24, 'Shots', 'DRINK'),
       (25, 'Mix', 'DRINK');


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

insert into product(id, brand, is_favorite, name, price, product_type, size, category_id)
VALUES (1, 'Heiniken', true, 'Pils vaasje', 1.30, 'DRINK', 250, 1),
       (2, 'Heiniken', true, 'Pils glas', 1.60, 'DRINK', 330, 1),
       (3, 'Heiniken', false, 'Pils pitcher', 9.20, 'DRINK', 1800, 1),
       (4, 'Apple Bandit', false, 'Apple cider Juicy Apple', 1.50, 'DRINK', 300, 1),
       (5, 'Stoney Creek', true, 'Shiraz', 1.80, 'DRINK', 200, 2),
       (6, 'Stoney Creek', false, 'Chardonnay', 1.80, 'DRINK', 200, 2),
       (7, 'Johney Walker', false, 'Red Label', 2.10, 'DRINK', 100, 3),
       (8, 'Absolut', false, 'Vodka ', 2.00, 'DRINK', 0.1, 7),
       (9, 'Bartjes Bar', true, 'Watermelon Dream ', 2.20, 'DRINK',330, 8),
       (10, 'Coca Cola', false, 'Cola ', 1.50, 'DRINK', 100, 7),
       (11, 'Lays', false, 'Paprika chips ', 3.00, 'FOOD', 350, 10),
       (12, 'Duyvis', false, 'Nootjes ', 2.30, 'FOOD', 230, 12),
       (13, 'Bartjes Bar', false, 'GO BACK TO MONKE: Special ', 2.00, 'OTHER', 0.1, 17);

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

insert into session_bartender (user_id, id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4);

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
