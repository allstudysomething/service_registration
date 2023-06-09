package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import serviceregistration.dto.CustomInterfaces.CustomDoctorSpecializationDay;
import serviceregistration.model.DoctorSlot;

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
                 join doctors d3 on d3.id = doctors_slots.doctor_id
                 where s.id = :specializationId
                   and d2.day between :currentDay and :plusDays
                   and doctors_slots.is_registered = false
                   and d3.is_deleted = false
                order by doctor_id
                   """)
    List<Long> findDoctorIDsBySpecializationAndDayBetween(Long specializationId, LocalDate currentDay, LocalDate plusDays);

    @Query(nativeQuery = true, value = """
        SELECT DISTINCT day_id FROM doctors_slots JOIN doctors d on doctors_slots.doctor_id = d.id
            JOIN days d2 on doctors_slots.day_id = d2.id
            WHERE doctor_id = :doctorDTOId
              AND is_registered = 'false'
              AND d2.day BETWEEN :currentDay AND :plusDays
            order by day_id
            """)
    List<Long> findDaysIdByDoctorDTOIdAndNotRegisteredAndDateBetween(Long doctorDTOId, LocalDate currentDay, LocalDate plusDays);

    @Query(nativeQuery = true, value = """
        select doctors_slots.id from doctors_slots 
        where doctor_id = :doctorDTOId
            and day_id = :dayId
            and slot_id = :slotId """)
    Long findByDoctorIdAndDayIdAndSlotId(Long doctorDTOId, Long dayId, Long slotId);

    // from zotov
//    @Query(nativeQuery = true,
//            value = """
//                    select ds.*
//                    from doctors_slots ds
//                        join days d on ds.day_id = d.id
//                    order by day, doctor_id, slot_id
//                    """)
//    Page<DoctorSlot> findAllSchedule(Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                select ds.doctor_id, ds.day_id, ds.cabinet_id
                from doctors_slots ds
                         join days d on ds.day_id = d.id
                group by ds.doctor_id, ds.day_id, ds.cabinet_id
                order by ds.doctor_id, ds.day_id, ds.cabinet_id
                    """)
    Page<DoctorSlot> findAllSchedule(Pageable pageable);

//    @Query(nativeQuery = true,
//            value = """
//                    select ds.*
//                    from doctors_slots ds
//                        join days d on ds.day_id = d.id
//                    order by day, doctor_id, slot_id
//                    """)
//    Page<DoctorSlot> findAllScheduleWOTimeSlots(Pageable pageable);

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
            join slots s2 on s2.id = ds.slot_id
        where d.last_name ilike '%' || coalesce(:doctorLastName, '%') || '%'
          and d.first_name ilike '%' || coalesce(:doctorFirstName, '%') || '%'
          and d.mid_name ilike '%' || coalesce(:doctorMiddleName, '%') || '%'
          and s.title ilike '%' || coalesce(:titleSpecialization, '%') || '%'
          and d2.day = :doctorSlotDay
        order by d.last_name, d2.day, s2.time_slot
        """)
    List<DoctorSlot> findDoctorSlotByManyNotPaging(@Param(value = "doctorLastName") String doctorLastName,
                                                   @Param(value = "doctorFirstName") String doctorFirstName,
                                                   @Param(value = "doctorMiddleName") String doctorMiddleName,
                                                   @Param(value = "titleSpecialization") String titleSpecialization,
                                                   @Param(value = "doctorSlotDay") LocalDate doctorSlotDay);

    @Query(nativeQuery = true, value = """
        select ds.* from doctors_slots ds
            join doctors d on d.id = ds.doctor_id
            join specializations s on s.id = d.specialization_id
            join days d2 on d2.id = ds.day_id
            join slots s2 on s2.id = ds.slot_id
        where d.last_name ilike '%' || coalesce(:doctorLastName, '%') || '%'
          and d.first_name ilike '%' || coalesce(:doctorFirstName, '%') || '%'
          and d.mid_name ilike '%' || coalesce(:doctorMiddleName, '%') || '%'
          and s.title ilike '%' || coalesce(:titleSpecialization, '%') || '%'
          and d2.day = :doctorSlotDay
        order by d.last_name, d2.day, s2.time_slot
        """)
    Page<DoctorSlot> findDoctorSlotByMany(@Param(value = "doctorLastName") String doctorLastName,
                                          @Param(value = "doctorFirstName") String doctorFirstName,
                                          @Param(value = "doctorMiddleName") String doctorMiddleName,
                                          @Param(value = "titleSpecialization") String titleSpecialization,
                                          @Param(value = "doctorSlotDay") LocalDate doctorSlotDay,
                                          Pageable pageable);

//    @Param(value = "doctorSlotDay") String doctorSlotDay,
//    and cast(d2.day as text) like '%' || coalesce(:doctorSlotDay, '%') || '%'
//    limit 14

    @Query(nativeQuery = true, value = """
        select distinct d2.day as Day, d.first_name as FirstName, d.last_name as LastName, d.mid_name as MidName,
            s.title as TitleSpecialization
        from doctors_slots ds
            join doctors d on d.id = ds.doctor_id
            join days d2 on d2.id = ds.day_id
            join cabinets c on c.id = ds.cabinet_id
            join specializations s on s.id = d.specialization_id
        where d2.day > now() - interval '1 day' and d2.day < now() + interval '10 days'
        order by d2.day, s.title
        """)
    Page<CustomDoctorSpecializationDay> findAllCurrentDays10(Pageable pageable);

    @Query(nativeQuery = true, value = """
        select count(*) from registrations
                                 join doctors_slots ds on ds.id = registrations.doctor_slot_id
                                 join clients c on c.id = registrations.client_id
                                 join days d on d.id = ds.day_id
                                 join slots s on s.id = ds.slot_id
                                 join doctors d2 on ds.doctor_id = d2.id
                                 join specializations s2 on d2.specialization_id = s2.id
        where c.login = :getCurrentUserLogin
            and is_active = true
            and s2.id = :specializationId
            and d.id = :dayIdForFuture
            and registrations.is_active = true
        """)
    Long isActiveRegistrationByClientAndDayIdAndSpecializationId(String getCurrentUserLogin,
                                                                 Long specializationId,
                                                                 Long dayIdForFuture);

    @Query(nativeQuery = true, value = """
        select doctors_slots.* from doctors_slots
                join cabinets c on c.id = doctors_slots.cabinet_id
                join doctors d on d.id = doctors_slots.doctor_id
                join days d2 on d2.id = doctors_slots.day_id
        where d.login = :currentUserLogin
            and d2.day = :localDate
        order by d2.day, doctors_slots.slot_id
        """)
    List<DoctorSlot> getMySchedule(String currentUserLogin, LocalDate localDate);

    @Query(nativeQuery = true, value = """
        select doctors_slots.* from doctors_slots
            join doctors d on d.id = doctors_slots.doctor_id
            join cabinets c on c.id = doctors_slots.cabinet_id
            join days d2 on d2.id = doctors_slots.day_id
            join slots s on s.id = doctors_slots.slot_id
        where cast(d2.day as text) like '%' || coalesce(:registrationDay, '%') || '%'
            and d.login = :getCurrentUserLogin
        order by d2.day desc, s.time_slot
        limit 15
        """)
    List<DoctorSlot> findDoctorSlotByDay(String getCurrentUserLogin, String registrationDay);

    @Query(nativeQuery = true, value = """
        select doctors_slots.* from doctors_slots
            join doctors d on d.id = doctors_slots.doctor_id
            join days d2 on d2.id = doctors_slots.day_id
            join cabinets c on c.id = doctors_slots.cabinet_id
            join slots s on s.id = doctors_slots.slot_id
        where d2.day = timestamp 'today'
        """)
    List<DoctorSlot> findActualScheduleNotPaging();

//    @Query(nativeQuery = true, value = """
//        select d.last_name as DoctorLastName, d.first_name as DoctorFirstName, d.mid_name as DoctorMidName,
//               s2.title as TitleSpecialization, d2.day as Day, c.number as CabinetNumber, d.id as DoctorId, d2.id as DayId
//                 from doctors_slots
//                          join doctors d on d.id = doctors_slots.doctor_id
//                          join slots s on s.id = doctors_slots.slot_id
//                          join cabinets c on c.id = doctors_slots.cabinet_id
//                          join days d2 on d2.id = doctors_slots.day_id
//                        join specializations s2 on d.specialization_id = s2.id
//        where doctors_slots.is_registered = false
//            and d2.day <= TIMESTAMP 'today' + interval '14 days'
//            and d2.day + s.time_slot > (now() at time zone 'utc-3')
//        group by d.last_name, d.first_name, d.mid_name, s2.title, d2.day, c.number, d.id, d2.id
//        order by 5, 6
//        """)
//    Page<MyUniversalQueryModel> getCurrentDaysPlus(Pageable pageable);

//    @Query(nativeQuery = true,
//            value = """
//                    select ds.id as DoctorSlotId, s.time_slot as TimeSlot, ds.is_registered as IsRegistered
//                    from doctors_slots ds
//                        join slots s on s.id = ds.slot_id
//                        join doctors doc on doc.id = ds.doctor_id
//                        join days d on ds.day_id = d.id
//                    where doc.id = :doctorId
//                        and d.id = :dayId
//                        and d.day + s.time_slot > (now() at time zone 'utc-3')
//                        and ds.is_registered = false
//                    order by ds.is_registered desc, s.time_slot
//                    """)
//    List<MyUniversalQueryModel> findSlotsOneDayForClient(Long doctorId, Long dayId);
//
//    @Query(nativeQuery = true,
//            value = """
//                    select c.number
//                    from doctors_slots ds
//                        join cabinets c on c.id = ds.cabinet_id
//                        join doctors doc on doc.id = ds.doctor_id
//                        join days d on d.id = ds.day_id
//                    where doc.id = :doctorId
//                        and d.id = :dayId
//                    """)
//    Integer findCabinetByDoctorIdAndDayId(Long doctorId, Long dayId);

}

