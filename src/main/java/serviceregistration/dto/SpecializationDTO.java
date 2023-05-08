package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class SpecializationDTO extends GenericDTO {

    private Long id;
    private String specialization;
    private String description;

}
