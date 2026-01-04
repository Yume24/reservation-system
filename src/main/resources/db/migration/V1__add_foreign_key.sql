alter table users
    add constraint users_roles_id_fk
        foreign key (role_id) references roles (id)
            on update cascade;
