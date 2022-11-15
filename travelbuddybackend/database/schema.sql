drop schema if exists travelbuddy;

create schema travelbuddy;

use travelbuddy;

create table users(
    name varchar(128) not null,
    username varchar(128) not null,
    password varchar(256) not null,
    dob date not null,
    image mediumblob not null,
    gender enum('f','m') not null,
    country varchar(128) not null,
    state varchar(128),
    city varchar(128),
    interests varchar(1000) not null,
    introduction varchar(1000) not null,
    role varchar(128) default 'user',
    token varchar(1000),

    primary key(username)
);

create table locals(
    local_id int auto_increment not null,
    location varchar(128) not null,
    activities varchar(128) not null,
    description varchar(1000) not null,

    primary key(local_id),

    username varchar(128) not null,

    constraint fk_local_username
        foreign key(username)
        references users(username)
);

create table trips(
    trip_id int auto_increment not null,
	start date not null,
    end date not null,
    location varchar(128) not null,
    description varchar(1000) not null,
    type varchar(128) not null,
    
    primary key(trip_id),

    username varchar(128) not null,

    constraint fk_trip_username
        foreign key(username)
        references users(username)
);

