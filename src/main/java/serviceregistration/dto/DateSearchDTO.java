package serviceregistration.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class DateSearchDTO {
    private LocalDate registrationDay;
}
