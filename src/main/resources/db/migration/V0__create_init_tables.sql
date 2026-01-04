create table Users
(
    id       int unsigned auto_increment
        primary key,
    email    varchar(50)      not null,
    name     varchar(50)      not null,
    surname  varchar(50)      not null,
    password varchar(255)     not null,
    role_id  tinyint unsigned not null
);

create table Roles
(
    id   tinyint unsigned auto_increment
        primary key,
    role varchar(50) not null
);

create table Rooms
(
    id       int unsigned auto_increment
        primary key,
    name     varchar(50)       not null,
    capacity smallint unsigned null
);