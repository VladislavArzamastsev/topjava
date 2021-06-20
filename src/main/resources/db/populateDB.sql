DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, TO_TIMESTAMP('2020-01-30 10-00', 'YYYY-MM-DD HH24-MI'), 'Завтрак', 467),
       (100000, TO_TIMESTAMP('2020-01-30 13-00', 'YYYY-MM-DD HH24-MI'), 'Обед', 611),
       (100000, TO_TIMESTAMP('2020-01-30 20-00', 'YYYY-MM-DD HH24-MI'), 'Ужин', 500),
       (100000, TO_TIMESTAMP('2020-01-31 00-00', 'YYYY-MM-DD HH24-MI'), 'Еда на граничное значение', 100),
       (100000, TO_TIMESTAMP('2020-01-31 10-00', 'YYYY-MM-DD HH24-MI'), 'Завтрак', 520),
       (100000, TO_TIMESTAMP('2020-01-31 13-00', 'YYYY-MM-DD HH24-MI'), 'Обед', 1000),
       (100000, TO_TIMESTAMP('2020-01-31 20-00', 'YYYY-MM-DD HH24-MI'), 'Ужин', 500),
       (100001, TO_TIMESTAMP('2020-01-31 10-00', 'YYYY-MM-DD HH24-MI'), 'Завтрак', 400),
       (100001, TO_TIMESTAMP('2020-01-31 13-00', 'YYYY-MM-DD HH24-MI'), 'Обед', 520),
       (100001, TO_TIMESTAMP('2020-01-31 20-00', 'YYYY-MM-DD HH24-MI'), 'Ужин', 930);
