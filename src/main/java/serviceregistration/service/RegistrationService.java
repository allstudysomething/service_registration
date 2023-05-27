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
import serviceregistration.mapper.DoctorSlotMapper;
import serviceregistration.mapper.RegistrationMapper;
import serviceregistration.model.Client;
import serviceregistration.model.DoctorSlot;
import serviceregistration.model.Registration;
import serviceregistration.model.ResultMeet;
import serviceregistration.repository.ClientRepository;
import serviceregistration.repository.RegistrationRepository;
import serviceregistration.service.userdetails.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class RegistrationService extends GenericService<Registration, RegistrationDTO> {

    private final DoctorSlotMapper doctorSlotMapper;
    private final DoctorSlotService doctorSlotService;
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final ClientRepository clientRepository;

    public RegistrationService(RegistrationRepository repository,
                               RegistrationMapper mapper, DoctorSlotMapper doctorSlotMapper, DoctorSlotService doctorSlotService,
                               RegistrationRepository registrationRepository, RegistrationMapper registrationMapper,
                               ClientRepository clientRepository) {
        super(repository, mapper);
        this.doctorSlotMapper = doctorSlotMapper;
        this.doctorSlotService = doctorSlotService;
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
        this.clientRepository = clientRepository;
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
//        System.out.println("userId :" + userId);
        return userId;
    }

    public CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

    public Page<RegistrationDTO> listAllCurrentPaged(Pageable pageable) {
        Client currentClient = clientRepository.findClientById(getCurrentUserId());
        Page<Registration> registrationPage = registrationRepository.findAllByClientAndIsActive(currentClient,
                true, pageable);
        List<RegistrationDTO> result = mapper.toDTOs(registrationPage.getContent());
        return new PageImpl<>(result, pageable, registrationPage.getTotalElements());
    }
}
