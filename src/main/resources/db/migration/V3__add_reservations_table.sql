create table reservations
(
    id        bigint unsigned auto_increment
        primary key,
    from_date datetime not null,
    to_date   datetime not null,
    user_id   bigint unsigned not null,
    room_id   bigint unsigned not null,
    constraint reservations_rooms_id_fk
        foreign key (room_id) references rooms (id)
            on update cascade on delete cascade,
    constraint reservations_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete cascade
);