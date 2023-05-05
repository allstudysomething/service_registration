package serviceregistration.model;

//@Entity
//@Table(name = "roles")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Role {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @Column(name = "title", nullable = false)
//    private String title;
//
//    @Column(name = "description")
//    private String description;
//
//}

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

}