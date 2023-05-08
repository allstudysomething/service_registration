insert into roles(description, title)
values
('Client', 'CLIENT'),
('Admin', 'ADMIN'),
('Doctor', 'DOCTOR');

insert into cabinets(description, number)
values
('Кабинет_1', 1),
('Кабинет_2', 2),
('Кабинет_3', 3),
('Кабинет_4', 4);

insert into slots(time_slot)
values
('2023-04-25 09:00'),
('2023-04-25 10:00'),
('2023-04-25 11:00'),
('2023-04-25 12:00');

insert into specializations(description, title)
values
('TERAPEVT', 'Terapevt'),
('LOR', 'Uhogorlonos'),
('OKULIST', 'Okulist'),
('NEVRO', 'Nevropatolog');


--select * from doctors_slots;
--
--select * from doctors;
--
--select * from specializations;
--
--select * from slots;
--
--select * from cabinets;
--
--select * from clients;
--
--select * from roles;
--
--select * from registrations;
--
--insert into specializations(description, title)
--values
--('Врач общей практики', 'Терапевт');
--