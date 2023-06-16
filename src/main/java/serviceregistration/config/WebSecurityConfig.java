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

import static serviceregistration.constants.SecurityConstants.*;
import static serviceregistration.constants.UserRolesConstants.*;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

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
                      .requestMatchers(DOCTORS_WHITE_LIST.toArray(String[]::new)).permitAll()
                      .requestMatchers(DOCTORSLOTS_WHITE_LIST.toArray(String[]::new)).permitAll()
                      .requestMatchers(CLIENTS_WHITE_LIST.toArray(String[]::new)).permitAll()
                      .requestMatchers(DOCTORS_PERMISSION_LIST_FOR_ADMIN.toArray(String[]::new)).hasRole(ADMIN)
                      .requestMatchers(DOCTORS_PERMISSION_LIST_FOR_DOCTOR.toArray(String[]::new)).hasRole(DOCTOR)
                      .requestMatchers(DOCTORSLOTS_PERMISSION_LIST_FOR_CLIENT_ADMIN.toArray(String[]::new)).hasAnyRole(CLIENT, ADMIN)
                      .requestMatchers(DOCTORSLOTS_PERMISSION_LIST_FOR_ADMIN.toArray(String[]::new)).hasRole(ADMIN)
                      .requestMatchers(DOCTORSLOTS_PERMISSION_LIST_FOR_DOCTOR.toArray(String[]::new)).hasRole(DOCTOR)
                      .requestMatchers(DOCTORSLOTS_PERMISSION_LIST_FOR_CLIENT.toArray(String[]::new)).hasRole(CLIENT)
                      .requestMatchers(CLIENTS_PERMISSION_LIST_FOR_ADMIN.toArray(String[]::new)).hasAnyRole(ADMIN, DOCTOR)
                      .requestMatchers(CLIENTS_PERMISSION_LIST_FOR_CLIENT.toArray(String[]::new)).hasRole(CLIENT)
                      .requestMatchers(CLIENTS_PERMISSION_LIST_FOR_DOCTOR_ADMIN.toArray(String[]::new)).hasAnyRole(DOCTOR, ADMIN)
                      .requestMatchers(REGISTRATIONS_PERMISSION_LIST_FOR_CLIENT.toArray(String[]::new)).hasRole(CLIENT)
                      .requestMatchers(REGISTRATIONS_PERMISSION_LIST_FOR_CLIENT_DOCTOR.toArray(String[]::new)).hasAnyRole(CLIENT,DOCTOR)
                      .requestMatchers(REGISTRATIONS_PERMISSION_LIST_FOR_ADMIN.toArray(String[]::new)).hasRole(ADMIN)
                      .anyRequest().authenticated()
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
