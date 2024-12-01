/*
Created: 01.12.2024
Modified: 01.12.2024
Model: mySQL 8
Database: MySQL 8.0
*/

-- Create tables section -------------------------------------------------

-- Table Players

CREATE TABLE `Players`
(
  `id` Serial NOT NULL,
  `Name` Varchar(200) NOT NULL,
  `Birth_Place` Varchar(200),
  `Birth_Date` Date,
  `Location` Varchar(200),
  `Gender` Bigint
)
;

ALTER TABLE `Players` ADD PRIMARY KEY (`id`)
;

-- Table Ratings

CREATE TABLE `Ratings`
(
  `id` Serial NOT NULL,
  `player_id` Serial NOT NULL,
  `Rating_In` Float,
  `Rating_Current` Float,
  `Rating_Out` Float,
  `Year` Smallint NOT NULL,
  `Month` Smallint NOT NULL,
  `Week` Smallint NOT NULL
)
;

ALTER TABLE `Ratings` ADD PRIMARY KEY (`player_id`, `id`)
;

-- Table Tourneys

CREATE TABLE `Tourneys`
(
  `id` Serial NOT NULL,
  `competition_id` Serial NOT NULL,
  `Title` Varchar(200) NOT NULL,
  `Type` Bigint NOT NULL,
  `Rank` Smallint NOT NULL,
  `Stage` Smallint NOT NULL
)
;

CREATE INDEX `IX_Relationship5` ON `Tourneys` (`competition_id`)
;

ALTER TABLE `Tourneys` ADD PRIMARY KEY (`id`)
;

-- Table Competitions

CREATE TABLE `Competitions`
(
  `id` Serial NOT NULL,
  `Title` Varchar(200) NOT NULL,
  `Date` Date NOT NULL,
  `Location` Varchar(200) NOT NULL
)
;

ALTER TABLE `Competitions` ADD PRIMARY KEY (`id`)
;

-- Table Games

CREATE TABLE `Games`
(
  `id` Serial NOT NULL,
  `Tour` Smallint NOT NULL,
  `Rating1` Float NOT NULL,
  `Rating2` Float NOT NULL,
  `Result` Smallint NOT NULL,
  `tourney_id` Serial NOT NULL,
  `tourney_player1_id` Serial NOT NULL,
  `tourney_player2_id` Serial NOT NULL
)
;

CREATE INDEX `IX_Relationship29` ON `Games` (`tourney_id`, `tourney_player2_id`)
;

CREATE INDEX `IX_Relationship28` ON `Games` (`tourney_id`, `tourney_player1_id`)
;

ALTER TABLE `Games` ADD PRIMARY KEY (`id`)
;

-- Table Tourney_Players

CREATE TABLE `Tourney_Players`
(
  `id` Serial NOT NULL,
  `tourney_id` Serial NOT NULL,
  `player_id` Serial NOT NULL,
  `Rating_In` Float,
  `Rating_Current` Float,
  `Rating_Out` Float
)
;

CREATE INDEX `IX_Relationship20` ON `Tourney_Players` (`player_id`)
;

ALTER TABLE `Tourney_Players` ADD PRIMARY KEY (`tourney_id`, `id`)
;

-- Create foreign keys (relationships) section -------------------------------------------------

ALTER TABLE `Ratings` ADD CONSTRAINT `Relationship4` FOREIGN KEY (`player_id`) REFERENCES `Players` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE `Tourneys` ADD CONSTRAINT `Relationship5` FOREIGN KEY (`competition_id`) REFERENCES `Competitions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE `Tourney_Players` ADD CONSTRAINT `Relationship10` FOREIGN KEY (`tourney_id`) REFERENCES `Tourneys` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE `Tourney_Players` ADD CONSTRAINT `Relationship20` FOREIGN KEY (`player_id`) REFERENCES `Players` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE `Games` ADD CONSTRAINT `Relationship28` FOREIGN KEY (`tourney_id`, `tourney_player1_id`) REFERENCES `Tourney_Players` (`tourney_id`, `id`) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE `Games` ADD CONSTRAINT `Relationship29` FOREIGN KEY (`tourney_id`, `tourney_player2_id`) REFERENCES `Tourney_Players` (`tourney_id`, `id`) ON DELETE NO ACTION ON UPDATE NO ACTION
;

