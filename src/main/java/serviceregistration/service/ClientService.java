package serviceregistration.service;

import org.springframework.stereotype.Service;
import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.RoleDTO;
import serviceregistration.mapper.GenericMapper;
import serviceregistration.model.Client;
import serviceregistration.repository.GenericRepository;

@Service
public class ClientService extends GenericService<Client, ClientDTO> {
    public ClientService(GenericRepository<Client> repository, GenericMapper<Client, ClientDTO> mapper) {
        super(repository, mapper);
    }

    @Override
    public ClientDTO create(ClientDTO newObj) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        newObj.setRole(roleDTO);
        return mapper.toDTO(repository.save(mapper.toEntity(newObj)));
    }

}
