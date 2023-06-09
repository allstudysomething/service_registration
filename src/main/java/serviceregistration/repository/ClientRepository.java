package serviceregistration.repository;

import org.springframework.data.jpa.repository.Query;
import serviceregistration.model.Client;

import java.util.List;

public interface ClientRepository
        extends GenericRepository<Client> {

    Client findClientByLogin(String login);
    Client findClientByEmail(String email);
    Client findClientByPhone(String phone);
    Client findClientByPolicy(String policy);
    Client findClientById(Long id);

    Client findUserByChangePasswordToken(String uuid);

    @Query(nativeQuery = true, value = """
        select clients.* from clients
        where clients.last_name ilike '%' || coalesce(:lastName, '%') || '%'
        and clients.first_name ilike '%' || coalesce(:firstName, '%') || '%'
        and clients.mid_name ilike '%' || coalesce(:midName, '%') || '%'
        and clients.policy ilike '%' || coalesce(:policy, '%') || '%'
        """)
    List<Client> listAllSearch(String lastName, String firstName, String midName, String policy);
}
