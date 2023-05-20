package serviceregistration.repository;

import serviceregistration.model.Doctor;
import serviceregistration.model.Specialization;

import java.util.List;

public interface DoctorRepository
        extends GenericRepository<Doctor> {

    Doctor findDoctorByLogin(String login);

    List<Doctor> findAllBySpecialization(Specialization specialization);

}
