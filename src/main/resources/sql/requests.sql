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
       ('Хирург', 'Хирург'),
       ('Отоларинголог', 'Лор'),
       ('Кардиолог', 'Кардиолог'),
       ('Педиатр', 'Педиатр'),
       ('Офтальмолог', 'Офтальмолог'),
       ('Стоматолог', 'Стоматолог'),
       ('Врач ультразвуковой диагностики', 'УЗИ'),
       ('Диетолог', 'Диетолог');

insert into roles(description, title)
values
('Стандарт', 'CLIENT'),
('Врач', 'DOCTOR');

insert into cabinets(description, number)
values ('Кабинет № 1', 1),
       ('Кабинет № 2', 2),
       ('Кабинет № 3', 3),
       ('Кабинет № 4', 4),
       ('Кабинет № 5', 5),
       ('Кабинет № 6', 6),
       ('Кабинет № 7', 7),
       ('Кабинет № 8', 8),
       ('Кабинет № 9', 9),
       ('Кабинет № 10', 10),
       ('Кабинет № 11', 11),
       ('Кабинет № 12', 12),
       ('Кабинет № 13', 13),
       ('Кабинет № 14', 14),
       ('Кабинет № 15', 15);

insert into slots(time_slot)
values
('09:00'),
('10:00'),
('11:00'),
('12:00');

insert into days(day)
values
    ('2023-05-01'),
    ('2023-05-02'),
    ('2023-05-03'),
    ('2023-05-04'),
    ('2023-05-05'),
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
       (nextval('doctors_seq'), null, now(), null, null, false, 'Иван', 'Петров', 'IvPeLogin', 'Иванович', 'p', 2, 1),
       (nextval('doctors_seq'), null, now(), null, null, false, 'Doctor1Name', 'Doctor1LastName', 'Doctor1Login', 'Doctor1MidName', 'p', 2, 3),
       (nextval('doctors_seq'), null, now(), null, null, false, 'Doctor2Name', 'Doctor2LastName', 'Doctor2Login', 'Doctor2MidName', 'p', 2, 4),
       (nextval('doctors_seq'), null, now(), null, null, false, 'Doctor3Name', 'Doctor3LastName', 'Doctor3Login', 'Doctor3MidName', 'p', 2, 2),
       (nextval('doctors_seq'), null, now(), null, null, false, 'Doctor4Name', 'Doctor4LastName', 'Doctor4Login', 'Doctor4MidName', 'p', 2, 1),
       (nextval('doctors_seq'), null, now(), null, null, false, 'Doctor5Name', 'Doctor5LastName', 'Doctor5Login', 'Doctor5MidName', 'p', 2, 3),
       (nextval('doctors_seq'), null, now(), null, null, false, 'Doctor6Name', 'Doctor6LastName', 'Doctor6Login', 'Doctor6MidName', 'p', 2, 1),
       (nextval('doctors_seq'), null, now(), null, null, false, 'Doctor7Name', 'Doctor7LastName', 'Doctor7Login', 'Doctor7MidName', 'p', 2, 4);

-- insert into clients
-- values (nextval('clients_seq'), now(), null, null, null, null,
--         'Изм проспект', 34, now(), 'as1d2@sf.ru', 'Алексей', 'm', 'Зотов', 'login', 'Владимирович', 'pass', '89031103775', 12345, 1),
--        (nextval('clients_seq'), now(), null, null, null, null,
--         'Изм проспект', 19, now(), 'vs1d2@sf.ru', 'Иван', 'm', 'Иванов', 'login2', 'Владимирович', 'pass', '89131103765', 16345, 1);

