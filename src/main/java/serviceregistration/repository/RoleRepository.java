package serviceregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serviceregistration.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
