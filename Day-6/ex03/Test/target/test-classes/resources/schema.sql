drop table if exists Product;
drop table if exists "user";

create table if not exists Product(
                                      id bigint generated by default as identity primary key,
                                      name varchar(256) not null,
                                      price int not null
);


create table if not exists "user"(
                                     id bigint generated by default as identity primary key,
                                     login varchar(256) not null,
                                     password varchar(256) not null,
                                     auth_status boolean not null
);
