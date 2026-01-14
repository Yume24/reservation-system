alter table refresh_tokens
    add user bigint unsigned not null;

alter table refresh_tokens
    add constraint refresh_tokens_users_id_fk
        foreign key (user) references users (id)
            on delete cascade;