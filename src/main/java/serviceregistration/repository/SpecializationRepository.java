package serviceregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.model.Specialization;

import java.util.List;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    @Query(nativeQuery = true, value = """
        select specializations.* from specializations join doctors d on specializations.id = d.specialization_id
        where d.id = :doctorId
        """)
    Specialization getByDoctorId(Long doctorId);

    @Query(nativeQuery = true, value = """
            select specializations.* 
            from specializations
            order by specializations.title
            """)
    List<Specialization> findAllSorted();
}
