/*
Created: 01.12.2024
Modified: 01.12.2024
Model: mySQL 8
Database: MySQL 8.0
*/

-- Create tables section -------------------------------------------------

-- Table Players

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

-- Table Ratings

CREATE TABLE `ratings`
(
  `id` Serial NOT NULL,
  `player_id` Serial NOT NULL,
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

-- Table Tourneys

CREATE TABLE `tourneys`
(
  `id` Serial NOT NULL,
  `competition_id` Serial NOT NULL,
  `title` Varchar(200) NOT NULL,
  `type` Bigint NOT NULL,
  `rank` Smallint NOT NULL,
  `stage` Smallint NOT NULL
)
;

CREATE INDEX `IX_Relationship5` ON `tourneys` (`competition_id`)
;

ALTER TABLE `tourneys` ADD PRIMARY KEY (`id`)
;

-- Table Competitions

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

-- Table Games

CREATE TABLE `games`
(
  `id` Serial NOT NULL,
  `tour` Smallint NOT NULL,
  `rating1` Float NOT NULL,
  `rating2` Float NOT NULL,
  `result` Smallint NOT NULL,
  `tourney_id` Serial NOT NULL,
  `tourney_player1_id` Serial NOT NULL,
  `tourney_player2_id` Serial NOT NULL
)
;

CREATE INDEX `IX_Relationship29` ON `games` (`tourney_id`, `tourney_player2_id`)
;

CREATE INDEX `IX_Relationship28` ON `games` (`tourney_id`, `tourney_player1_id`)
;

ALTER TABLE `games` ADD PRIMARY KEY (`id`)
;

-- Table Tourney_Players

CREATE TABLE `tourney_players`
(
  `id` Serial NOT NULL,
  `tourney_id` Serial NOT NULL,
  `player_id` Serial NOT NULL,
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

