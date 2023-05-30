package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import serviceregistration.model.Doctor;
import serviceregistration.model.Specialization;

import java.util.List;

public interface DoctorRepository
        extends GenericRepository<Doctor> {

    Doctor findDoctorByLogin(String login);

    List<Doctor> findAllBySpecialization(Specialization specialization);

    @Query(nativeQuery = true, value = """
            select doctors.* from doctors
                join specializations s on s.id = doctors.specialization_id
            where doctors.last_name ilike '%' || coalesce(:doctorLastName, '%') || '%'
          and doctors.first_name ilike '%' || coalesce(:doctorFirstName, '%') || '%'
          and doctors.mid_name ilike '%' || coalesce(:doctorMiddleName, '%') || '%'
          and s.title ilike '%' || coalesce(:titleSpecialization, '%') || '%'
          order by doctors.last_name
            """)
    Page<Doctor> findAllSearched(@Param(value = "doctorLastName") String doctorLastName,
                                 @Param(value = "doctorFirstName") String doctorFirstName,
                                 @Param(value = "doctorMiddleName") String doctorMiddleName,
                                 @Param(value = "titleSpecialization") String titleSpecialization,
                                 Pageable pageable);

}
