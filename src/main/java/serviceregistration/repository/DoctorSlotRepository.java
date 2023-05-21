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

    void deleteAllByDoctorIdAndDayId(Long doctorId, Long dayId);

    DoctorSlot findFirstByCabinetIdAndDayId(Long cabinetId, Long dayId);

    DoctorSlot findFirstByDoctorIdAndDayId(Long doctorId, Long dayId);

//    List<DoctorSlot> findByDoctorAndDayBetween(Day currentDay, Day plus30day);
    @Query(nativeQuery = true, value = """
        SELECT DISTINCT doctor_id FROM doctors_slots JOIN doctors d on doctors_slots.doctor_id = d.id
                 JOIN specializations s on d.specialization_id = s.id
                 JOIN days d2 on d2.id = doctors_slots.day_id
                 where s.id = :specializationId
        --          where s.title = 'Хирург'
                   and d2.day between :currentDay AND :plusDays """)
    List<Long> findDoctorIDsBySpecializationAndDayBetween(Long specializationId, LocalDate currentDay, LocalDate plusDays);

    @Query(nativeQuery = true, value = """
        SELECT DISTINCT day_id FROM doctors_slots JOIN doctors d on doctors_slots.doctor_id = d.id
            JOIN days d2 on doctors_slots.day_id = d2.id
            WHERE doctor_id = :doctorDTOId
              AND is_registered = 'false'
              AND d2.day BETWEEN :currentDay AND :plusDays """)
    List<Long> findDaysIdByDoctorDTOIdAndNotRegisteredAndDateBetween(Long doctorDTOId, LocalDate currentDay, LocalDate plusDays);

}
