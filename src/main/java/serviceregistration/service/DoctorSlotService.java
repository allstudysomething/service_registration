package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serviceregistration.dto.CustomInterfaces.CustomDoctorSpecializationDay;
import serviceregistration.dto.CustomInterfaces.MyUniversalQueryModel;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.DoctorSlotSearchAdminDTO;
import serviceregistration.mapper.DoctorSlotMapper;
import serviceregistration.mapper.GenericMapper;
import serviceregistration.model.Day;
import serviceregistration.model.DoctorSlot;
import serviceregistration.model.Slot;
import serviceregistration.repository.DoctorSlotRepository;
import serviceregistration.repository.SlotRepository;
import serviceregistration.service.userdetails.CustomUserDetails;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorSlotService extends GenericService<DoctorSlot, DoctorSlotDTO> {
    private static LocalDate localDateCurrent;
    private static LocalDate plusDateCurrent;

    private final DoctorService doctorService;
    private final DoctorSlotRepository doctorSlotRepository;
    private final DayService dayService;
    private final DoctorSlotMapper doctorSlotMapper;
    private final SlotRepository slotRepository;

    public DoctorSlotService(DoctorSlotRepository doctorSlotRepository,
                             GenericMapper<DoctorSlot, DoctorSlotDTO> mapper, DoctorService doctorService, DayService dayService, DoctorSlotMapper doctorSlotMapper, SlotRepository slotRepository) {
        super(doctorSlotRepository, mapper);
        this.doctorSlotRepository = doctorSlotRepository;
        this.doctorService = doctorService;
        this.dayService = dayService;
        this.doctorSlotMapper = doctorSlotMapper;
        this.slotRepository = slotRepository;
    }

    public Long getCurrentUserId() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = Long.valueOf(customUserDetails.getUserId());
        return userId;
    }

    public CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getCurrentUserLogin() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserLogin = customUserDetails.getUsername();
        return currentUserLogin;
    }

    @Transactional
    public void addSchedule(Long doctorId, Long dayId, Long cabinetId) {
         doctorSlotRepository.addSchedule(doctorId, dayId, cabinetId);
    }

    @Transactional
    public void deleteSchedule(final Long doctorId, final Long dayId) {
        doctorSlotRepository.deleteAllByDoctorIdAndDayId(doctorId, dayId);
    }

    public DoctorSlotDTO getDoctorSlotByCabinetAndDay(final Long cabinetId, final Long dayId) {
        return mapper.toDTO(doctorSlotRepository.findFirstByCabinetIdAndDayId(cabinetId, dayId));
    }

