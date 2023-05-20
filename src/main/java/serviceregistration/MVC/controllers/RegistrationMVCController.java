package serviceregistration.MVC.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.Specialization;
import serviceregistration.service.DoctorService;
import serviceregistration.service.DoctorSlotService;
import serviceregistration.service.RegistrationService;
import serviceregistration.service.SpecializationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/registrations")
public class RegistrationMVCController {

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
    public String chooseDoctor(@ModelAttribute("specializationsForm") Object object
                                , Specialization specialization
                                , Model model
//            , BindingResult bindingResult
    ) {
        if(!Objects.isNull(specialization.getTitleSpecialization()) && !Objects.isNull(specialization.getSpecializationDescription())) {
            List<DoctorDTO> doctorDTOList = doctorService.findAllDoctorsBySpecialization(specialization);
    //        doctorDTOList.forEach(System.out::println);

            LocalDate localDateCurrent = LocalDate.of(2023, 6, 1);
            List<DoctorDTO> doctorDTOS = doctorSlotService.findDoctorDTOBySpecializationIdAndDayBetween(specialization.getId(),
                    localDateCurrent, localDateCurrent.plusDays(10));

//            doctorDTOS.forEach(System.out::println);

            model.addAttribute("doctorDTOList", doctorDTOS);
            model.addAttribute("doctorDTOForm", new DoctorDTO());
            return "registrations/chooseDoctor";
        }

        return "redirect:/registrations/addRegistration";
    }

    @PostMapping("/addRegistrationTwo")
    public String chooseDoctorWork(@ModelAttribute("doctorDTOForm") Object object
//    public String chooseDoctorWork(@ModelAttribute("doctorDTOForm") DoctorDTO doctorDTO
            , DoctorDTO doctorDTO
//            , BindingResult bindingResult
    ) {
        log.info("in addRegistrationTwo");
        System.out.println(doctorDTO);

        return "registrations/chooseDateOfDoctorWork";
    }

    @GetMapping("/listAll")
    public String listAll(Model model) {
        List<RegistrationDTO> registrations = registrationService.listAll();
        model.addAttribute("registrations", registrations);
        return "registrations/listAll";
    }



//    @GetMapping("/addRegistration")
//    public String registrations(Model model) {
//        List<Specialization> specializations = specializationService.listAll();
////        Specialization specialization = new Specialization();
//        model.addAttribute("specializations", specializations);
//        model.addAttribute("specializationsForm", new Specialization());
//        return "registrations/choiceOfSpecialization";
//    }
//
//    @PostMapping("/addRegistration")
//    public String registrations(@ModelAttribute("specializationsForm") Object object
//            , Specialization specialization
//            , Model model
////            , BindingResult bindingResult
//    ) {
//        if(!Objects.isNull(specialization.getTitleSpecialization()) && !Objects.isNull(specialization.getSpecializationDescription())) {
//
//            List<DoctorDTO> doctorDTOList = doctorService.findAllDoctorsBySpecialization(specialization);
//            //        doctorDTOList.forEach(System.out::println);
//
//            LocalDate localDateCurrent = LocalDate.of(2023, 06, 01);
//            List<DoctorDTO> doctorDTOS = doctorSlotService.findDoctorDTOBySpecializationIdAndDayBetween(specialization.getId(),
//                    localDateCurrent, localDateCurrent.plusDays(10));
//
//            doctorDTOS.forEach(System.out::println);
//
//            model.addAttribute("doctorDTOList", doctorDTOS);
//            model.addAttribute("doctorDTOForm", new DoctorDTO());
//            return "registrations/choiceOfDoctor";
//        }
//
//        return "redirect:addRegistration";
//    }
//
//    @PostMapping("/addRegistrationTwo")
//    public String registrations(@ModelAttribute("doctorDTOForm") Object object
//            , DoctorDTO doctorDTO
////            , BindingResult bindingResult
//    ) {
//        System.out.println("in addRegistrationTwo");
//        System.out.println(doctorDTO);
////        System.out.println(doctorDTO.getId());
////        System.out.println(doctorDTO.getFirstName());
////        System.out.println(doctorDTO.getLastName());
////        System.out.println(doctorDTO.getMidName());
//
//        return "registrations/choiceOfDateOfDoctorWork";
//    }
//
//    @GetMapping("/listAll")
//    public String listAll(Model model) {
//        List<RegistrationDTO> registrations = registrationService.listAll();
//        model.addAttribute("registrations", registrations);
//        return "registrations/listAll";
//    }

}
