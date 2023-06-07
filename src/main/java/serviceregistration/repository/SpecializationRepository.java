package serviceregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.model.Specialization;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    @Query(nativeQuery = true, value = """
        select specializations.* from specializations join doctors d on specializations.id = d.specialization_id
        where d.id = :doctorId
        """)
    Specialization getByDoctorId(Long doctorId);
}
