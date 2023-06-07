package serviceregistration.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeletedRegistrationDTO {
    private String email;
    private String doctorFIO;
    private String cabinet;
    private String day;
    private String time;
}
