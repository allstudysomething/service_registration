package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serviceregistration.model.Client;
import serviceregistration.model.DoctorSlot;
import serviceregistration.model.Registration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistrationRepository
        extends GenericRepository<Registration> {

    List<Registration> findAllByClientAndIsActiveOrderByDoctorSlotDesc(Client client, Boolean is_active);

    @Query(nativeQuery = true, value = """
        select registrations.* from registrations
            join clients c on c.id = registrations.client_id
            join doctors_slots ds on ds.id = registrations.doctor_slot_id
            join cabinets c2 on c2.id = ds.cabinet_id
            join days d on d.id = ds.day_id
        where c.login = :clientLogin
            and registrations.is_active = true
            and d.day >= now() - interval '1 day'
        """)
    Page<Registration> findAllByClientAndIsActiveAndDateCurrentPlus(@Param(value = "clientLogin") String clientLogin,
                                                  Pageable pageRequest);
    @Query(nativeQuery = true, value = """
        select registrations.* from registrations
            join clients c on c.id = registrations.client_id
            join doctors_slots ds on ds.id = registrations.doctor_slot_id
            join cabinets c2 on c2.id = ds.cabinet_id
            join days d on d.id = ds.day_id
            join slots s on s.id = ds.slot_id
        where c.login = :clientLogin
            and registrations.is_active = false
        """)
    Page<Registration> findAllByClientAndNotIsActiveAndDateCurrentMinus(@Param(value = "clientLogin") String clientLogin,
                                                                        Pageable pageRequest);

    @Query(nativeQuery = true, value = """
        select registrations.* from registrations left join doctors_slots ds on registrations.doctor_slot_id = ds.id
                join clients c on c.id = registrations.client_id
                join doctors d on d.id = ds.doctor_id
                join days d2 on d2.id = ds.day_id
                join cabinets c2 on c2.id = ds.cabinet_id
                join specializations s on d.specialization_id = s.id
        where c.last_name ilike '%' || coalesce(:clientLastName, '%') || '%'
            and c.first_name ilike '%' || coalesce(:clientFirstName, '%') || '%'
            and c.mid_name ilike '%' || coalesce(:clientMiddleName, '%') || '%'
            and d.last_name ilike '%' || coalesce(:doctorLastName, '%') || '%'
            and d.first_name ilike '%' || coalesce(:doctorFirstName, '%') || '%'
            and d.mid_name ilike '%' || coalesce(:doctorMiddleName, '%') || '%'
            and s.title ilike '%' || coalesce(:titleSpecialization, '%') || '%'
            and cast(d2.day as text) like '%' || coalesce(:registrationDay, '%') || '%'
        order by registrations.is_active DESC 
            """)
    Page<Registration> findRegistrationByMany(@Param(value = "clientLastName") String clientLastName,
                                              @Param(value = "clientFirstName") String clientFirstName,
                                              @Param(value = "clientMiddleName") String clientMiddleName,
                                              @Param(value = "doctorLastName") String doctorLastName,
                                              @Param(value = "doctorFirstName") String doctorFirstName,
                                              @Param(value = "doctorMiddleName") String doctorMiddleName,
                                              @Param(value = "titleSpecialization") String titleSpecialization,
                                              @Param(value = "registrationDay") String registrationDay,
                                              Pageable pageable);

    Registration findByDoctorSlot(DoctorSlot doctorSlot);

    @Query(nativeQuery = true, value = """
        select registrations.* from registrations join doctors_slots ds on registrations.doctor_slot_id = ds.id
                                    join doctors d on d.id = ds.doctor_id
                                    join clients c on c.id = registrations.client_id
                                    join slots s on s.id = ds.slot_id
                                    join days d2 on d2.id = ds.day_id
        where registrations.is_active = true
            and d2.day <= cast(:localDateTime as date)
            and s.time_slot <= cast(:localDateTime as time)
        """)
    List<Registration> findExpiredRegistrations(LocalDateTime localDateTime);

    @Query(nativeQuery = true, value = """
        select registrations.* from registrations join doctors_slots ds on ds.id = registrations.doctor_slot_id
                join doctors d on d.id = ds.doctor_id
                join clients c on c.id = registrations.client_id
                join cabinets c2 on c2.id = ds.cabinet_id
                join slots s on s.id = ds.slot_id
                join days d2 on d2.id = ds.day_id
        where doctor_id = :id
            and registrations.is_active
            and d2.day > timestamp 'today'
        """)
    List<Registration> findCurrentRegistrationsByDoctorId(Long id);

}


