alter table Refresh_tokens
    add user bigint unsigned not null;

alter table Refresh_tokens
    add constraint refresh_tokens_users_id_fk
        foreign key (user) references Users (id)
            on delete cascade;