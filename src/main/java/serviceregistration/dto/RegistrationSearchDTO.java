package serviceregistration.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ToString
public class RegistrationSearchDTO {
    public String lastName;
    public String titleSpecialization;
    public LocalDate day;
    public LocalTime slot;
}
