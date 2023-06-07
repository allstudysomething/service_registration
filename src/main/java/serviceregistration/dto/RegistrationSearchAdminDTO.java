package serviceregistration.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ToString
public class RegistrationSearchAdminDTO {
    private String clientLastName;
    private String clientFirstName;
    private String clientMiddleName;
    private String doctorLastName;
    private String doctorFirstName;
    private String doctorMiddleName;
    private String titleSpecialization;
    private LocalDate registrationDay;
}
