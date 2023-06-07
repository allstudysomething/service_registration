package serviceregistration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import serviceregistration.constants.ToDeleteList;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.Registration;
import serviceregistration.service.ClientService;
import serviceregistration.service.DoctorSlotService;
import serviceregistration.service.RegistrationService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class DeletedRegistrationsScheduler {

    private final ClientService clientService;
    private final RegistrationService registrationService;
    private final DoctorSlotService doctorSlotService;
    private final JavaMailSender javaMailSender;

    public DeletedRegistrationsScheduler(ClientService clientService, RegistrationService registrationService, DoctorSlotService doctorSlotService, JavaMailSender javaMailSender) {
        this.clientService = clientService;
        this.registrationService = registrationService;
        this.doctorSlotService = doctorSlotService;
        this.javaMailSender = javaMailSender;
    }

//    @Scheduled(cron = "0 0 6 * * ?") // Every day at 6am
//    @Scheduled(cron = "0 * * ? * *") // Every minute. NOT RECOMMEND. FOR TEST ONLY

    @Scheduled(cron = "0/5 * * ? * *") // Every 5 seconds. STRONGLY NOT RECOMMEND. FOR TEST ONLY
    public void checkDeletedRegistrations() {
        if(ToDeleteList.deletedRegistrationsList.size() > 0) {
            Registration registration = ToDeleteList.deletedRegistrationsList.remove(0);
            System.out.println("************ " + registration + " ************");
            System.out.println(registration);
        } else {
//        List<Registration> registrationList = registrationService.getExpiredRegistrations();
//        System.out.println("************ here list of registrations expired ****************");
//        registrationList.forEach(System.out::println);
//        registrationService.safeDelete(registrationList, 3L);
        System.out.println("work at every 5 sec");
        }
    }

}
