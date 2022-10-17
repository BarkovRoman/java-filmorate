drop table FILM_GENRES;
drop table FRIENDS;
drop table GENRES;
drop table LIKES;
drop table FILMS;
drop table MPA;
drop table USERS;

CREATE TABLE IF NOT EXISTS GENRES (
    ID_GENRES INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME_GENRES varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS MPA (
    ID_MPA INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME_MPA varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS FILMS (
    ID_FILMS INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME_FILMS varchar(100) NOT NULL,
    DESCRIPTION varchar(200) NOT NULL,
    RELEASE_DATE datetime,
    DURATION int NOT NULL,
    MPA INTEGER REFERENCES MPA (ID_MPA)
);

CREATE TABLE IF NOT EXISTS FILM_GENRES (
    FILM_ID INTEGER REFERENCES FILMS (ID_FILMS) ON DELETE CASCADE,
    GENRE_ID INTEGER REFERENCES GENRES (ID_GENRES)
);

CREATE TABLE IF NOT EXISTS USERS (
    ID_USER  INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    EMAIL varchar NOT NULL,
    LOGIN varchar(100) NOT NULL,
    NAME_USER varchar(255),
    BIRTHDAY datetime
);

CREATE TABLE IF NOT EXISTS LIKES (
  FILM_ID INTEGER REFERENCES FILMS (ID_FILMS) ON DELETE CASCADE,
  USER_ID INTEGER REFERENCES USERS (ID_USER) ON DELETE CASCADE,
  CONSTRAINT User_film_ike UNIQUE (FILM_ID, USER_ID)
);

CREATE TABLE IF NOT EXISTS FRIENDS (
  USER_ID INTEGER REFERENCES USERS (ID_USER) ON DELETE CASCADE,
  OTHER_ID INTEGER REFERENCES USERS (ID_USER) ON DELETE CASCADE,
  CONSTRAINT User_friends UNIQUE (USER_ID, OTHER_ID)
);

