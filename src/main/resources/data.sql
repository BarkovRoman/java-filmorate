DELETE FROM USERS where id_user > 0;
DELETE FROM FILMS where id_films > 0;
DELETE FROM LIKES where film_id > 0;
DELETE FROM FRIENDS where user_id > 0;
DELETE FROM FILM_GENRES where film_id > 0;
ALTER TABLE USERS ALTER COLUMN id_user RESTART WITH 1;
ALTER TABLE FILMS ALTER COLUMN id_films RESTART WITH 1;


MERGE INTO GENRES (id_genres, name_genres)
    values (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

MERGE INTO MPA (ID_MPA, NAME_MPA)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');






