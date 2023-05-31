package serviceregistration.dto.CustomInterfaces;

import java.time.LocalDate;

public interface CustomDoctorSpecializationDay extends CustomGenericModel {
    String getFirstName();
    String getLastName();
    String getMidName();
    String getTitleSpecialization();
    LocalDate getDay();
}
