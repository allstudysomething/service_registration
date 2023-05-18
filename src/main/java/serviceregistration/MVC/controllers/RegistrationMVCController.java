package serviceregistration.MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.Specialization;
import serviceregistration.service.RegistrationService;
import serviceregistration.service.SpecializationService;

import java.util.List;

@Controller
@RequestMapping("/registrations")
public class RegistrationMVCController {

    private final RegistrationService registrationService;
    private final SpecializationService specializationService;

    public RegistrationMVCController(RegistrationService registrationService, SpecializationService specializationService) {
        this.registrationService = registrationService;
        this.specializationService = specializationService;
    }

    @GetMapping("/addRegistration")
    public String registrations(Model model) {
        List<Specialization> specializations = specializationService.listAll();
//        Specialization specialization = new Specialization();
        model.addAttribute("specializations", specializations);
        model.addAttribute("specializationsForm", new Specialization());
        return "registrations/choiceOfSpecialization";
    }

    @PostMapping("/addRegistration")
    public String registrations(@ModelAttribute("specializationsForm") Object object,
                                Specialization specialization,
                                BindingResult bindingResult) {
        System.out.println(specialization.getTitleSpecialization());
        System.out.println(specialization.getSpecializationDescription());
        return "registrations/choiceOfSpecialization";
    }

    @GetMapping("/listAll")
    public String listAll(Model model) {
        List<RegistrationDTO> registrations = registrationService.listAll();
        model.addAttribute("registrations", registrations);
        return "registrations/listAll";
    }

}
