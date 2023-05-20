package serviceregistration.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.model.*;

import java.time.LocalDate;
import java.util.List;

public interface DoctorSlotRepository
        extends GenericRepository<DoctorSlot> {

    @Modifying
    @Query(nativeQuery = true,
            value = """
                    insert into doctors_slots
                    select nextval('doctor_slot_seq'), null, now(), null, null, null, false,cabinets.id, days.id, doctors.id, slots.id
                    from days
                        cross join slots
                        cross join doctors
                        cross join cabinets
                    where
                        doctors.id = :doctorId
                        and
                        days.id = :dayId
                        and
                        cabinets.id = :cabinetId""")
    void addSchedule(Long doctorId, Long dayId, Long cabinetId);

    DoctorSlot findFirstByCabinetAndDay(Cabinet cabinet, Day day);

//    List<DoctorSlot> findByDoctorAndDayBetween(Day currentDay, Day plus30day);
    @Query(nativeQuery = true, value = """
        SELECT DISTINCT doctor_id FROM doctors_slots JOIN doctors d on doctors_slots.doctor_id = d.id
                 JOIN specializations s on d.specialization_id = s.id
                 JOIN days d2 on d2.id = doctors_slots.day_id
                 where s.id = :specializationId
        --          where s.title = 'Хирург'
                   and d2.day between :currentDay AND :plusDays """)
    List<Long> findDoctorIDsBySpecializationAndDayBetween(Long specializationId, LocalDate currentDay, LocalDate plusDays);

}
