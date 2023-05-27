package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class SlotDTO {

    private Long id;
    private LocalTime timeSlot;
    private List<Long> doctorSlotsIds;

}
