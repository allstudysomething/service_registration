package serviceregistration.dto.CustomInterfaces;

import serviceregistration.dto.RoleDTO;
import serviceregistration.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface MyUniversalQueryModel {

    Long getCabinetId();
    Integer getCabinetNumber();
    String getCabinetDescription();
    Long getClientId();
    String getLogin();
    String getPassword();
    String getPolicy();
    String getClientFirstName();
    String getClientMidName();
    String getClientLastName();
    Gender getGender();
    Integer getAge();
    LocalDate getBirthDate();
    String getPhone();
    String getMail();
    String getAddress();
    String getChangePasswordToken();
    LocalDateTime getChangePasswordTokenExpireDateTime();
    RoleDTO getRole();
    Long getDayId();
    String getDoctorFirstName();
    String getDoctorMidName();
    String getDoctorLastName();
    Long getDoctorSlotId();
    Long getDoctorId();
    Doctor getDoctor();
    LocalDate getDay();
    Slot getSlot();
    Cabinet getCabinet();
    Boolean getIsRegistered();
    Boolean getIsActive();
    Boolean getIsDeleted();
    String getTitleSpecialization();

}
