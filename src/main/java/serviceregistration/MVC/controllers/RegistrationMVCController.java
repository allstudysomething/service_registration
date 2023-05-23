package serviceregistration.MVC.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.Day;
import serviceregistration.model.Slot;
import serviceregistration.model.Specialization;
import serviceregistration.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Slf4j
@Controller
@RequestMapping("/registrations")
public class RegistrationMVCController {

    private static Specialization specializationForFuture;
    private static Long doctorDTOIdForFuture;
    private static Long dayIdForFuture;

    private static LocalDate localDateCurrent;
    private static LocalDate plusDateCurrent;

    private final RegistrationService registrationService;
    private final SpecializationService specializationService;
    private final DoctorSlotService doctorSlotService;
    private final DoctorService doctorService;
    private SlotService slotService;

    public RegistrationMVCController(RegistrationService registrationService, SpecializationService specializationService, DoctorSlotService doctorSlotService, DoctorService doctorService, SlotService slotService) {
        this.registrationService = registrationService;
        this.specializationService = specializationService;
        this.doctorSlotService = doctorSlotService;
        this.doctorService = doctorService;
        this.slotService = slotService;
    }

    @GetMapping("")
    public String faceWindow() {
        return "registrations/faceWindowClientRegistrations";
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

            // static specialization appropriation
            specializationForFuture = specialization;

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
    public String chooseDoctorWorkDay(@RequestParam("doctorDTO") Long doctorDTOId
            , Model model
            , RedirectAttributes redirectAttributes
//            , BindingResult bindingResult
    ) {
//        log.info("in addRegistrationTwo");
        System.out.println(doctorDTOId);

        // static doctorDTOId appropriation
        doctorDTOIdForFuture = doctorDTOId;

        List<Day> dayList = doctorSlotService.findDaysByDoctorDTOIdAndNotRegisteredAndDateBetween(doctorDTOId,
                localDateCurrent, plusDateCurrent);
        dayList.forEach(s -> System.out.println(s.getDay()));

        model.addAttribute("doctorDTOForm", new DoctorDTO());
        model.addAttribute("doctorWorkDates", dayList);
        return "registrations/chooseDateOfDoctorWork";
    }

    @PostMapping("/addRegistrationThree")
    public String reserveDoctorSlotByClient(@RequestParam("day") Long dayId,
                                            Model model) {
        System.out.println("in addRegistrationThree");
        System.out.println(dayId);

        // static dayId appropriation
        dayIdForFuture = dayId;

//        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long userId = Long.valueOf(customUserDetails.getUserId());
//        System.out.println("userId :" + userId);

        List<Slot> freeTimeSlots = slotService.getFreeSlotsByDoctorDTOIdAndDayId(doctorDTOIdForFuture, dayIdForFuture);
//        freeTimeSlots.forEach(s -> System.out.println(s.getTimeSlot()));
        model.addAttribute("freeTimeSlots", freeTimeSlots);
        return "registrations/chooseTimeOfDayRegistration";

    }

    @GetMapping("/listAll")
    public String listAll(Model model) {
        List<RegistrationDTO> registrations = registrationService.listAll();
        model.addAttribute("registrations", registrations);
        return "registrations/listAll";
    }

    @GetMapping("/myList")
    public String myList(Model model) {
//        List<RegistrationDTO> registrations = registrationService.listAll();
//        model.addAttribute("registrations", registrations);
        return "registrations/myList";
    }

}
