package serviceregistration.service;

import org.springframework.stereotype.Service;
import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RoleDTO;
import serviceregistration.mapper.ClientMapper;
import serviceregistration.model.Cabinet;
import serviceregistration.model.Client;
import serviceregistration.model.Day;
import serviceregistration.repository.ClientRepository;

import java.time.LocalDate;
import java.time.Period;

@Service
public class ClientService extends GenericService<Client, ClientDTO> {

    public ClientService(ClientRepository repository,
                         ClientMapper mapper) {
        super(repository, mapper);
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

    public ClientDTO create(ClientDTO newObj) {
        int age = Period.between(LocalDate.from(newObj.getBirthDate()), LocalDate.now()).getYears();
//        System.out.println(ChronoUnit.YEARS.between(localDate, LocalDate.now())); //returns 11
        newObj.setAge(age);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        newObj.setRole(roleDTO);
        return mapper.toDTO(repository.save(mapper.toEntity(newObj)));
    }

}
