package serviceregistration.utils;

import org.springframework.mail.SimpleMailMessage;

public class MailUtils {
    private MailUtils() {
    
    }
    
    public static SimpleMailMessage crateMailMessage(final String email,
                                                     final String subject,
                                                     final String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }
    
    public static SimpleMailMessage crateMailMessage(final String[] emails,
                                                     final String subject,
                                                     final String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emails);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }
}
