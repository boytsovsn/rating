drop table if exists users;

CREATE TABLE IF NOT EXISTS users (
    `id` serial not null,
    `lock` Bigint,
    `email` varchar(255),
    `firstname` varchar(255),
    `lastname` varchar(255),
    `login` varchar(255),
    `password` varchar(255),
    `role` varchar(255),
    `surname` varchar(255),
    primary key (id)
)
;

--
drop table if exists games;
drop table if exists tourney_players;
drop table if exists tourneys;
drop table if exists competitions;
drop table if exists ratings;
drop table if exists players;/*
-- Created: 01.12.2024
-- Modified: 01.12.2024
-- Model: mySQL 8
-- Database: MySQL 8.0
-- */
--
-- -- Create tables section -------------------------------------------------
--
-- -- Table players
--
CREATE TABLE `players`
(
    `id` Serial NOT NULL,
    `name` Varchar(200) NOT NULL,
    `birth_place` Varchar(200),
    `birth_date` Date,
    `location` Varchar(200),
    `gender` Bigint
)
;

ALTER TABLE `players` ADD PRIMARY KEY (`id`)
;

-- Table ratings

CREATE TABLE `ratings`
(
    `id` Serial NOT NULL,
    `player_id` Bigint UNSIGNED NOT NULL,
    `rating_in` Float,
    `rating_current` Float,
    `rating_out` Float,
    `year` Smallint NOT NULL,
    `month` Smallint NOT NULL,
    `week` Smallint NOT NULL
)
;

ALTER TABLE `ratings` ADD PRIMARY KEY (`player_id`, `id`)
;

-- Table tourneys

CREATE TABLE `tourneys`
(
    `id` Serial NOT NULL,
    `competition_id` bigint UNSIGNED  NOT NULL,
    `title` Varchar(200) NOT NULL,
    `type` Bigint NOT NULL,
    `tour_rank` Smallint NOT NULL,
    `stage` Smallint NOT NULL
)
;

CREATE INDEX `IX_Relationship5` ON `tourneys` (`competition_id`)
;

ALTER TABLE `tourneys` ADD PRIMARY KEY (`id`)
;

-- Table competitions

CREATE TABLE `competitions`
(
    `id` Serial NOT NULL,
    `title` Varchar(200) NOT NULL,
    `date` Date NOT NULL,
    `location` Varchar(200) NOT NULL
)
;

ALTER TABLE `competitions` ADD PRIMARY KEY (`id`)
;

-- Table games

CREATE TABLE `games`
(
    `id` Serial NOT NULL,
    `tour` Smallint NOT NULL,
    `rating1` Float NOT NULL,
    `rating2` Float NOT NULL,
    `result` Smallint NOT NULL,
    `tourney_id` bigint UNSIGNED NOT NULL,
    `tourney_player1_id` bigint UNSIGNED NOT NULL,
    `tourney_player2_id` bigint UNSIGNED NOT NULL
)
;

CREATE INDEX `IX_Relationship29` ON `games` (`tourney_id`, `tourney_player2_id`)
;

CREATE INDEX `IX_Relationship28` ON `games` (`tourney_id`, `tourney_player1_id`)
;

ALTER TABLE `games` ADD PRIMARY KEY (`id`)
;

-- Table tourney_players

CREATE TABLE `tourney_players`
(
    `id` Serial NOT NULL,
    `tourney_id` bigint UNSIGNED NOT NULL,
    `player_id` bigint UNSIGNED NOT NULL,
    `rating_in` Float,
    `rating_current` Float,
    `rating_out` Float
)
;

CREATE INDEX `IX_Relationship20` ON `tourney_players` (`player_id`)
;

ALTER TABLE `tourney_players` ADD PRIMARY KEY (`tourney_id`, `id`)
;

-- Create foreign keys (relationships) section -------------------------------------------------

ALTER TABLE `ratings` ADD CONSTRAINT `Relationship4` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE `tourneys` ADD CONSTRAINT `Relationship5` FOREIGN KEY (`competition_id`) REFERENCES `competitions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE `tourney_players` ADD CONSTRAINT `Relationship10` FOREIGN KEY (`tourney_id`) REFERENCES `tourneys` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE `tourney_players` ADD CONSTRAINT `Relationship20` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE `games` ADD CONSTRAINT `Relationship28` FOREIGN KEY (`tourney_id`, `tourney_player1_id`) REFERENCES `tourney_players` (`tourney_id`, `id`) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE `games` ADD CONSTRAINT `Relationship29` FOREIGN KEY (`tourney_id`, `tourney_player2_id`) REFERENCES `tourney_players` (`tourney_id`, `id`) ON DELETE NO ACTION ON UPDATE NO ACTION
;

