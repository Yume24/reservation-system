create table equipment
(
    id   bigint unsigned auto_increment
        primary key,
    name varchar(255) not null,
    constraint equipment_pk_2
        unique (name)
);

create table room_equipment
(
    room_id      bigint unsigned not null,
    equipment_id bigint unsigned not null,
    constraint room_equipment_pk
        primary key (room_id, equipment_id),
    constraint room_equipment_equipment_id_fk
        foreign key (equipment_id) references equipment (id)
            on update cascade on delete cascade,
    constraint room_equipment_rooms_id_fk
        foreign key (room_id) references rooms (id)
            on update cascade on delete cascade
);