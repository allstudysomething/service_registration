package serviceregistration.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.mapper.GenericMapper;
import serviceregistration.model.Cabinet;
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

    public DoctorSlotService(DoctorSlotRepository doctorSlotRepository,
                             GenericMapper<DoctorSlot, DoctorSlotDTO> mapper, DoctorService doctorService) {
        super(doctorSlotRepository, mapper);
        this.doctorSlotRepository = doctorSlotRepository;
        this.doctorService = doctorService;
    }

    public void getSchedule(Long doctorId, Long dayId, Long cabinetId) {
         doctorSlotRepository.addSchedule(doctorId, dayId, cabinetId);
    }

    public DoctorSlotDTO findDoctorSlotByCabinetAndDay(Cabinet cabinet, Day day) {
        return mapper.toDTO(doctorSlotRepository.findFirstByCabinetAndDay(cabinet, day));
    }

    // Потом заменить LocalDate date на LocalDate.Now()
    public List<DoctorDTO> findDoctorDTOBySpecializationIdAndDayBetween(Long specializationId, LocalDate currenTDate
            , LocalDate futureDate) {
        List<Long> doctorDTOIds = doctorSlotRepository.findDoctorIDsBySpecializationAndDayBetween(specializationId,
                currenTDate, futureDate);
        List<DoctorDTO> doctorDTOS = new ArrayList<>();
        doctorDTOIds.stream().forEach(s -> doctorDTOS.add(doctorService.getOne(s)));
//        doctorDTOS.stream().forEach(s -> doctorService.getOne(s))
        return doctorDTOS;
    }


}
