package serviceregistration.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import serviceregistration.config.BCryptPasswordConfig;
import serviceregistration.dto.ClientDTO;
import serviceregistration.mapper.ClientMapper;
import serviceregistration.model.Client;
import serviceregistration.repository.ClientRepository;
import serviceregistration.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientServiceTest extends GenericTest<Client, ClientDTO> {

    public ClientServiceTest() {
        super();
        UserService userService = Mockito.mock(UserService.class);
        repository = Mockito.mock(ClientRepository.class);
        mapper = Mockito.mock(ClientMapper.class);
        BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);
        service = new ClientService((ClientRepository) repository, (ClientMapper) mapper, bCryptPasswordEncoder, userService, javaMailSender);
    }

    @Test
    @Order(1)
    @Override
    protected void getAll() {
        Mockito.when(repository.findAll()).thenReturn(ClientTestData.CLIENT_LIST);
        Mockito.when(mapper.toDTOs(ClientTestData.CLIENT_LIST)).thenReturn(ClientTestData.CLIENT_DTO_LIST);
        List<ClientDTO> clientDTOS = service.listAll();
        log.info("Testing getAll: " + clientDTOS);
        assertEquals(ClientTestData.CLIENT_LIST.size(), clientDTOS.size());
    }

    @Test
    @Order(2)
    @Override
    protected void getOne() {
//        Mockito.when(repository.)
    }

    @Test
    @Order(3)
    @Override
    protected void create() {

    }

    @Test
    @Order(4)
    @Override
    protected void update() {

    }

    @Test
    @Order(5)
    @Override
    protected void delete() {

    }
}
