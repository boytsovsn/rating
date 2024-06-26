create table tourney (
            id bigserial not null, 
		    full_name varchar(255), 
		    primary key (id)
);
create table player (
            tourney_id bigint,
			game_id bigint,
			id bigserial not null, 
			title varchar(255), 
			primary key (id)
);
create table game (
			id bigserial not null, 
			name varchar(255), 
			primary key (id)
);
create table rating (
			player_id bigint,
			id bigserial not null, 
			rating_text varchar(255),
			primary key (id)
);
create table users (
			lock boolean, 
			roles smallint check (roles between 0 and 1), 
			id bigserial not null, 
			email varchar(255), 
			firstname varchar(255), 
			lastname varchar(255), 
			login varchar(255), 
			password varchar(255), 
			role varchar(255), 
			surname varchar(255), 
			primary key (id)
);
alter table if exists player add constraint FKklnrv3weler2ftkweewlky958 foreign key (tourney_id) references tourney
alter table if exists player add constraint FKm1t3yvw5i7olwdf32cwuul7ta foreign key (game_id) references game
alter table if exists rating add constraint FK3r3qrq3bm49cg50n7na33qf4l foreign key (player_id) references player