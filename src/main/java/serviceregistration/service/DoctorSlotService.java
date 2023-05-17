package serviceregistration.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.mapper.GenericMapper;
import serviceregistration.model.Cabinet;
import serviceregistration.model.Day;
import serviceregistration.model.DoctorSlot;
import serviceregistration.repository.DoctorSlotRepository;

@Transactional
@Service
public class DoctorSlotService extends GenericService<DoctorSlot, DoctorSlotDTO> {

    private final DoctorSlotRepository doctorSlotRepository;

    public DoctorSlotService(DoctorSlotRepository doctorSlotRepository,
                             GenericMapper<DoctorSlot, DoctorSlotDTO> mapper) {
        super(doctorSlotRepository, mapper);
        this.doctorSlotRepository = doctorSlotRepository;
    }

    public void getSchedule(Long doctorId, Long dayId, Long cabinetId) {
         doctorSlotRepository.addSchedule(doctorId, dayId, cabinetId);
    }

    public DoctorSlotDTO findDoctorSlotByCabinetAndDay(Cabinet cabinet, Day day) {
        return mapper.toDTO(doctorSlotRepository.findFirstByCabinetAndDay(cabinet, day));
    }

//    @Override
//    public DoctorSlotDTO create(DoctorSlotDTO newObj) {
//
//    }
}
