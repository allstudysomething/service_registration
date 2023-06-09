package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import serviceregistration.constants.MailConstants;
import serviceregistration.constants.ToDeleteList;
import serviceregistration.dto.*;
import serviceregistration.mapper.DoctorSlotMapper;
import serviceregistration.mapper.RegistrationMapper;
import serviceregistration.model.Client;
import serviceregistration.model.Doctor;
import serviceregistration.model.Registration;
import serviceregistration.model.ResultMeet;
import serviceregistration.repository.ClientRepository;
import serviceregistration.repository.RegistrationRepository;
import serviceregistration.repository.UserRepository;
import serviceregistration.service.userdetails.CustomUserDetails;
import serviceregistration.utils.MailUtils;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
public class RegistrationService extends GenericService<Registration, RegistrationDTO> {

    private final JavaMailSender javaMailSender;
    private final DoctorSlotMapper doctorSlotMapper;
    private final DoctorSlotService doctorSlotService;
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public RegistrationService(RegistrationRepository repository,
                               RegistrationMapper mapper, JavaMailSender javaMailSender, DoctorSlotMapper doctorSlotMapper, DoctorSlotService doctorSlotService,
                               RegistrationRepository registrationRepository, RegistrationMapper registrationMapper,
                               ClientRepository clientRepository,
                               UserRepository userRepository) {
        super(repository, mapper);
        this.javaMailSender = javaMailSender;
        this.doctorSlotMapper = doctorSlotMapper;
        this.doctorSlotService = doctorSlotService;
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    public RegistrationDTO addRecord(Long doctorSlotId) {
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
        return registrationDTO;
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
        List<Registration> registrationList = registrationRepository.findAllByClientAndIsActiveOrderByDoctorSlotDesc(currentClient, true);
        return registrationList;
    }

    public Page<RegistrationDTO> listAllCurrentPagedNotSorted(Pageable pageable) {
//        Page<Registration> registrationPageSorted = registrationRepository.findAll(pageable);
        Page<Registration> registrationPageSorted = registrationRepository.listAllCurrentPagedNotSorted(pageable);
        List<RegistrationDTO> result = mapper.toDTOs(registrationPageSorted.getContent());
//        result.forEach(s -> System.out.println(s.getIsActive()));
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
                                                            pageable);
        List<RegistrationDTO> result = mapper.toDTOs(registrationPage.getContent());
        return new PageImpl<>(result, pageable, registrationPage.getTotalElements());
    }

    public void safeDelete(List<Registration> registrationList, Long roleId) {
        registrationList.forEach(s -> safeDelete(roleId, s.getId()));
    }

    public void safeDelete(Long roleId, Long toDeleteId) {
        RegistrationDTO registrationDTO;
        DoctorSlotDTO updatedDoctorSlot;
        if (roleId == 1L || roleId == 3L || roleId == 4L) {
            registrationDTO = getOne(toDeleteId);
            Long doctorSlotId = registrationDTO.getDoctorSlot().getId();
            updatedDoctorSlot = doctorSlotService.getOne(doctorSlotId);
        } else if (roleId == 2L) {
            updatedDoctorSlot = doctorSlotService.getOne(toDeleteId);
            Registration registration = registrationRepository.findByDoctorSlot(
                    doctorSlotMapper.toEntity(updatedDoctorSlot));
            if(Objects.isNull(registration)) return;
            registrationDTO = registrationMapper.toDTO(registration);
        } else return;

        if(roleId != 3) {
            fillDeletedRegistrationList(registrationDTO);
        }

        updatedDoctorSlot.setIsRegistered(false);
        updatedDoctorSlot.setDeletedWhen(LocalDateTime.now());
        doctorSlotService.update(updatedDoctorSlot);
        registrationDTO.setIsActive(false);
        String deletedBy = "unknown";
        if (roleId == 1L) deletedBy = getCurrentUser().getUsername();
        if (roleId == 3L) deletedBy = "scheduler";
        if (roleId == 4L) deletedBy = "admin";
        registrationDTO.setDeletedBy(deletedBy);
        registrationDTO.setDeletedWhen(LocalDateTime.now());
        registrationDTO.setResultMeet(ResultMeet.CANCEL);
        registrationRepository.save(mapper.toEntity(registrationDTO));
    }

    private void fillDeletedRegistrationList(RegistrationDTO registrationDTO) {
        DeletedRegistrationDTO deletedRegistration = new DeletedRegistrationDTO();
        deletedRegistration.setEmail(registrationDTO.getClient().getEmail());
        deletedRegistration.setDoctorFIO(registrationDTO.getDoctorSlot().getDoctor().getLastName()
                + " " + registrationDTO.getDoctorSlot().getDoctor().getFirstName().charAt(0) + "."
                + registrationDTO.getDoctorSlot().getDoctor().getMidName().charAt(0));
        deletedRegistration.setDay(registrationDTO.getDoctorSlot().getDay().getDay().toString());
        deletedRegistration.setTime(registrationDTO.getDoctorSlot().getSlot().getTimeSlot().toString());
        deletedRegistration.setCabinet(registrationDTO.getDoctorSlot().getCabinet().getCabinetNumber().toString());
        System.out.println(" in fillDeletedRegistrationList ");
        System.out.println(deletedRegistration);
        System.out.println();
        ToDeleteList.deletedRegistrationsList.add(deletedRegistration);
    }

    public void sendAcceptedMeetEmail(RegistrationDTO registrationDTO) {
        String email = registrationDTO.getClient().getEmail();
        String doctorFIO = registrationDTO.getDoctorSlot().getDoctor().getLastName() + " "
                + registrationDTO.getDoctorSlot().getDoctor().getFirstName().charAt(0) + ". "
                + registrationDTO.getDoctorSlot().getDoctor().getMidName().charAt(0) + ".";
        String cabinet = registrationDTO.getDoctorSlot().getCabinet().getCabinetDescription();
        String day = registrationDTO.getDoctorSlot().getDay().getDay().toString();
        String time = registrationDTO.getDoctorSlot().getSlot().getTimeSlot().toString();
        SimpleMailMessage mailMessage = MailUtils.crateMailMessage(email,
                MailConstants.MAIL_SUBJECT_ABOUT_ACCEPTED_RECORD,
                MailConstants.MAIL_MESSAGE_ABOUT_ACCEPTED_RECORD_1_1
                        + " " + doctorFIO
                        + ", " + day
                        + " на время " + time
                        + ", кабинет " + cabinet
                        + " - " + MailConstants.MAIL_MESSAGE_ABOUT_ACCEPTED_RECORD_1_2);

        javaMailSender.send(mailMessage);
    }

    public List<Registration> getExpiredRegistrations(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return registrationRepository.findExpiredRegistrations(localDateTime);
    }

    public List<Registration> findCurrentRegistrationsByDoctorId(Long id) {
        return registrationRepository.findCurrentRegistrationsByDoctorId(id);
    }
}
