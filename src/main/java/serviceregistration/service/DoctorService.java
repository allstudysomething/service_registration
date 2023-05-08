package serviceregistration.service;

import org.springframework.stereotype.Service;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.RoleDTO;
import serviceregistration.dto.SpecializationDTO;
import serviceregistration.mapper.DoctorMapper;
import serviceregistration.model.Doctor;
import serviceregistration.repository.DoctorRepository;

@Service
public class DoctorService
        extends GenericService<Doctor, DoctorDTO> {

    public DoctorService(DoctorRepository repository,
                         DoctorMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public DoctorDTO create(DoctorDTO newObj) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(3L);
        SpecializationDTO specializationDTO = new SpecializationDTO();
        specializationDTO.setId(1L);
        newObj.setRole(roleDTO);
        newObj.setSpecialization(specializationDTO);
        return mapper.toDTO(repository.save(mapper.toEntity(newObj)));
    }
}