//    public DoctorSlotDTO getDoctorSlotByDoctorAndDay(final Long doctorId, final Long dayId) {
    public DoctorSlotDTO getDoctorSlotByDoctorAndDay(final Long doctorId, final Long dayId) {
//        String doctorId =
        return mapper.toDTO(doctorSlotRepository.findFirstByDoctorIdAndDayId(doctorId, dayId));
    }

    // Потом заменить LocalDate date на LocalDate.Now()
    public List<DoctorDTO> findDoctorDTOBySpecializationIdAndDayBetween(Long specializationId) {
        localDateCurrent = LocalDate.now();
        plusDateCurrent = localDateCurrent.plusDays(30);
        List<Long> doctorIds = doctorSlotRepository.findDoctorIDsBySpecializationAndDayBetween(specializationId,
                localDateCurrent, plusDateCurrent);
        List<DoctorDTO> doctorDTOS = new ArrayList<>();
        doctorIds.forEach(s -> doctorDTOS.add(doctorService.getOne(s)));
        return doctorDTOS;
    }

    public List<Day> findDaysByDoctorDTOIdAndNotRegisteredAndDateBetween(Long doctorDTOId){
        localDateCurrent = LocalDate.now();
        plusDateCurrent = localDateCurrent.plusDays(30);
        List<Long> dayIds = doctorSlotRepository.findDaysIdByDoctorDTOIdAndNotRegisteredAndDateBetween(doctorDTOId,
                localDateCurrent, plusDateCurrent);
        List<Day> dayList = new ArrayList<>();
        dayIds.forEach(s -> dayList.add(dayService.getDayById(s)));
        dayList.sort((o1, o2) -> o1.getDay().compareTo(o2.getDay()));
        return  dayList;
    }

    public List<Slot> getFreeSlotsByDoctorDTOIdAndDayId(Long doctorDTOIdForFuture, Long dayIdForFuture) {
        List<Long> slotIDs = slotRepository.findFreeSlotsByDoctorDTOIdAndDayId(
                doctorDTOIdForFuture, dayIdForFuture);
        Day day = dayService.getDayById(dayIdForFuture);
        List<Slot> slotList = slotRepository.findAllById(slotIDs);

        if (!day.getDay().isEqual(LocalDate.now())) {
            return slotList;
        }

        List<Slot> slotListNew = new ArrayList<>();
        for (int i = 0; i < slotList.size(); i++) {
            if(slotList.get(i).getTimeSlot().isAfter(LocalTime.now())) {
                slotListNew.add(slotList.get(i));
            }
        }
        return slotListNew;
    }

    public Long getDoctorSlotByDoctorAndDayAndSlot(Long doctorDTOIdForFuture, Long dayIdForFuture, Long slotIdForFuture) {
        return doctorSlotRepository.findByDoctorIdAndDayIdAndSlotId(doctorDTOIdForFuture, dayIdForFuture, slotIdForFuture);
    }

    public Page<DoctorSlotDTO> listAllPaging(Pageable pageable) {
        Page<DoctorSlot> doctorSlotPage = doctorSlotRepository.findAllSchedule(pageable);
        List<DoctorSlotDTO> result = doctorSlotMapper.toDTOs(doctorSlotPage.getContent());
        return new PageImpl<>(result, pageable, doctorSlotPage.getTotalElements());
    }

    public Page<CustomDoctorSpecializationDay> listCurrentDays10(Pageable pageable) {
        Page<CustomDoctorSpecializationDay> doctorSlotPage = doctorSlotRepository.findAllCurrentDays10(pageable);
        List<CustomDoctorSpecializationDay> result = doctorSlotPage.getContent();
        return new PageImpl<>(result, pageable, doctorSlotPage.getTotalElements());
    }

    public List<DoctorSlotDTO> listAllCurrent() {
        List<DoctorSlot> doctorSlot = doctorSlotRepository.findActualScheduleNotPaging();
        List<DoctorSlotDTO> doctorSlotDTOS = mapper.toDTOs(doctorSlot);
        return doctorSlotDTOS;
    }

    public Page<DoctorSlotDTO> listAllCurrentPaging(PageRequest pageable) {
        Page<DoctorSlot> doctorSlotPage = doctorSlotRepository.findActualSchedule(pageable);
        List<DoctorSlotDTO> result = doctorSlotMapper.toDTOs(doctorSlotPage.getContent());
        return new PageImpl<>(result, pageable, doctorSlotPage.getTotalElements());
    }


    public List<DoctorSlotDTO> findDoctorSlotByManyNotPaging(DoctorSlotSearchAdminDTO doctorSlotSearchAdminDTO) {
        String getDoctorSlotDay = doctorSlotSearchAdminDTO.getRegistrationDay() == null ?
                "%" : doctorSlotSearchAdminDTO.getRegistrationDay().toString();
        List<DoctorSlot> doctorSlots = doctorSlotRepository.findDoctorSlotByManyNotPaging(doctorSlotSearchAdminDTO.getDoctorLastName(),
                doctorSlotSearchAdminDTO.getDoctorFirstName(),
                doctorSlotSearchAdminDTO.getDoctorMiddleName(),
                doctorSlotSearchAdminDTO.getTitleSpecialization(),
                doctorSlotSearchAdminDTO.getRegistrationDay());
        List<DoctorSlotDTO> doctorSlotDTOS = mapper.toDTOs(doctorSlots);
        return doctorSlotDTOS;
    }

    public Page<DoctorSlotDTO> findDoctorSlotByMany(DoctorSlotSearchAdminDTO doctorSlotSearchAdminDTO, Pageable pageable) {
        String getDoctorSlotDay = doctorSlotSearchAdminDTO.getRegistrationDay() == null ?
                "%" : doctorSlotSearchAdminDTO.getRegistrationDay().toString();
        Page<DoctorSlot> doctorSlotPage =
                doctorSlotRepository.findDoctorSlotByMany(doctorSlotSearchAdminDTO.getDoctorLastName(),
                        doctorSlotSearchAdminDTO.getDoctorFirstName(),
                        doctorSlotSearchAdminDTO.getDoctorMiddleName(),
                        doctorSlotSearchAdminDTO.getTitleSpecialization(),
                        doctorSlotSearchAdminDTO.getRegistrationDay(),
//                        getDoctorSlotDay,
                        pageable);
        List<DoctorSlotDTO> result = mapper.toDTOs(doctorSlotPage.getContent());
        return new PageImpl<>(result, pageable, doctorSlotPage.getTotalElements());
    }

    public boolean isActiveRegistrationByClientAndDayIdAndSpecializationId(Long specializationId, Long dayIdForFuture) {
        Long count = doctorSlotRepository.isActiveRegistrationByClientAndDayIdAndSpecializationId(getCurrentUserLogin(), specializationId, dayIdForFuture);
        System.out.println("************ in this day u have meet with doctor **************");
        return count >= 1L;
    }

    // TODO нужно ли расписание архив для доктора? (реализовать при необходимости)
    public List<DoctorSlotDTO> getMySchedule() {
        List<DoctorSlot> doctorSlots = doctorSlotRepository.getMySchedule(getCurrentUserLogin(), LocalDate.now());
        List<DoctorSlotDTO> doctorSlotDTOS = mapper.toDTOs(doctorSlots);
        return doctorSlotDTOS;
    }

    public List<DoctorSlotDTO> getMySearchedSchedule(LocalDate registrationDay) {
        String registrationDayNew = (registrationDay == null) ?
                "%" : registrationDay.toString();
        List<DoctorSlot> doctorSlots = doctorSlotRepository.findDoctorSlotByDay(getCurrentUserLogin(), registrationDayNew);
        List<DoctorSlotDTO> doctorSlotDTOS = mapper.toDTOs(doctorSlots);
        return doctorSlotDTOS;
    }

    public Page<MyUniversalQueryModel> getCurrentDaysPlus(Pageable pageable) {
        Page<MyUniversalQueryModel> myUniversalQueryModels = doctorSlotRepository.getCurrentDaysPlus(pageable);
        return new PageImpl<>(myUniversalQueryModels.getContent(), pageable, myUniversalQueryModels.getTotalElements());
    }
}
