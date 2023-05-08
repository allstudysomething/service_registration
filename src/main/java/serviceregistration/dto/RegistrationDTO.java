package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import serviceregistration.model.ResultMeet;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RegistrationDTO extends GenericDTO {

    private Long doctorSlotId;
    private Long clientId;
    private String complaint;
    private ResultMeet resultMeet;
    private Boolean isActive;

}
