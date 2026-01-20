create table refresh_tokens
(
    id              bigint auto_increment
        primary key,
    token           char(44) not null,
    creation_date   DATETIME not null,
    expiration_date DATETIME not null,
    user_id         bigint unsigned not null,
    constraint refresh_tokens_users_id_fk
        foreign key (user_id) references users (id)
            on delete cascade on update cascade
);

create index refresh_tokens_token_index
    on refresh_tokens (token);
