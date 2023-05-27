package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serviceregistration.model.Client;
import serviceregistration.model.Registration;

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


    Page<Registration> findAllByClientAndIsActive(Client client, Boolean is_active, Pageable pageRequest);

}


