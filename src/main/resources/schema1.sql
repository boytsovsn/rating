drop table if exists users;

CREATE TABLE IF NOT EXISTS users (
                                     id bigserial not null,
                                     lock boolean,
                                     email varchar(255),
                                     firstname varchar(255),
                                     lastname varchar(255),
                                     login varchar(255),
                                     password varchar(255),
                                     role varchar(255),
                                     surname varchar(255),
                                     primary key (id)
);


drop table if exists Games;
drop table if exists Tourney_Players;
drop table if exists Tourneys;
drop table if exists Competitions;
drop table if exists Ratings;
drop table if exists Players;

/*
Created: Вт 23.04.24
Modified: Сб 01.06.24
Model: PostgreSQL 12
Database: PostgreSQL 12
*/

-- Create tables section -------------------------------------------------

-- Table Players

CREATE TABLE  Players
(
    id  BigSerial NOT NULL,
    Name  Character varying(200) NOT NULL,
    Birth_Place  Character varying(200),
    Birth_Date  Date,
    Location  Character varying(200),
    Gender  Boolean
)
    WITH (
        autovacuum_enabled=true)
;

ALTER TABLE  Players  ADD CONSTRAINT  PK_Players  PRIMARY KEY ( id )
;

-- Table Ratings

CREATE TABLE  Ratings
(
    id  BigSerial NOT NULL,
    player_id  Bigint NOT NULL,
    Rating_In  Real,
    Rating_Current  Real,
    Rating_Out  Real,
    Year  Smallint NOT NULL,
    Month  Smallint NOT NULL,
    Week  Smallint NOT NULL
)
    WITH (
        autovacuum_enabled=true)
;

ALTER TABLE  Ratings  ADD CONSTRAINT  PK_Ratings  PRIMARY KEY ( player_id , id )
;

-- Table Tourneys

CREATE TABLE  Tourneys
(
    id  BigSerial NOT NULL,
    competition_id  Bigint NOT NULL,
    Title  Character varying(200) NOT NULL,
    Type  Bigint NOT NULL,
    Rank  Smallint NOT NULL,
    Stage  Smallint NOT NULL
)
    WITH (
        autovacuum_enabled=true)
;

CREATE INDEX  IX_Relationship5  ON  Tourneys  ( competition_id )
;

ALTER TABLE  Tourneys  ADD CONSTRAINT  PK_Tourneys  PRIMARY KEY ( id )
;

-- Table Competitions

CREATE TABLE  Competitions
(
    id  BigSerial NOT NULL,
    Title  Character varying(200) NOT NULL,
    Date  Date NOT NULL,
    Location  Character varying(200) NOT NULL
)
    WITH (
        autovacuum_enabled=true)
;

ALTER TABLE  Competitions  ADD CONSTRAINT  PK_Competitions  PRIMARY KEY ( id )
;

-- Table Games

CREATE TABLE  Games
(
    id  BigSerial NOT NULL,
    Tour  Smallint NOT NULL,
    Rating1  Real NOT NULL,
    Rating2  Real NOT NULL,
    Result  Smallint NOT NULL,
    tourney_id  Bigint NOT NULL,
    tourney_player1_id  Bigint NOT NULL,
    tourney_player2_id  Bigint NOT NULL
)
    WITH (
        autovacuum_enabled=true)
;

CREATE INDEX  IX_Relationship29  ON  Games  ( tourney_id , tourney_player2_id )
;

CREATE INDEX  IX_Relationship28  ON  Games  ( tourney_id , tourney_player1_id )
;

ALTER TABLE  Games  ADD CONSTRAINT  PK_Games  PRIMARY KEY ( id )
;

-- Table Tourney_Players

CREATE TABLE  Tourney_Players
(
    id  BigSerial NOT NULL,
    tourney_id  Bigint NOT NULL,
    player_id  Bigint NOT NULL,
    Rating_In  Real,
    Rating_Current  Real,
    Rating_Out  Real
)
    WITH (
        autovacuum_enabled=true)
;

CREATE INDEX  IX_Relationship20  ON  Tourney_Players  ( player_id )
;

ALTER TABLE  Tourney_Players  ADD CONSTRAINT  PK_Tourney_Players  PRIMARY KEY ( tourney_id , id )
;

-- Create materialized views section -------------------------------------------------

--CREATE MATERIALIZED VIEW  MaterializedView1
--WITH (
--  autovacuum_enabled=true)
--;

-- Create foreign keys (relationships) section -------------------------------------------------

ALTER TABLE  Ratings
    ADD CONSTRAINT  Relationship4
        FOREIGN KEY ( player_id )
            REFERENCES  Players  ( id )
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
;

ALTER TABLE  Tourneys
    ADD CONSTRAINT  Relationship5
        FOREIGN KEY ( competition_id )
            REFERENCES  Competitions  ( id )
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
;

ALTER TABLE  Tourney_Players
    ADD CONSTRAINT  Relationship10
        FOREIGN KEY ( tourney_id )
            REFERENCES  Tourneys  ( id )
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
;

ALTER TABLE  Tourney_Players
    ADD CONSTRAINT  Relationship20
        FOREIGN KEY ( player_id )
            REFERENCES  Players  ( id )
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
;

ALTER TABLE  Games
    ADD CONSTRAINT  Relationship28
        FOREIGN KEY ( tourney_id ,  tourney_player1_id )
            REFERENCES  Tourney_Players  ( tourney_id ,  id )
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
;

ALTER TABLE  Games
    ADD CONSTRAINT  Relationship29
        FOREIGN KEY ( tourney_id ,  tourney_player2_id )
            REFERENCES  Tourney_Players  ( tourney_id ,  id )
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
;

