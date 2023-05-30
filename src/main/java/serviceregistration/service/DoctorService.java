package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSearchAllDTO;
import serviceregistration.dto.RoleDTO;
import serviceregistration.mapper.DoctorMapper;
import serviceregistration.model.Doctor;
import serviceregistration.model.Specialization;
import serviceregistration.repository.DoctorRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DoctorService extends GenericService<Doctor, DoctorDTO> {

    private final UserService userService;
    private final DoctorRepository doctorRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DoctorService(DoctorRepository repository,
                         DoctorMapper mapper,
                         UserService userService, DoctorRepository doctorRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(repository, mapper);
        this.userService = userService;
        this.doctorRepository = doctorRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public DoctorDTO create(DoctorDTO newObj) {
        newObj.setCreatedWhen(LocalDateTime.now());
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(2L);
        newObj.setRole(roleDTO);
        newObj.setPassword(bCryptPasswordEncoder.encode(newObj.getPassword()));
        userService.createUser(newObj.getLogin(), newObj.getRole().getId());
        return mapper.toDTO(repository.save(mapper.toEntity(newObj)));
    }

    public DoctorDTO getDoctorByLogin(String login) {
        return mapper.toDTO(((DoctorRepository)repository).findDoctorByLogin(login));
    }

    public List<DoctorDTO> findAllDoctorsBySpecialization(Specialization specialization){
        return mapper.toDTOs(((DoctorRepository)repository).findAllBySpecialization(specialization));
    }

    public Page<DoctorDTO> listAll(Pageable pageable) {
        Page<Doctor> page = doctorRepository.findAll(pageable);
        List<DoctorDTO> result = mapper.toDTOs(page.getContent());
        return new PageImpl<>(result, pageable, page.getTotalElements());
    }

    public Page<DoctorDTO> listAllSearched(DoctorSearchAllDTO doctorSearchAllDTO, Pageable pageable) {
        Page<Doctor> page = doctorRepository.findAllSearched(doctorSearchAllDTO.getDoctorLastName(),
                                                             doctorSearchAllDTO.getDoctorFirstName(),
                                                             doctorSearchAllDTO.getDoctorMiddleName(),
                                                             doctorSearchAllDTO.getTitleSpecialization(),
                                                             pageable);
        List<DoctorDTO> result = mapper.toDTOs(page.getContent());
        return new PageImpl<>(result, pageable, page.getTotalElements());
    }
}
