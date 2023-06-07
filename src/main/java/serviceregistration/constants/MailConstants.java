package serviceregistration.constants;

public interface MailConstants {

    String MAIL_MESSAGE_ABOUT_ACCEPTED_RECORD_1_1 = """
         Добрый день. Запись к врачу """;

    String MAIL_MESSAGE_ABOUT_ACCEPTED_RECORD_1_2 = """
           подтверждена. Узнать подробности можно по телефону 8-495-ххххххх""";

    String MAIL_SUBJECT_ABOUT_ACCEPTED_RECORD = """
            Запись на прием к врачу""";

    String MAIL_MESSAGE_FOR_REMEMBER_PASSWORD = """
          Добрый день. Вы получили это письмо, так как с вашего аккаунта была отправлена заявка на восстановление пароля.\n
          Для восстановления пароля перейдите по ссылке: http://localhost:9090/clients/change-password?uuid=""";
    
    String MAIL_SUBJECT_FOR_REMEMBER_PASSWORD = "Восстановление пароля на сайте Сервис регистрации записи к врачу";

    String MAIL_MESSAGE_ABOUT_CANCELLED_RECORD_1_1 = """
         Добрый день. Запись к врачу """;

    String MAIL_MESSAGE_ABOUT_CANCELLED_RECORD_1_2 = """
           отменена. Записаться на прием или узнать подробности можно по телефону 8-495-ххххххх""";

    String MAIL_SUBJECT_ABOUT_CANCELLED_RECORD = """
            Отмена записи на прием к врачу""";
}
