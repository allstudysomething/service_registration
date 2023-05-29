package serviceregistration.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.RoleDTO;
import serviceregistration.mapper.ClientMapper;
import serviceregistration.model.Client;
import serviceregistration.repository.ClientRepository;

import java.time.LocalDate;
import java.time.Period;

@Service
public class ClientService extends GenericService<Client, ClientDTO> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    public ClientService(ClientRepository repository,
                         ClientMapper mapper, BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) {
        super(repository, mapper);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
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

    public ClientDTO create(ClientDTO clientDTO) {
        int age = Period.between(LocalDate.from(clientDTO.getBirthDate()), LocalDate.now()).getYears();
        clientDTO.setAge(age);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        clientDTO.setRole(roleDTO);
        System.out.println("+++++++++++++++++++"+clientDTO.getPassword()+"+++++++++++++++++");
        clientDTO.setPassword(bCryptPasswordEncoder.encode(clientDTO.getPassword()));
        if (userService.findUserByLogin(clientDTO.getLogin()) == null) {
            userService.createUser(clientDTO.getLogin(), clientDTO.getRole().getId());
        }
        return mapper.toDTO(repository.save(mapper.toEntity(clientDTO)));
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

}
