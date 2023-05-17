package serviceregistration.service;

import org.springframework.stereotype.Service;
import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.mapper.ClientMapper;
import serviceregistration.model.Cabinet;
import serviceregistration.model.Client;
import serviceregistration.model.Day;
import serviceregistration.repository.ClientRepository;

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


//    public ClientDTO create(ClientDTO newObj) {
//        RoleDTO roleDTO = new RoleDTO();
//        roleDTO.setId(2L);
//        newObj.setRole(roleDTO);
//        return mapper.toDTO(repository.save(mapper.toEntity(newObj)));
//    }
}
