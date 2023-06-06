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
import java.util.Optional;

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
        Mockito.when(repository.findById(4L)).thenReturn(Optional.of(ClientTestData.CLIENT_4));
        Mockito.when(mapper.toDTO(ClientTestData.CLIENT_4)).thenReturn(ClientTestData.CLIENT_DTO_4);
        ClientDTO clientDTO = service.getOne(4L);
        log.info("Testing getOne() : " + clientDTO);
        assertEquals(ClientTestData.CLIENT_DTO_4, clientDTO);
    }

    @Test
    @Order(3)
    @Override
    protected void create() {
        Mockito.when(repository.save(ClientTestData.CLIENT_2)).thenReturn(ClientTestData.CLIENT_2);
        Mockito.when(mapper.toDTO(ClientTestData.CLIENT_2)).thenReturn(ClientTestData.CLIENT_DTO_2);
        Mockito.when(mapper.toEntity(ClientTestData.CLIENT_DTO_2)).thenReturn(ClientTestData.CLIENT_2);
        ClientDTO clientDTO = service.create(ClientTestData.CLIENT_DTO_2);
        log.info("Testing create : " + clientDTO);
        assertEquals(ClientTestData.CLIENT_DTO_2, clientDTO);
    }

    @Test
    @Order(4)
    @Override
    protected void update() {
//        Mockito.when(repository.save(ClientTestData.CLIENT_2)).thenReturn(ClientTestData.CLIENT_2);
//        Mockito.when(mapper.toDTO(ClientTestData.CLIENT_2)).thenReturn(ClientTestData.CLIENT_DTO_2);
//        Mockito.when(mapper.toEntity(ClientTestData.CLIENT_DTO_2)).thenReturn(ClientTestData.CLIENT_2);
        Mockito.when(service.create(ClientTestData.CLIENT_DTO_2)).thenReturn(ClientTestData.CLIENT_DTO_2);
        ClientDTO clientDTO = service.update(ClientTestData.CLIENT_DTO_2);
        log.info("Testing create : " + clientDTO);
        assertEquals(ClientTestData.CLIENT_DTO_2, clientDTO);
    }

    @Test
    @Order(5)
    @Override
    protected void delete() {
//        Mockito.when(repository.deleteById(2L)).thenReturn(null);
//        Mockito.when(service.delete(2L)).thenReturn(null);
    }
}
