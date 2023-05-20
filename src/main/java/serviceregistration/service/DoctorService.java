package serviceregistration.service;

import org.springframework.stereotype.Service;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.RoleDTO;
import serviceregistration.mapper.DoctorMapper;
import serviceregistration.model.Doctor;
import serviceregistration.model.Specialization;
import serviceregistration.repository.DoctorRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DoctorService extends GenericService<Doctor, DoctorDTO> {

    public DoctorService(DoctorRepository repository,
                         DoctorMapper mapper) {
        super(repository, mapper);
    }

    public DoctorDTO create(DoctorDTO newObj) {
        newObj.setCreatedWhen(LocalDateTime.now());
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(2L);
        newObj.setRole(roleDTO);
        return mapper.toDTO(repository.save(mapper.toEntity(newObj)));
    }

    public DoctorDTO getDoctorByLogin(String login) {
        return mapper.toDTO(((DoctorRepository)repository).findDoctorByLogin(login));
    }

    public List<DoctorDTO> findAllDoctorsBySpecialization(Specialization specialization){
        return mapper.toDTOs(((DoctorRepository)repository).findAllBySpecialization(specialization));
    }

}
