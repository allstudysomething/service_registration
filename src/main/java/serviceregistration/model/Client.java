package serviceregistration.model;

import jakarta.persistence.*;
import jakarta.validation.Constraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clients",
        uniqueConstraints =
                {
                        @UniqueConstraint(name = "uniqueLogin", columnNames = "login"),
                        @UniqueConstraint(name = "uniquePolicy", columnNames = "policy")
                }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "clients_seq", allocationSize = 1)
public class Client extends GenericModel implements Userable {

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "policy", nullable = false)
    private String policy;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "mid_name")
    private String midName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "gender", nullable = false)
    @Enumerated
    private Gender gender;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "change_password_token")
    private String changePasswordToken;

    @Column(name = "change_password_token_expire_date")
    private LocalDateTime changePasswordTokenExpireDateTime;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "FK_CLIENTS_ROLES"))
    private Role role;

    @OneToMany(mappedBy = "client")
    private List<Registration> registrations;
}