insert into clients
values (nextval('clients_seq'), now(), null, null, null, null,
        'Изм проспект', 34, now(), 'as1d2@sf.ru', 'Алексей', 1, 'Зотов', 'login', 'Владимирович', 'pass', '89031103775', '123451234554321', 1),
       (nextval('clients_seq'), now(), null, null, null, null,
        'Изм проспект', 19, now(), 'vs1d2@sf.ru', 'Иван', 1, 'Иванов', 'login2', 'Владимирович', 'pass', '89131103765', '163451634538421', 1),
       (nextval('clients_seq'), now(), null, null, null, null, 'client1address', 45, now(), 'client1@mail.ru', 'client1name',
           1, 'client1lastname', 'client1login', 'client1midname', '$2a$10$yPgfY6CRQTYUiy6bxEc57ON/Kc07AdpuOGLSa33AhbqJhc2Gy.TM2', '89111112277', '1112277', 1),
       (nextval('clients_seq'), now(), null, null, null, null, 'client2address', 21, now(), 'client2@mail.ru', 'client2name',
           1, 'client2lastname', 'client2login', 'client2midname', '$2a$10$yPgfY6CRQTYUiy6bxEc57ON/Kc07AdpuOGLSa33AhbqJhc2Gy.TM2', '89222222277', '2222277', 1),
       (nextval('clients_seq'), now(), null, null, null, null, 'client3address', 33, now(), 'client3@mail.ru', 'client3name',
           1, 'client3lastname', 'client3login', 'client3midname', '$2a$10$yPgfY6CRQTYUiy6bxEc57ON/Kc07AdpuOGLSa33AhbqJhc2Gy.TM2', '89333332277', '3332277', 1),
       (nextval('clients_seq'), now(), null, null, null, null, 'client4address', 68, now(), 'client4@mail.ru', 'client4name',
           1, 'client4lastname', 'client4login', 'client4midname', '$2a$10$yPgfY6CRQTYUiy6bxEc57ON/Kc07AdpuOGLSa33AhbqJhc2Gy.TM2', '89444442277', '4442277', 1),
       (nextval('clients_seq'), now(), null, null, null, null, 'client5address', 14, now(), 'client5@mail.ru', 'client5name',
           1, 'client5lastname', 'client5login', 'client5midname', '$2a$10$yPgfY6CRQTYUiy6bxEc57ON/Kc07AdpuOGLSa33AhbqJhc2Gy.TM2', '89555552277', '5552277', 1),
       (nextval('clients_seq'), now(), null, null, null, null, 'client6address', 27, now(), 'client6@mail.ru', 'client6name',
           1, 'client6lastname', 'client6login', 'client6midname', '$2a$10$yPgfY6CRQTYUiy6bxEc57ON/Kc07AdpuOGLSa33AhbqJhc2Gy.TM2', '8966662277', '6662277', 1),
       (nextval('clients_seq'), now(), null, null, null, null, 'client7address', 31, now(), 'client7@mail.ru', 'client7name',
           1, 'client7lastname', 'client7login', 'client7midname', '$2a$10$yPgfY6CRQTYUiy6bxEc57ON/Kc07AdpuOGLSa33AhbqJhc2Gy.TM2', '89777772277', '7772277', 1);

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

insert into doctors_slots
values (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 15, 1, 1),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 15, 1, 2),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 15, 1, 3),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 15, 1, 4),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 20, 1, 1),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 20, 1, 2),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 20, 1, 3),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 20, 1, 4),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 25, 1, 1),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 25, 1, 2),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 25, 1, 3),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 25, 1, 4),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 16, 2, 1),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 16, 2, 2),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 16, 2, 3),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 16, 2, 4),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 21, 2, 1),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 21, 2, 2),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 21, 2, 3),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 21, 2, 4),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 26, 2, 1),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 26, 2, 2),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 26, 2, 3),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 26, 2, 4),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 1, 2, 1),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 1, 2, 2),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 1, 2, 3),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 1, 1, 2, 4),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 2, 1, 1, 1),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 2, 1, 1, 2),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 2, 1, 1, 3),
       (nextval('doctor_slot_seq'), null, now(), null, null, null, false, 2, 1, 1, 4);

insert into registrations
values (nextval('registrations_seq'), null, now(), null, null, false, null, false, 1, 2, 1),
       (nextval('registrations_seq'), null, now(), null, null, false, null, false, 1, 2, 3),
       (nextval('registrations_seq'), null, now(), null, null, false, null, true, 1, 1, 4);