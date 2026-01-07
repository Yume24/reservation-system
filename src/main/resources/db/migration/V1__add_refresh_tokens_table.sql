create table Refresh_tokens
(
    id              bigint auto_increment
        primary key,
    token           varchar(64) not null,
    creation_date   DATETIME    not null,
    expiration_date DATETIME    not null
);

create index Refresh_tokens_token_index
    on Refresh_tokens (token);
