package serviceregistration.service;

import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.RoleDTO;
import serviceregistration.model.Client;
import serviceregistration.model.Gender;
import serviceregistration.model.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface ClientTestData {
    ClientDTO CLIENT_DTO_1 = new ClientDTO(
            "login1",
            "password1",
            "1234",
            "firstName1",
            "midName1",
            "lastName1",
            Gender.MALE,
            31,
            LocalDate.now(),
            "89031111111",
            "email@mail.ru1",
            "address1",
            null,
            null,
            new RoleDTO(1L, "CLIENT", "cliennt"),
            new ArrayList<>()
    );

    ClientDTO CLIENT_DTO_2 = new ClientDTO(
            "login2",
            "password2",
            "2234",
            "firstName2",
            "midName2",
            "lastName2",
            Gender.MALE,
            32,
            LocalDate.now(),
            "890322222222",
            "email@mail.ru2",
            "address2",
            null,
            null,
            new RoleDTO(1L, "CLIENT", "cliennt"),
            new ArrayList<>()
    );

    ClientDTO CLIENT_DTO_3_DELETED = new ClientDTO(
            "login3",
            "password3",
            "3234",
            "firstName3",
            "midName3",
            "lastName3",
            Gender.MALE,
            33,
            LocalDate.now(),
            "8903333333",
            "email@mail.ru3",
            "address3",
            null,
            null,
            new RoleDTO(1L, "CLIENT", "cliennt"),
            new ArrayList<>()
    );

    List<ClientDTO> CLIENT_DTO_LIST = Arrays.asList(CLIENT_DTO_1, CLIENT_DTO_2, CLIENT_DTO_3_DELETED);

    Client CLIENT_1 = new Client(
            "login1",
            "password1",
            "1234",
            "firstName1",
            "midName1",
            "lastName1",
            Gender.MALE,
            31,
            LocalDate.now(),
            "89031111111",
            "email@mail.ru1",
            "address1",
            null,
            null,
            new Role(1L, "CLIENT", "cliennt"),
            new ArrayList<>()
    );

    Client CLIENT_2 = new Client(
            "login2",
            "password2",
            "2234",
            "firstName2",
            "midName2",
            "lastName2",
            Gender.MALE,
            32,
            LocalDate.now(),
            "890322222222",
            "email@mail.ru2",
            "address2",
            null,
            null,
            new Role(1L, "CLIENT", "cliennt"),
            new ArrayList<>()
    );

    Client CLIENT_3 = new Client(
            "login3",
            "password3",
            "3234",
            "firstName3",
            "midName3",
            "lastName3",
            Gender.MALE,
            33,
            LocalDate.now(),
            "8903333333",
            "email@mail.ru3",
            "address3",
            null,
            null,
            new Role(1L, "CLIENT", "cliennt"),
            new ArrayList<>()
    );

    List<Client> CLIENT_LIST = Arrays.asList(CLIENT_1, CLIENT_2, CLIENT_3);
}
