create table users
(
    id       bigint unsigned auto_increment
        primary key,
    email    varchar(255)                              not null unique,
    name     varchar(255)                              not null,
    surname  varchar(255)                              not null,
    password varchar(255)                              not null,
    role     enum ('ADMIN', 'USER', 'MANAGER', 'VIEW') not null
);

create table rooms
(
    id       bigint unsigned auto_increment
        primary key,
    name     varchar(255)      not null,
    capacity smallint unsigned null
);