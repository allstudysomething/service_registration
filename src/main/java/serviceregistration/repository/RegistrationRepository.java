package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serviceregistration.model.Client;
import serviceregistration.model.Registration;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistrationRepository
        extends GenericRepository<Registration> {

    List<Registration> findAllByClientAndIsActiveOrderByDoctorSlotDesc(Client client, Boolean is_active);

//    @Query(nativeQuery = true, value = """
//        select d.day, s.time_slot, c2.number, registrations.is_active from registrations join clients c on c.id = registrations.client_id
//        -- select * from registrations join clients c on c.id = registrations.client_id
//                                    join doctors_slots ds on registrations.doctor_slot_id = ds.id
//                                    join cabinets c2 on c2.id = ds.cabinet_id
//                                    join days d on d.id = ds.day_id
//                                    join slots s on s.id = ds.slot_id
//
//        -- where client_id = 5
//        where c.login = :clientLogin
//        order by d.day, s.time_slot """)
//    List<Registration> findAllByClientAndIsActiveOrderByDayAndSlot(String clientLogin, Boolean is_active);

//    Page<Registration> findAllByClientAndIsActive(Client client, Boolean is_active, Pageable pageRequest);
    @Query(nativeQuery = true, value = """
        select registrations.* from registrations
            join clients c on c.id = registrations.client_id
            join doctors_slots ds on ds.id = registrations.doctor_slot_id
            join cabinets c2 on c2.id = ds.cabinet_id
            join days d on d.id = ds.day_id
        where c.login = :clientLogin
            and registrations.is_active = true
            and d.day >= now()
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

//    and d.day < now()
    //        order by d.day

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

//    Page<Registration> findAllAndOrderByIsActiveBefore(Boolean is_active, Pageable pageable);
//    Page<Registration> findAll(Pageable pageable);
}


