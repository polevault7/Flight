insert into airLines(name)
values ( 'S7 Airlines' ),
       ('Aeroflot'),
       ('Russia'),
       ('Azure Air'),
       ('Pobeda'),
       ('North Star'),
       ('North Wind')
;
insert into airCrafts (airline_id, model, capacity)
values (5, 'Boeing 737-800', 120),
       (5, 'Boeing 737-800', 120),
       (5, 'Boeing 737-800', 120),
       (5, 'Boeing 737-800', 120),
       (5, 'Boeing 747-300', 400),
       (6, 'Boeing 747-300', 400),
       (7, 'Boeing 747-300', 400)
;
--
insert into flights
(aircraft_id, source_city, destination_city, departure_time, arrival_time, journey_duration, price)
values (1, 'KAZAN', 'MOSCOW', 1608163200000, 1500000, 100000, 30000),
       (2, 'KAZAN', 'MOSCOW', 1608163200000, 1500000, 10000, 5000),
       (2, 'KAZAN', 'MOSCOW', 1608163200000, 1500000, 50000, 1000),
       (2, 'MOSCOW', 'KAZAN', 1608854400000, 1500000, 50000, 1000),
       (5, 'LOS-ANGELES', 'PARIS', 1608163200000, 1500000, 50000, 1000)
;
insert into passengers (name, age, passport, registration, luggage)
VALUES ('SERGEY', 25, 001, 'Kazan',false),
       ('ANDREW', 30, 002, 'Sochi',true),
       ('ADEL', 28, 003, 'Volgograd',true),
       ('OLESYA', 27, 004, 'Moscow',false),
       ('KATE', 27, 005, 'Sochi',false)
;

insert into tickets (flight_id, passenger_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5)
;





