package serviceregistration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import serviceregistration.constants.MailConstants;
import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.RoleDTO;
import serviceregistration.mapper.ClientMapper;
import serviceregistration.model.Client;
import serviceregistration.repository.ClientRepository;
import serviceregistration.utils.MailUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Slf4j
@Service
public class ClientService extends GenericService<Client, ClientDTO> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final JavaMailSender javaMailSender;

    public ClientService(ClientRepository repository,
                         ClientMapper mapper, BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService, JavaMailSender javaMailSender) {
        super(repository, mapper);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.javaMailSender = javaMailSender;
    }
    public ClientDTO getClientByLogin(String login) {
        return mapper.toDTO(((ClientRepository) repository).findClientByLogin(login));
    }

    public ClientDTO getClientByEmail(String email) {
        return mapper.toDTO(((ClientRepository) repository).findClientByEmail(email));
    }

    public ClientDTO getClientByPhone(String phone) {
        return mapper.toDTO(((ClientRepository) repository).findClientByPhone(phone));
    }
    public ClientDTO getClientByPolicy(String policy) {
        return mapper.toDTO(((ClientRepository) repository).findClientByPolicy(policy));
    }

    public ClientDTO create(ClientDTO clientDTO, Boolean encode) {
        int age = Period.between(LocalDate.from(clientDTO.getBirthDate()), LocalDate.now()).getYears();
        clientDTO.setAge(age);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        clientDTO.setRole(roleDTO);
//        System.out.println("+++++++++++++++++++"+clientDTO.getPassword()+"+++++++++++++++++");
        if(encode) {
            clientDTO.setPassword(bCryptPasswordEncoder.encode(clientDTO.getPassword()));
        } else {
            clientDTO.setPassword(clientDTO.getPassword());
        }
        if (userService.findUserByLogin(clientDTO.getLogin()) == null) {
            userService.createUser(clientDTO.getLogin(), clientDTO.getRole().getId());
        }
        return mapper.toDTO(repository.save(mapper.toEntity(clientDTO)));
    }

    public ClientDTO create(ClientDTO clientDTO) {
        clientDTO = create(clientDTO, false);
        return clientDTO;
    }

//    public ClientDTO create(ClientDTO newObj) {
//        int age = Period.between(LocalDate.from(newObj.getBirthDate()), LocalDate.now()).getYears();
////        System.out.println(ChronoUnit.YEARS.between(localDate, LocalDate.now())); //returns 11
//        newObj.setAge(age);
//        RoleDTO roleDTO = new RoleDTO();
//        roleDTO.setId(1L);
//        newObj.setRole(roleDTO);
//        newObj.setPassword(bCryptPasswordEncoder.encode(newObj.getPassword()));
//        userService.createUser(newObj.getLogin(), newObj.getRole().getId());
//        return mapper.toDTO(repository.save(mapper.toEntity(newObj)));
//    }

    public void delete(final Long id) {
        userService.deleteByLogin(getOne(id).getLogin());
        repository.deleteById(id);
    }

    public void changePassword(final String uuid,
                               final String password) {
        ClientDTO clientDTO = mapper.toDTO(((ClientRepository) repository).findUserByChangePasswordToken(uuid));
        clientDTO.setChangePasswordToken(null);
        clientDTO.setPassword(bCryptPasswordEncoder.encode(password));
        update(clientDTO);
    }

    public void sendChangePasswordEmail(final ClientDTO clientDTO) {
        UUID uuid = UUID.randomUUID();
        clientDTO.setChangePasswordToken(uuid.toString());
        log.info(uuid.toString());
        log.info(clientDTO.toString());
        update(clientDTO);
        SimpleMailMessage mailMessage = MailUtils.crateMailMessage(clientDTO.getEmail(),
                MailConstants.MAIL_SUBJECT_FOR_REMEMBER_PASSWORD,
                MailConstants.MAIL_MESSAGE_FOR_REMEMBER_PASSWORD + uuid);

        javaMailSender.send(mailMessage);
    }
}
