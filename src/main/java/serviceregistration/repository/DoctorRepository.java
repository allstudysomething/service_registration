package serviceregistration.repository;

import serviceregistration.model.Doctor;
import serviceregistration.model.Specialization;

import java.util.List;

public interface DoctorRepository
        extends GenericRepository<Doctor> {

    List<Doctor> findAllBySpecialization(Specialization specialization);

}
