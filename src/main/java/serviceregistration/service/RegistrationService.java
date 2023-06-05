package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.dto.RegistrationSearchAdminDTO;
import serviceregistration.mapper.DoctorSlotMapper;
import serviceregistration.mapper.RegistrationMapper;
import serviceregistration.model.Client;
import serviceregistration.model.DoctorSlot;
import serviceregistration.model.Registration;
import serviceregistration.model.ResultMeet;
import serviceregistration.repository.ClientRepository;
import serviceregistration.repository.RegistrationRepository;
import serviceregistration.repository.UserRepository;
import serviceregistration.service.userdetails.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
public class RegistrationService extends GenericService<Registration, RegistrationDTO> {

    private final DoctorSlotMapper doctorSlotMapper;
    private final DoctorSlotService doctorSlotService;
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public RegistrationService(RegistrationRepository repository,
                               RegistrationMapper mapper, DoctorSlotMapper doctorSlotMapper, DoctorSlotService doctorSlotService,
                               RegistrationRepository registrationRepository, RegistrationMapper registrationMapper,
                               ClientRepository clientRepository,
                               UserRepository userRepository) {
        super(repository, mapper);
        this.doctorSlotMapper = doctorSlotMapper;
        this.doctorSlotService = doctorSlotService;
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    public void addRecord(Long doctorSlotId) {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setCreatedWhen(LocalDateTime.now());

        Long userId = getCurrentUserId();
        Client client = clientRepository.findById(userId).orElseThrow(() -> new NotFoundException("клиент на удивление не найден в базе данных"));
        registrationDTO.setClient(client);

        registrationDTO.setIsActive(true);
        registrationDTO.setResultMeet(ResultMeet.ABSENCE);
        registrationDTO.setDoctorSlot(doctorSlotMapper.toEntity(doctorSlotService.getOne(doctorSlotId)));

        DoctorSlotDTO doctorSlotDTO = doctorSlotService.getOne(doctorSlotId);
        doctorSlotDTO.setIsRegistered(true);

        doctorSlotService.update(doctorSlotDTO);

        registrationRepository.save(registrationMapper.toEntity(registrationDTO));
    }

    public Long getCurrentUserId() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = Long.valueOf(customUserDetails.getUserId());
        return userId;
    }

    public CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Long getCurrentUserRoleId() {
        return userRepository.findRoleByLogin(getCurrentUser().getUsername());
    }

    public List<Registration> listAllCurrent() {
        Client currentClient = clientRepository.findClientById(getCurrentUserId());
//        Client currentClient = clientRepository.findClientByLogin(getCurrentUserId());
//        System.out.println(currentClient.getLogin());
//        System.out.println(currentClient.getPassword());
//        System.out.println(currentClient.getEmail());
        List<Registration> registrationList = registrationRepository.findAllByClientAndIsActiveOrderByDoctorSlotDesc(currentClient, true);
//        List<Registration> registrationList = registrationRepository.findAllByClientAndIsActiveOrderByDayAndSlot(currentClient.getLogin(), true);
//        registrationList.forEach(s -> System.out.println(s.getDoctorSlot()));
        return registrationList;
    }

    public Page<RegistrationDTO> listAllCurrentPagedNotSorted(Pageable pageable) {
//        Client currentClient = clientRepository.findClientById(getCurrentUserId());
//        Page<Registration> registrationPageSorted = registrationRepository.findAllByClient(currentClient, pageable);
        Page<Registration> registrationPageSorted = registrationRepository.findAll(pageable);
//        Page<Registration> registrationPageSorted = registrationRepository.findAllAndOrderByIsActiveBefore(true, pageable);
        List<RegistrationDTO> result = mapper.toDTOs(registrationPageSorted.getContent());
        result.forEach(s -> System.out.println(s.getIsActive()));
        return new PageImpl<>(result, pageable, registrationPageSorted.getTotalElements());
    }

    public Page<RegistrationDTO> listAllCurrentPagedByClient(Pageable pageable) {
        Client currentClient = clientRepository.findClientById(getCurrentUserId());
        Page<Registration> registrationPage = registrationRepository.findAllByClientAndIsActiveAndDateCurrentPlus(currentClient.getLogin(),
                pageable);
        List<RegistrationDTO> result = mapper.toDTOs(registrationPage.getContent());
        return new PageImpl<>(result, pageable, registrationPage.getTotalElements());
    }

