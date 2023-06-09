package serviceregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.model.Slot;

import java.sql.Time;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    @Query(nativeQuery = true, value = """
    select slots.* from slots join doctors_slots ds on slots.id = ds.slot_id
            left join registrations r on ds.id = r.doctor_slot_id
            join doctors d on d.id = ds.doctor_id
            join days d2 on d2.id = ds.day_id
    --         join cabinets c on c.id = ds.cabinet_id
    where
        ds.doctor_id = :doctorDTOIdForFuture
            AND ds.day_id = :dayIdForFuture
            AND ds.is_registered = false
            AND slots.time_slot not in (select s.time_slot
                              from doctors_slots
                                       join registrations on doctors_slots.id = registrations.doctor_slot_id
                                       join doctors d on d.id = doctors_slots.doctor_id
                                       join days d2 on d2.id = doctors_slots.day_id
                                       join clients c on c.id = registrations.client_id
                                       join cabinets c2 on c2.id = doctors_slots.cabinet_id
                                       join slots s on doctors_slots.slot_id = s.id
                              where
                                     d.id = :doctorDTOIdForFuture
                                     and
                                     d2.id = :dayIdForFuture
                                     and
                                     registrations.is_active = true)                             
        """)
    List<Long> findFreeSlotsByDoctorDTOIdAndDayId(Long doctorDTOIdForFuture, Long dayIdForFuture);


//    String getCurrentUserLogin,

//    and
//    c.login = :getCurrentUserLogin


//    @Query(nativeQuery = true, value = """
//        select slots.id from slots join doctors_slots ds on slots.id = ds.slot_id
//        --                     join doctors d on ds.doctor_id = d.id
//        where ds.doctor_id = :doctorDTOIdForFuture
//                AND ds.day_id = :dayIdForFuture
//                AND ds.is_registered = false;
//        """)
//    List<Long> findFreeSlotsByDoctorDTOIdAndDayId(Long doctorDTOIdForFuture, Long dayIdForFuture);


//    @Query(nativeQuery = true, value = """
//        select slots.id from slots join doctors_slots ds on slots.id = ds.slot_id
//        --                     join doctors d on ds.doctor_id = d.id
//        where ds.doctor_id = :doctorDTOIdForFuture
//                AND ds.day_id = :dayIdForFuture
//                AND ds.is_registered = false;
//        """)
//    List<Long> findFreeSlotsByDoctorDTOIdAndDayId(Long doctorDTOIdForFuture, Long dayIdForFuture);

}
