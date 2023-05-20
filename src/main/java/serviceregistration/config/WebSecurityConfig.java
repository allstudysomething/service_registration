package serviceregistration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import serviceregistration.service.userdetails.CustomUserDetailsService;


import java.util.Arrays;
import java.util.List;

import static serviceregistration.constants.UserRolesConstants.ADMIN;
import static serviceregistration.constants.UserRolesConstants.DOCTOR;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    
    private final List<String> RESOURCES_WHITE_LIST = List.of("/resources/**",
                                                              "/static/**",
                                                              "/js/**",
                                                              "/images/**",
                                                              "/css/**",
                                                              "/",
                                                              "/swagger-ui/**",
                                                              "/v3/api-docs/**",
//            "/registrations/addRegistration",
            "/registrations/addRegistration");

    private final List<String> DOCTORSLOTS_WHITE_LIST = List.of("/doctorslots",
                                                                "/doctors");
    private final List<String> DOCTORSLOTS_PERMISSION_LIST = List.of("/doctorslots/addSchedule"
//            , "/doctorslots/update"
//            , "/doctorslots/delete"
                                                                );
    private final List<String> CLIENTS_WHITE_LIST = List.of("/login",
                                                          "/clients/registration",
                                                          "/clients/remember-password");
    public WebSecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder
                        ,CustomUserDetailsService customUserDetailsService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }
    
    //TODO: про файрволл https://docs.spring.io/spring-security/reference/servlet/exploits/firewall.html
    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowedHttpMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        firewall.setAllowBackSlash(true);
        firewall.setAllowUrlEncodedPercent(true);
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowSemicolon(true)
        ;
        return firewall;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().disable()
              .csrf().disable()
              //Настройка http-запросов - кому/куда можно/нельзя
              .authorizeHttpRequests((requests) -> requests
                                           .requestMatchers(RESOURCES_WHITE_LIST.toArray(String[]::new)).permitAll()
                                           .requestMatchers(DOCTORSLOTS_WHITE_LIST.toArray(String[]::new)).permitAll()
                                           .requestMatchers(CLIENTS_WHITE_LIST.toArray(String[]::new)).permitAll()
//                                           .requestMatchers(DOCTORSLOTS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(ADMIN, DOCTOR)
//                                           .anyRequest().authenticated()
                                           .anyRequest().permitAll()
                                    )
              //Настройка для входа в систему
              .formLogin((form) -> form
                               .loginPage("/login")
                               //Перенаправление на главную страницу после успешной авторизации
                               .defaultSuccessUrl("/")
                               .permitAll()
                        )
              .logout((logout) -> logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .permitAll()
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                     );
        
        return http.build();
    }
    
    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
    
}
