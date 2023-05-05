package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import serviceregistration.model.Role;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ClientDTO extends GenericDTO {

    private String login;
    private String password;
    private Integer policy;
    private String firstName;
    private String lastName;
    private String midName;
    private String gender;
    private Integer age;
    private LocalDateTime birthDate;
    private String phone;
    private String email;
    private String address;
    private Role role;
    private List<Long> registrationIDs;

}
