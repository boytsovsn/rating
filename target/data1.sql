-- delete from ratings;
-- delete from players;
-- delete from tourneys;
-- delete from games;

-- insert into players(id, name, birth_date, location, gender)
-- values (1, 'Широков', '2000-12-23', 'Северск', true), (2, 'Молодцов', '2012-11-23', 'Томск', true), (3, 'Кривошеин', '1997-04-13', 'Москва', true), (4, 'Горяева', '2000-03-26', 'Пенза', false);
--
-- insert into ratings(id, year, month, week, player_id, rating_in, rating_current, rating_out)
-- values (1, 2024, 1, 1, 1, 300.0, 305.0, 305.0), (2, 2024, 2, 2, 1, 305.0, 310.0, 310.0), (3, 2024, 5, 2, 2, 310.0, 311.2, 311.2),
--        (4, 2024, 5, 3, 2, 311.2, 310.1, 310.1), (5, 2024, 2, 3, 3, 320.0, 324.1, 324.1), (6, 2024, 5, 4, 4, 300.0, 300.3, 300.3);
--
-- insert into competitions(id, title, date, location)
-- values (1, 'Чемпионат города', '2024-05-17', 'Томск'), (2, 'Чемпионат области', '2024-05-30', 'Кемерово'), (3, 'Кубок победы', '2024-05-09', 'Северск');
--
-- insert into tourneys(id, competition_id, title, type, rank, stage)
-- values (1, 1, 'Подгруппы мужчины (абсолютка)', 0, 0, 0)
--      , (2, 1, 'Финал I мужчины (абсолютка)', 0, 0, 1)
--      , (3, 2, 'Подгруппы женщины (юниоры)', 0, 0, 0)
--      , (4, 2, 'Финал I женщины (юниоры)', 1, 0, 1)
--      , (5, 3, 'Финал мужчины (ветераны)', 1, 0, 1);
--
-- ALTER SEQUENCE players_id_seq RESTART WITH 5;
-- ALTER SEQUENCE ratings_id_seq RESTART WITH 7;
-- ALTER SEQUENCE competitions_id_seq RESTART WITH 4;
-- ALTER SEQUENCE tourneys_id_seq RESTART WITH 6;
-- ALTER SEQUENCE games_id_seq RESTART WITH 1;

delete from users where id in (1,2);

insert into users(id, login, password, role) values
(1, 'admin', 'Ytcrf;e', 'EDITOR'),
(2, 'user', 'Crf;bvytult', 'USER');
update users set password =  'Ytcrf;e' where id =1;
update users set password =  'Crf;bvytult' where id =2;