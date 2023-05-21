package serviceregistration.MVC.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.Day;
import serviceregistration.model.Specialization;
import serviceregistration.service.DoctorService;
import serviceregistration.service.DoctorSlotService;
import serviceregistration.service.RegistrationService;
import serviceregistration.service.SpecializationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/registrations")
public class RegistrationMVCController {

    private static LocalDate localDateCurrent;
    private static LocalDate plusDateCurrent;

    private final RegistrationService registrationService;
    private final SpecializationService specializationService;
    private final DoctorSlotService doctorSlotService;
    private final DoctorService doctorService;

    public RegistrationMVCController(RegistrationService registrationService, SpecializationService specializationService, DoctorSlotService doctorSlotService, DoctorService doctorService) {
        this.registrationService = registrationService;
        this.specializationService = specializationService;
        this.doctorSlotService = doctorSlotService;
        this.doctorService = doctorService;
    }

    @GetMapping("/addRegistration")
    public String chooseSpecialization(Model model) {
        List<Specialization> specializations = specializationService.listAll();
        model.addAttribute("specializations", specializations);
        model.addAttribute("specializationsForm", new Specialization());
        return "registrations/chooseSpecialization";
    }

    @PostMapping("/addRegistration")
    public String chooseSpecialization(@ModelAttribute("specialization") Specialization specialization
            , Model model
//            , BindingResult bindingResult
    ) {
//        System.out.println(specialization.getId() + " " + specialization.getTitleSpecialization());
//        System.out.println("******************");

        if(!Objects.isNull(specialization.getTitleSpecialization()) && !Objects.isNull(specialization.getSpecializationDescription())) {
            List<DoctorDTO> doctorDTOList = doctorService.findAllDoctorsBySpecialization(specialization);
            doctorDTOList.forEach(System.out::println);

            // ONLY FOR CURRENT EXAMPLE (plus 100 days)
            localDateCurrent = LocalDate.of(2023, 6, 1);
            plusDateCurrent = localDateCurrent.plusDays(100);
            List<DoctorDTO> doctorDTOS = doctorSlotService.findDoctorDTOBySpecializationIdAndDayBetween(specialization.getId(),
                    localDateCurrent, plusDateCurrent);

//            doctorDTOS.forEach(System.out::println);
            model.addAttribute("doctorDTOList", doctorDTOS);
            model.addAttribute("doctorDTOForm", new DoctorDTO());
            return "registrations/chooseDoctor";
        }

        chooseSpecialization(model);
//        return "redirect:registrations/addRegistration";
        return "registrations/chooseSpecialization";
    }

    @PostMapping("/addRegistrationSecond")
    public String chooseDoctorWork(@RequestParam("doctorDTO") Long doctorDTOId
            , Model model
            , RedirectAttributes redirectAttributes
//            , BindingResult bindingResult
    ) {
//        log.info("in addRegistrationTwo");
        System.out.println(doctorDTOId);
        List<Day> dayList = doctorSlotService.findDaysByDoctorDTOIdAndNotRegisteredAndDateBetween(doctorDTOId,
                localDateCurrent, plusDateCurrent);
        dayList.forEach(s -> System.out.println(s.getDay()));

        model.addAttribute("doctorDTOForm", new DoctorDTO());
        model.addAttribute("doctorWorkDates", dayList);
        return "registrations/chooseDateOfDoctorWork";
    }


    @GetMapping("/listAll")
    public String listAll(Model model) {
        List<RegistrationDTO> registrations = registrationService.listAll();
        model.addAttribute("registrations", registrations);
        return "registrations/listAll";
    }

}
