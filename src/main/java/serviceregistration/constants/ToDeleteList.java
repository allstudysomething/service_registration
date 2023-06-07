package serviceregistration.constants;

import serviceregistration.dto.DeletedRegistrationDTO;

import java.util.ArrayList;
import java.util.List;

public interface ToDeleteList {
    public List<DeletedRegistrationDTO> deletedRegistrationsList = new ArrayList<>();
}
