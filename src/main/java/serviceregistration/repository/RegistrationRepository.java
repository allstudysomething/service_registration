package serviceregistration.repository;

import org.springframework.stereotype.Repository;
import serviceregistration.model.Registration;

@Repository
public interface RegistrationRepository
        extends GenericRepository<Registration> {
}
