package serviceregistration.dto;

import lombok.*;
import serviceregistration.model.Client;
import serviceregistration.model.Gender;
import serviceregistration.model.Registration;
import serviceregistration.model.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientDTO extends GenericDTO {

    private String login;
    private String password;
    private String policy;
    private String firstName;
    private String midName;
    private String lastName;
    private Gender gender;
    private Integer age;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String address;
    private String changePasswordToken;
    private LocalDateTime changePasswordTokenExpireDateTime;
    private RoleDTO role;
    private List<Long> registrationIds;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.createdWhen = client.getCreatedWhen();
        this.createdBy = client.getCreatedBy();
        this.deletedWhen = client.getDeletedWhen();
        this.deletedBy = client.getDeletedBy();
        this.isDeleted = client.getIsDeleted();
        this.login = client.getLogin();
        this.password = client.getPassword();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.midName = client.getMidName();
        this.gender = client.getGender();
        this.age = client.getAge();
        this.birthDate = client.getBirthDate();
        this.phone = client.getPhone();
        this.email = client.getEmail();
        this.address = client.getAddress();
        this.role.setId(client.getRole().getId());
        this.role.setTitle(client.getRole().getTitle());
        this.role.setDescription(client.getRole().getDescription());
        this.changePasswordToken = client.getChangePasswordToken();
        this.changePasswordTokenExpireDateTime = client.getChangePasswordTokenExpireDateTime();
        List<Registration> registrations = client.getRegistrations();
        List<Long> registrationIds = new ArrayList<>();
        registrations.forEach(reg -> registrationIds.add(reg.getId()));
        this.registrationIds = registrationIds;
    }

}
