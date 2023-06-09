package serviceregistration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import serviceregistration.model.Registration;
import serviceregistration.service.ClientService;
import serviceregistration.service.DoctorSlotService;
import serviceregistration.service.RegistrationService;

import java.util.List;

@Slf4j
@Component
public class RegistrationsScheduler {

    private final ClientService clientService;
    private final RegistrationService registrationService;
    private final DoctorSlotService doctorSlotService;
    private final JavaMailSender javaMailSender;

    public RegistrationsScheduler(ClientService clientService, RegistrationService registrationService, DoctorSlotService doctorSlotService, JavaMailSender javaMailSender) {
        this.clientService = clientService;
        this.registrationService = registrationService;
        this.doctorSlotService = doctorSlotService;
        this.javaMailSender = javaMailSender;
    }

//    @Scheduled(cron = "0 0 6 * * ?") // Every day at 6am
//    @Scheduled(cron = "0 */55 * ? * *") // Every 55 minute
//    @Scheduled(cron = "0 * * ? * *") // Every minute. NOT RECOMMEND. FOR TEST ONLY
    public void findExpiredRegistrations() {
        List<Registration> registrationList = registrationService.getExpiredRegistrations();
        registrationService.safeDelete(registrationList, 3L);
    }

}
