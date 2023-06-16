package serviceregistration.constants;

import java.util.List;

public interface SecurityConstants {
     List<String> RESOURCES_WHITE_LIST = List.of("/resources/**",
            "/static/**",
            "/js/**",
            "/images/**",
            "/css/**",
            "/",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "clients/change-password/**"
     );

    List<String> DOCTORS_WHITE_LIST = List.of
            (
                    "/doctors/**",
                    "/doctors/search/**"
            );

    List<String> DOCTORS_PERMISSION_LIST_FOR_ADMIN = List.of
            (
                    "/doctors/add",
                    "/doctors/addDoctor",
                    "/doctors/deleteDoctor",
                    "/doctors/deleteSoft/**",
                    "/doctors/restore/**"
            );

    List<String> DOCTORS_PERMISSION_LIST_FOR_DOCTOR = List.of
            (
                    "/doctors/profile/**"
            );

    List<String> DOCTORSLOTS_WHITE_LIST = List.of
            (
                    "/doctorslots/currentDays"
            );

    List<String> DOCTORSLOTS_PERMISSION_LIST_FOR_CLIENT_ADMIN = List.of
            (
                    "/doctorslots/schedule/actual/**"
            );

    List<String> DOCTORSLOTS_PERMISSION_LIST_FOR_CLIENT = List.of
            (
                    "/doctorslots/currentDays",
                    "/doctorslots/makeMeet"
            );

    List<String> DOCTORSLOTS_PERMISSION_LIST_FOR_ADMIN = List.of
            (
                    "/doctorslots",
                    "/doctorslots/search",
                    "/doctorslots/addSchedule",
                    "/doctorslots/deleteSchedule",
                    "/doctorslots/getActualSchedule"
            );

    List<String> DOCTORSLOTS_PERMISSION_LIST_FOR_DOCTOR = List.of
            (
                    "/doctorslots/mySchedule",
                    "/doctorslots/myScheduleSearch"

            );

    List<String> CLIENTS_WHITE_LIST = List.of
            (
                    "/login",
                    "/clients/registration",
                    "/clients/remember-password"
            );

    List<String> CLIENTS_PERMISSION_LIST_FOR_ADMIN = List.of
            (
                    "/clients"
            );

    List<String> CLIENTS_PERMISSION_LIST_FOR_DOCTOR_ADMIN = List.of
            (
                    "/clients/search"
            );

    List<String> CLIENTS_PERMISSION_LIST_FOR_CLIENT = List.of
            (
                    "/clients/profile/**",
                    "/clients/change-password/client",
                    "/clients/change-password/**"
            );

    List<String> REGISTRATIONS_PERMISSION_LIST_FOR_CLIENT = List.of
            (
                    "/registrations/myList",
                    "registrations/myRegistrations",
                    "registrations/myRegistrationsAllTime",
                    "/registrations/addRegistration",
                    "/registrations/addRegistrationSecond",
                    "/registrations/addRegistrationThird",
                    "/registrations/redirectToThird/**",
                    "/registrations/addRegistrationFourth"
            );

    List<String> REGISTRATIONS_PERMISSION_LIST_FOR_CLIENT_DOCTOR = List.of
            (
                    "/registrations/deleteRecord/**"
            );

    List<String> REGISTRATIONS_PERMISSION_LIST_FOR_ADMIN = List.of
            (
                    "/registrations",
                    "registrations/search"
            );
}
