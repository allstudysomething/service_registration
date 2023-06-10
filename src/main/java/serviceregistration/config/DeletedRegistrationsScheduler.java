package serviceregistration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import serviceregistration.constants.MailConstants;
import serviceregistration.constants.ToDeleteList;
import serviceregistration.dto.DeletedRegistrationDTO;
import serviceregistration.service.ClientService;
import serviceregistration.service.DoctorSlotService;
import serviceregistration.service.RegistrationService;
import serviceregistration.utils.MailUtils;

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
//    @Scheduled(cron = "0/15 * * ? * *") // Every 15 seconds. STRONGLY NOT RECOMMEND. FOR TEST ONLY
    public void checkDeletedRegistrations() {
        if(ToDeleteList.deletedRegistrationsList.size() > 0) {
            System.out.println(ToDeleteList.deletedRegistrationsList.size());
            DeletedRegistrationDTO deletedRegistrationDTO = ToDeleteList.deletedRegistrationsList.remove(0);
            System.out.println("deletedRegistrationDTO");
            System.out.println(deletedRegistrationDTO);
            System.out.println();

            sendCancelledMeetEmail(deletedRegistrationDTO);
        }
        // Закомментировать или удалить потом строки 51-53
        else {
            System.out.println();
            System.out.println("DeletedRegistrationsScheduler, method - checkDeletedRegistrations, work at every ***** sec");
        }
    }

    public void sendCancelledMeetEmail(DeletedRegistrationDTO deletedRegistrationDTO) {
        String email = deletedRegistrationDTO.getEmail();
        String doctorFIO = deletedRegistrationDTO.getDoctorFIO();
        String cabinet = deletedRegistrationDTO.getCabinet();
        String day = deletedRegistrationDTO.getDay();
        String time = deletedRegistrationDTO.getTime();
        SimpleMailMessage mailMessage = MailUtils.crateMailMessage(email,
                MailConstants.MAIL_SUBJECT_ABOUT_CANCELLED_RECORD,
                MailConstants.MAIL_MESSAGE_ABOUT_CANCELLED_RECORD_1_1
                        + " " + doctorFIO
                        + ", " + day
                        + " на время " + time
                        + ", кабинет " + cabinet
                        + " - " + MailConstants.MAIL_MESSAGE_ABOUT_CANCELLED_RECORD_1_2);

        System.out.println(mailMessage);
        javaMailSender.send(mailMessage);
        System.out.println("sendCancelledMeetEmail отработал");
        System.out.println();
    }

}