    public Page<RegistrationDTO> listArchivePagedByClient(Pageable pageable) {
        Client currentClient = clientRepository.findClientById(getCurrentUserId());
        Page<Registration> registrationPage = registrationRepository.findAllByClientAndNotIsActiveAndDateCurrentMinus(currentClient.getLogin(),
                pageable);
        List<RegistrationDTO> result = mapper.toDTOs(registrationPage.getContent());
        return new PageImpl<>(result, pageable, registrationPage.getTotalElements());
    }

    public Page<RegistrationDTO> findRegistrationByMany(RegistrationSearchAdminDTO registrationSearchAdminDTO, Pageable pageable) {
        String getRegistrationDay = registrationSearchAdminDTO.getRegistrationDay() == null ?
                "%" : registrationSearchAdminDTO.getRegistrationDay().toString();
        Page<Registration> registrationPage =
                registrationRepository.findRegistrationByMany(registrationSearchAdminDTO.getClientLastName(),
                                                            registrationSearchAdminDTO.getClientFirstName(),
                                                            registrationSearchAdminDTO.getClientMiddleName(),
                                                            registrationSearchAdminDTO.getDoctorLastName(),
                                                            registrationSearchAdminDTO.getDoctorFirstName(),
                                                            registrationSearchAdminDTO.getDoctorMiddleName(),
                                                            registrationSearchAdminDTO.getTitleSpecialization(),
                                                            getRegistrationDay,
//                                                            registrationSearchAdminDTO.getRegistrationDay(),
                                                            pageable);
//        registrationPage.getContent().forEach(System.out::println);
        List<RegistrationDTO> result = mapper.toDTOs(registrationPage.getContent());
        return new PageImpl<>(result, pageable, registrationPage.getTotalElements());
    }

    public void safeDelete(List<Registration> registrationList) {
        System.out.println("************ here in safe delete public void safeDelete(List<Registration> registrationList) { ****************");
        registrationList.forEach(s -> safeDelete(3L, s.getId()));

//        registrationList.forEach(s -> {
//            System.out.println(s);
//            System.out.println(doctorSlotService.getOne(getOne(s.getId()).getDoctorSlot().getId()));
//            System.out.println("*******         ***********         **********");});
    }

    public void safeDelete(Long roleId, Long toDeleteId) {
        RegistrationDTO registrationDTO;
        DoctorSlotDTO updatedDoctorSlot;
        if (roleId == 1L || roleId == 3) {
            registrationDTO = getOne(toDeleteId);
            Long doctorSlotId = registrationDTO.getDoctorSlot().getId();
            updatedDoctorSlot = doctorSlotService.getOne(doctorSlotId);
        } else if (roleId == 2L) {
//            System.out.println("****** in roleId == 2L *********");
            updatedDoctorSlot = doctorSlotService.getOne(toDeleteId);
//            System.out.println("updatedDoctorSlot");
//            System.out.println(updatedDoctorSlot);
//            System.out.println();
            Registration registration = registrationRepository.findByDoctorSlot(
                    doctorSlotMapper.toEntity(updatedDoctorSlot));
            if(Objects.isNull(registration)) return;
//            System.out.println("registration");
//            System.out.println(registration);
//            System.out.println();
            registrationDTO = registrationMapper.toDTO(registration);
//            System.out.println("registrationDTO");
//            System.out.println(registrationDTO);
//        } else if(roleId == 3) {
//            registrationDTO =
        } else return;

        updatedDoctorSlot.setIsRegistered(false);
        doctorSlotService.update(updatedDoctorSlot);
        registrationDTO.setIsActive(false);
        String deletedBy = "unknown";
        if (roleId == 1) deletedBy = getCurrentUser().getUsername();
        if (roleId == 3) deletedBy = "scheduler";
        registrationDTO.setDeletedBy(deletedBy);
        registrationDTO.setDeletedWhen(LocalDateTime.now());
        registrationDTO.setResultMeet(ResultMeet.CANCEL);
        registrationRepository.save(mapper.toEntity(registrationDTO));
//        System.out.println("**********   " + getCurrentUserRoleId() + "    **************");
//        return userRepository.findRoleByLogin(getCurrentUser().getUsername());
//        return roleId;
    }

    public List<Registration> getExpiredRegistrations(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return registrationRepository.findExpiredRegistrations(localDateTime);
    }
}
