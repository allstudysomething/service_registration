package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DoctorSlotDTO {

    private Long doctorId;
    private Long slotId;
    private Long cabinetId;
    private List<Long> registrationsId;
    private Boolean isRegistered;

}
