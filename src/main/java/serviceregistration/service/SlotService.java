package serviceregistration.service;

import org.springframework.stereotype.Service;
import serviceregistration.model.Slot;
import serviceregistration.repository.SlotRepository;

import java.sql.SQLOutput;
import java.sql.Time;
import java.util.List;

@Service
public class SlotService {

    private final SlotRepository slotRepository;

    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public List<Slot> listAll() {
        return slotRepository.findAll();
    }

    public List<Slot> getFreeSlotsByDoctorDTOIdAndDayId(Long doctorDTOIdForFuture, Long dayIdForFuture) {
        List<Long> slotIDs = slotRepository.findFreeSlotsByDoctorDTOIdAndDayId(doctorDTOIdForFuture, dayIdForFuture);
//        System.out.println(" *** print slotIDs ***");
//        slotIDs.forEach(s -> System.out.println(s.intValue()));
        return slotRepository.findAllById(slotIDs);
    }
}
