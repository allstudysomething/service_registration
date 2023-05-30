package serviceregistration.dto.CustomInterfaces;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import serviceregistration.model.Cabinet;
import serviceregistration.model.Day;

import java.time.LocalDate;

public interface CustomDoctorSpecializationDay extends CustomGenericModel {
    String getFirstName();
    String getLastName();
    String getMidName();
    String getTitleSpecialization();
    LocalDate getDay();
}
