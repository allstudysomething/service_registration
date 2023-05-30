package serviceregistration.dto.CustomInterfaces;

import java.time.LocalDateTime;

public interface CustomGenericModel {
    Long getId();
    LocalDateTime getCreatedWhen();
    String getCreatedBy();
    LocalDateTime getDeletedWhen();
    String getDeletedBy();
    Boolean getIsDeleted();
}
