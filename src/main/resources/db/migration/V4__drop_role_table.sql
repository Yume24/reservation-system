alter table users
drop foreign key users_roles_id_fk;

alter table users
    change role_id role varchar(20) not null;

drop index users_roles_id_fk on users;

drop table roles;