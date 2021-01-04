create table airLines
(
    id integer primary key auto_increment,
    name varchar(255) unique not null
);


create table airCrafts
(
    id       integer primary key auto_increment,
    airline_id  integer not null,
    foreign key(airline_id) references airLines,
    model    varchar(255) not null,
    capacity integer      not null
);

create table flights
(
    id               integer primary key auto_increment,
    aircraft_id      integer      not null,
    foreign key (aircraft_id) references airCrafts,
    source_city      varchar(255) not null,
    destination_city varchar(255) not null,
    departure_time   BIGINT       not null,
    arrival_time     BIGINT       not null,
    journey_duration BIGINT       not null,
    price            integer      not null
);


create table passengers
(
    id integer primary key auto_increment,
    name varchar(255) not null,
    age integer not null,
    passport integer not null unique,
    registration varchar(255) not null,
    luggage boolean not null
);

create table tickets
(
    id          integer primary key auto_increment,
    flight_id   integer      not null,
    foreign key (flight_id) references flights,
    passenger_id integer  not null,
    foreign key (passenger_id) references passengers
);