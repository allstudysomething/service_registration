package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(nativeQuery = true, value = """
        SELECT DISTINCT doctor_id FROM doctors_slots JOIN doctors d on doctors_slots.doctor_id = d.id
                 JOIN specializations s on d.specialization_id = s.id
                 JOIN days d2 on d2.id = doctors_slots.day_id
                 where s.id = :specializationId
        --          where s.title = 'Хирург'
                   and d2.day between :currentDay and :plusDays
                   and doctors_slots.is_registered = false""")
    List<Long> findDoctorIDsBySpecializationAndDayBetween(Long specializationId, LocalDate currentDay, LocalDate plusDays);

    @Query(nativeQuery = true, value = """
        SELECT DISTINCT day_id FROM doctors_slots JOIN doctors d on doctors_slots.doctor_id = d.id
            JOIN days d2 on doctors_slots.day_id = d2.id
            WHERE doctor_id = :doctorDTOId
              AND is_registered = 'false'
              AND d2.day BETWEEN :currentDay AND :plusDays """)
    List<Long> findDaysIdByDoctorDTOIdAndNotRegisteredAndDateBetween(Long doctorDTOId, LocalDate currentDay, LocalDate plusDays);

    @Query(nativeQuery = true, value = """
        select doctors_slots.id from doctors_slots 
        where doctor_id = :doctorDTOId
            and day_id = :dayId
            and slot_id = :slotId """)
    Long findByDoctorIdAndDayIdAndSlotId(Long doctorDTOId, Long dayId, Long slotId);

//    @Query(nativeQuery = true,
//            value = """
//                    select ds.*
//                    from doctors_slots ds
//                        join days d on ds.day_id = d.id
//                    where day >= TIMESTAMP 'today'
//                    order by day_id
//                    """)
//    Page<DoctorSlot> findAllNotLessThanToday(Pageable pageable);

//    @Query(nativeQuery = true,
//            value = """
//                    select dc.id as DoctorId, d.id as DayId
//                    from doctors_slots ds
//                        join days d on ds.day_id = d.id
//                        join doctors dc on ds.doctor_id = dc.id
//                    where day > TIMESTAMP 'today' and ds.is_registered = false
//                    group by dc.id, d.id""")
//    List<DoctorDay> groupByDoctorSlot();

    // from zotov
    @Query(nativeQuery = true,
            value = """
                    select ds.*
                    from doctors_slots ds
                        join days d on ds.day_id = d.id
                    order by day, doctor_id, slot_id
                    """)
    Page<DoctorSlot> findAllSchedule(Pageable pageable);

    // from zotov
    @Query(nativeQuery = true,
            value = """
                    select ds.*
                    from doctors_slots ds
                        join days d on ds.day_id = d.id
                    where day >= TIMESTAMP 'today'
                    order by day, doctor_id, slot_id
                    """)
    Page<DoctorSlot> findActualSchedule(PageRequest pageable);

    @Query(nativeQuery = true, value = """
        select ds.* from doctors_slots ds
            join doctors d on d.id = ds.doctor_id
            join specializations s on s.id = d.specialization_id
            join days d2 on d2.id = ds.day_id
        where d.last_name ilike '%' || coalesce(:doctorLastName, '%') || '%'
          and d.first_name ilike '%' || coalesce(:doctorFirstName, '%') || '%'
          and d.mid_name ilike '%' || coalesce(:doctorMiddleName, '%') || '%'
          and s.title ilike '%' || coalesce(:titleSpecialization, '%') || '%'
          and cast(d2.day as text) like '%' || coalesce(:doctorSlotDay, '%') || '%'

        """)
    Page<DoctorSlot> findDoctorSlotByMany(@Param(value = "doctorLastName") String doctorLastName,
                                          @Param(value = "doctorFirstName") String doctorFirstName,
                                          @Param(value = "doctorMiddleName") String doctorMiddleName,
                                          @Param(value = "titleSpecialization") String titleSpecialization,
                                          @Param(value = "doctorSlotDay") String doctorSlotDay,
                                          Pageable pageable);
}
