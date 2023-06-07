package serviceregistration.constants;

import serviceregistration.model.Registration;

import java.util.ArrayList;
import java.util.List;

public interface ToDeleteList {
    public List<Registration> deletedRegistrationsList = new ArrayList<>();
}
