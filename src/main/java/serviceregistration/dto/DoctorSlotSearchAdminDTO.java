package serviceregistration.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class DoctorSlotSearchAdminDTO {
    private String doctorLastName;
    private String doctorFirstName;
    private String doctorMiddleName;
    private String titleSpecialization;
    private LocalDate registrationDay;
}
