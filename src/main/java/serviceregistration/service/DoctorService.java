package serviceregistration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSearchAllDTO;
import serviceregistration.dto.RoleDTO;
import serviceregistration.mapper.DoctorMapper;
import serviceregistration.model.Doctor;
import serviceregistration.model.Registration;
import serviceregistration.model.Specialization;
import serviceregistration.repository.DoctorRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DoctorService extends GenericService<Doctor, DoctorDTO> {

    private final UserService userService;

    @Autowired
    public void setRegistrationService(@Lazy RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    private RegistrationService registrationService;
    private final DoctorRepository doctorRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DoctorService(DoctorRepository repository,
                         DoctorMapper mapper,
                         UserService userService,
                         DoctorRepository doctorRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(repository, mapper);
        this.userService = userService;
        this.doctorRepository = doctorRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public DoctorDTO create(DoctorDTO doctorDTO, Boolean encode) {
        doctorDTO.setCreatedWhen(LocalDateTime.now());
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(2L);
        doctorDTO.setRole(roleDTO);
        if(encode) {
            doctorDTO.setPassword(bCryptPasswordEncoder.encode(doctorDTO.getPassword()));
        } else {
            doctorDTO.setPassword(doctorDTO.getPassword());
        }
        if (userService.findUserByLogin(doctorDTO.getLogin()) == null) {
            userService.createUser(doctorDTO.getLogin(), doctorDTO.getRole().getId());
        }
//        newObj.setPassword(bCryptPasswordEncoder.encode(newObj.getPassword()));
//        userService.createUser(newObj.getLogin(), newObj.getRole().getId());
        return mapper.toDTO(repository.save(mapper.toEntity(doctorDTO)));
    }

    public DoctorDTO create(DoctorDTO doctorDTO) {
        doctorDTO = create(doctorDTO, false);
        return doctorDTO;
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

    public void deleteSoft(Long id) {
        System.out.println("in doctorservice deleteSoft");
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor not found here"));
        List<Registration> registrationList = registrationService.findCurrentRegistrationsByDoctorId(doctor.getId());
//        registrationList.forEach(System.out::println);
        registrationService.safeDelete(registrationList, 4L);
        doctor.setIsDeleted(true);
        update(mapper.toDTO(doctor));
//        registrationService.safeDelete();
    }

    public void restore(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor not found here"));
        doctor.setIsDeleted(false);
        update(mapper.toDTO(doctor));
    }
}
