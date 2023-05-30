package serviceregistration.service.userdetails;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import serviceregistration.constants.UserRolesConstants;
import serviceregistration.model.Userable;
import serviceregistration.repository.ClientRepository;
import serviceregistration.repository.DoctorRepository;
import serviceregistration.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;



@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    @Value("${spring.security.user.name}")
    private String adminUserName;
    @Value("${spring.security.user.password}")
    private String adminPassword;
//    @Value("${spring.security.user.roles}")
//    private String adminRole;

    public CustomUserDetailsService(ClientRepository clientRepository, UserRepository userRepository, DoctorRepository doctorRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(username);
        if (username.equals(adminUserName)) {
            return new CustomUserDetails(null, username, adminPassword,
                    List.of(new SimpleGrantedAuthority("ROLE_" + UserRolesConstants.ADMIN)));
        }
        else {
            return getUserDetails(userRepository.findRoleByLogin(username) == 1
                            ? clientRepository.findClientByLogin(username)
                            : doctorRepository.findDoctorByLogin(username),
                    username);

//            Client client = clientRepository.findClientByLogin(username);
//            System.out.println(client.getLogin() + " ***** " + client.getEmail());
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            //ROLE_CLIENT, ROLE_DOCTOR
//            authorities.add(new SimpleGrantedAuthority(client.getRole().getId() == 1L ? "ROLE_" + UserRolesConstants.CLIENT :
//                                                       "ROLE_" + UserRolesConstants.DOCTOR));
//            return new CustomUserDetails(client.getId().intValue(), username, client.getPassword(), authorities);
        }
    }

    public UserDetails getUserDetails(final Userable user, final String username) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getTitle()));
        return new CustomUserDetails(user.getId().intValue(), username, user.getPassword(), authorities);
    }

}
