package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.mapper.DoctorSlotMapper;
import serviceregistration.mapper.GenericMapper;
import serviceregistration.model.Day;
import serviceregistration.model.DoctorSlot;
import serviceregistration.repository.DoctorSlotRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class DoctorSlotService extends GenericService<DoctorSlot, DoctorSlotDTO> {

    private final DoctorService doctorService;
    private final DoctorSlotRepository doctorSlotRepository;
    private final DayService dayService;
    private final DoctorSlotMapper doctorSlotMapper;

    public DoctorSlotService(DoctorSlotRepository doctorSlotRepository,
                             GenericMapper<DoctorSlot, DoctorSlotDTO> mapper, DoctorService doctorService, DayService dayService, DoctorSlotMapper doctorSlotMapper) {
        super(doctorSlotRepository, mapper);
        this.doctorSlotRepository = doctorSlotRepository;
        this.doctorService = doctorService;
        this.dayService = dayService;
        this.doctorSlotMapper = doctorSlotMapper;
    }

    public void addSchedule(Long doctorId, Long dayId, Long cabinetId) {
         doctorSlotRepository.addSchedule(doctorId, dayId, cabinetId);
    }

    public void deleteSchedule(final Long doctorId, final Long dayId) {
        doctorSlotRepository.deleteAllByDoctorIdAndDayId(doctorId, dayId);
    }

    public DoctorSlotDTO getDoctorSlotByCabinetAndDay(final Long cabinetId, final Long dayId) {
        return mapper.toDTO(doctorSlotRepository.findFirstByCabinetIdAndDayId(cabinetId, dayId));
    }

    public DoctorSlotDTO getDoctorSlotByDoctorAndDay(final Long doctorId, final Long dayId) {
        return mapper.toDTO(doctorSlotRepository.findFirstByDoctorIdAndDayId(doctorId, dayId));
    }

    // Потом заменить LocalDate date на LocalDate.Now()
    public List<DoctorDTO> findDoctorDTOBySpecializationIdAndDayBetween(Long specializationId, LocalDate currenTDate
            , LocalDate futureDate) {
        List<Long> doctorIds = doctorSlotRepository.findDoctorIDsBySpecializationAndDayBetween(specializationId,
                currenTDate, futureDate);
        List<DoctorDTO> doctorDTOS = new ArrayList<>();
        doctorIds.forEach(s -> doctorDTOS.add(doctorService.getOne(s)));
//        doctorDTOS.stream().forEach(s -> doctorService.getOne(s))
        return doctorDTOS;
    }

    public List<Day> findDaysByDoctorDTOIdAndNotRegisteredAndDateBetween(Long doctorDTOId,
            LocalDate currenTDate, LocalDate futureDate){
        List<Long> dayIds = doctorSlotRepository.findDaysIdByDoctorDTOIdAndNotRegisteredAndDateBetween(doctorDTOId,
                currenTDate, futureDate);
//        System.out.println("in doctorslotservice");
//        dayIds.forEach(System.out::println);
        List<Day> dayList = new ArrayList<>();
        dayIds.forEach(s -> dayList.add(dayService.getDayById(s)));
//        dayIds.stream().forEach(s -> dayList.add(dayService.getDayById(s)));
//        System.out.println("out doctorslotservice");

        return  dayList;
    }

    public Long getDoctorSlotByDoctorAndDayAndSlot(Long doctorDTOIdForFuture, Long dayIdForFuture, Long slotIdForFuture) {
        return doctorSlotRepository.findByDoctorIdAndDayIdAndSlotId(doctorDTOIdForFuture, dayIdForFuture, slotIdForFuture);
//        System.out.println(doctorSlot.getDoctor() + " : " + doctorSlot.getCabinet() + " " + doctorSlot.getSlot());
//        System.out.println(doctorSlotId);
//        return getOne(doctorSlotId);
    }

    public Page<DoctorSlotDTO> listAllPaging(Pageable pageable) {
//        Page<DoctorSlot> doctorSlotPage = doctorSlotRepository.findAll(pageable);
        Page<DoctorSlot> doctorSlotPage = doctorSlotRepository.findAllSchedule(pageable);
        List<DoctorSlotDTO> result = doctorSlotMapper.toDTOs(doctorSlotPage.getContent());
        return new PageImpl<>(result, pageable, doctorSlotPage.getTotalElements());
    }

    public Page<DoctorSlotDTO> listAllCurrentPaging(PageRequest pageable) {
//        Page<DoctorSlot> doctorSlotPage = doctorSlotRepository.findAll(pageable);
        Page<DoctorSlot> doctorSlotPage = doctorSlotRepository.findActualSchedule(pageable);
        List<DoctorSlotDTO> result = doctorSlotMapper.toDTOs(doctorSlotPage.getContent());
        return new PageImpl<>(result, pageable, doctorSlotPage.getTotalElements());
    }

//    public Page<DoctorSlotDTO> getAllDoctorSlot(Pageable pageable) {
//        Page<DoctorSlot> doctorSlotsPaginated = doctorSlotRepository.findAllNotLessThanToday(pageable);
//        List<DoctorSlotDTO> result = mapper.toDTOs(doctorSlotsPaginated.getContent());
//        return new PageImpl<>(result, pageable, doctorSlotsPaginated.getTotalElements());
//    }

}
