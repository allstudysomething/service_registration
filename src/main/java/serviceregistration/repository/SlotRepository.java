package serviceregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.model.Slot;

import java.sql.Time;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    @Query(nativeQuery = true, value = """
        select slots.id from slots join doctors_slots ds on slots.id = ds.slot_id
        --                     join doctors d on ds.doctor_id = d.id
        where ds.doctor_id = :doctorDTOIdForFuture
                AND ds.day_id = :dayIdForFuture
                AND ds.is_registered = false;
        """)
    List<Long> findFreeSlotsByDoctorDTOIdAndDayId(Long doctorDTOIdForFuture, Long dayIdForFuture);

}
