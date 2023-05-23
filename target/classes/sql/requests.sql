select * from doctors_slots;

select * from doctors;

select * from specializations;

select * from days;

select * from slots;

select * from cabinets;

select * from clients;

select * from roles;

select * from registrations;

insert into specializations(description, title)
values ('Врач общей практики', 'Терапевт'),
       ('Хирург', 'Хирург');

insert into roles(description, title)
values
('Стандарт', 'CLIENT'),
('Врач', 'DOCTOR');

insert into cabinets(description, number)
values ('Палата', 1),
       ('Палата', 13);

insert into slots(time_slot)
values
('09:00'),
('10:00'),
('11:00'),
('12:00');

insert into days(day)
values
    ('2023-06-01'),
    ('2023-06-02'),
    ('2023-06-03'),
    ('2023-06-04'),
    ('2023-06-05'),
    ('2023-06-06'),
    ('2023-06-07'),
    ('2023-06-08'),
    ('2023-06-09'),
    ('2023-06-10'),
    ('2023-06-11'),
    ('2023-06-12'),
    ('2023-06-13'),
    ('2023-06-14'),
    ('2023-06-15'),
    ('2023-06-16'),
    ('2023-06-17'),
    ('2023-06-18'),
    ('2023-06-19'),
    ('2023-06-20'),
    ('2023-06-21'),
    ('2023-06-22'),
    ('2023-06-23'),
    ('2023-06-24'),
    ('2023-06-25'),
    ('2023-06-26'),
    ('2023-06-27'),
    ('2023-06-28'),
    ('2023-06-29'),
    ('2023-06-30'),
    ('2023-07-01'),
    ('2023-07-02');

insert into doctors
values (nextval('doctors_seq'), null, now(), null, null, false, 'Петр', 'Иванов', 'PeIvLogin', 'Иванович', 'p', 2, 1),
       (nextval('doctors_seq'), null, now(), null, null, false, 'Семен', 'Семенов', 'SeSeLogin', 'Иванович', 'p', 2, 2),
       (nextval('doctors_seq'), null, now(), null, null, false, 'Иван', 'Петров', 'IvPeLogin', 'Иванович', 'p', 2, 1);

-- insert into clients
-- values (nextval('clients_seq'), now(), null, null, null, null,
--         'Изм проспект', 34, now(), 'as1d2@sf.ru', 'Алексей', 'm', 'Зотов', 'login', 'Владимирович', 'pass', '89031103775', 12345, 1),
--        (nextval('clients_seq'), now(), null, null, null, null,
--         'Изм проспект', 19, now(), 'vs1d2@sf.ru', 'Иван', 'm', 'Иванов', 'login2', 'Владимирович', 'pass', '89131103765', 16345, 1);

insert into clients
values (nextval('clients_seq'), now(), null, null, null, null,
        'Изм проспект', 34, now(), 'as1d2@sf.ru', 'Алексей', 1, 'Зотов', 'login', 'Владимирович', 'pass', '89031103775', '123451234554321', 1),
       (nextval('clients_seq'), now(), null, null, null, null,
        'Изм проспект', 19, now(), 'vs1d2@sf.ru', 'Иван', 1, 'Иванов', 'login2', 'Владимирович', 'pass', '89131103765', '163451634538421', 1);

insert into doctors_slots
select nextval('doctor_slot_seq'), null, now(), null, null, null, false, cabinets.id, days.id, doctors.id, slots.id
from days
    cross join slots
    cross join doctors
    cross join cabinets
where
    days.id = 1
    and
    doctors.id = 1
    and
    cabinets.id = 1;

insert into registrations
values (nextval('registrations_seq'), null, now(), null, null, false, null, false, 1, 2, 1),
       (nextval('registrations_seq'), null, now(), null, null, false, null, false, 1, 2, 3),
       (nextval('registrations_seq'), null, now(), null, null, false, null, true, 1, 1, 4);