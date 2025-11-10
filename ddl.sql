create table permission (description varchar(255), name varchar(255) not null, primary key (name)) engine=InnoDB;
create table role (description varchar(255), name varchar(255) not null, primary key (name)) engine=InnoDB;
create table role_permissions (permissions_name varchar(255) not null, role_name varchar(255) not null, primary key (permissions_name, role_name)) engine=InnoDB;
create table users (dob date, first_name varchar(255), id varchar(255) not null, last_name varchar(255), password varchar(255), username varchar(255), primary key (id)) engine=InnoDB;
create table users_roles (roles_name varchar(255) not null, user_id varchar(255) not null, primary key (roles_name, user_id)) engine=InnoDB;
alter table role_permissions add constraint FKf5aljih4mxtdgalvr7xvngfn1 foreign key (permissions_name) references permission (name);
alter table role_permissions add constraint FKcppvu8fk24eqqn6q4hws7ajux foreign key (role_name) references role (name);
alter table users_roles add constraint FK7tacasmhqivyolfjjxseeha5c foreign key (roles_name) references role (name);
alter table users_roles add constraint FK2o0jvgh89lemvvo17cbqvdxaa foreign key (user_id) references users (id);
