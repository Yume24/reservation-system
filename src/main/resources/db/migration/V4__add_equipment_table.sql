create table Equipment
(
    id   bigint unsigned auto_increment
        primary key,
    name varchar(255) not null,
    constraint Equipment_pk_2
        unique (name)
);

create table Room_equipment
(
    room_id      bigint unsigned not null,
    equipment_id bigint unsigned not null,
    constraint Room_equipment_pk
        primary key (room_id, equipment_id),
    constraint Room_equipment_Equipment_id_fk
        foreign key (equipment_id) references Equipment (id)
            on update cascade on delete cascade,
    constraint Room_equipment_Rooms_id_fk
        foreign key (room_id) references Rooms (id)
            on update cascade on delete cascade
);