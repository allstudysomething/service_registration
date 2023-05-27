package serviceregistration.MVC.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.*;
import serviceregistration.service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static serviceregistration.constants.UserRolesConstants.ADMIN;


@Slf4j
@Controller
@RequestMapping("/registrations")
public class RegistrationMVCController {

    private static Specialization specializationForFuture;
    private static Long doctorDTOIdForFuture;
    private static Long dayIdForFuture;
    private static Long slotIdForFuture;

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

//    @GetMapping("")
//    public String faceWindow() {
//
//        return "registrations/faceWindowClientRegistrations";
//    }

    @GetMapping("/addRegistration")
    public String chooseSpecialization(Model model) {
        specializationForFuture = null;
        doctorDTOIdForFuture = null;
        dayIdForFuture = null;
        slotIdForFuture = null;

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
//        if(!Objects.isNull(specialization.getTitleSpecialization()) && !Objects.isNull(specialization.getSpecializationDescription())) {

            // static specialization appropriation
            specializationForFuture = specialization;

        return "redirect:/registrations/addRegistrationSecond";
    }

    @GetMapping("/addRegistrationSecond")
    public String chooseDoctorWorkDay(Model model) {
//        List<DoctorDTO> doctorDTOList = doctorService.findAllDoctorsBySpecialization(specializationForFuture);
//        doctorDTOList.forEach(System.out::println);

        // ONLY FOR CURRENT EXAMPLE (plus 60 days)
//        localDateCurrent = LocalDate.of(2023, 6, 1);
        localDateCurrent = LocalDate.now();
        plusDateCurrent = localDateCurrent.plusDays(60);
        List<DoctorDTO> doctorDTOS = doctorSlotService.findDoctorDTOBySpecializationIdAndDayBetween(specializationForFuture.getId(),
                localDateCurrent, plusDateCurrent);

//            doctorDTOS.forEach(System.out::println);
        model.addAttribute("doctorDTOList", doctorDTOS);
        model.addAttribute("doctorDTOForm", new DoctorDTO());
        return "registrations/chooseDoctor";
    }

    @PostMapping("/addRegistrationSecond")
    public String chooseDoctorWorkDay(@RequestParam("doctorDTO") Long doctorDTOId
            , Model model
//            , BindingResult bindingResult
    ) {
//        log.info("in addRegistrationTwo");
        System.out.println("doctorDTOId : " + doctorDTOId);

        // static doctorDTOId appropriation
        doctorDTOIdForFuture = doctorDTOId;

        return "redirect:/registrations/addRegistrationThree";
    }

    @GetMapping("/addRegistrationThree")
    public String reserveDoctorSlotByClient(Model model) {
        List<Day> dayList = doctorSlotService.findDaysByDoctorDTOIdAndNotRegisteredAndDateBetween(doctorDTOIdForFuture,
                localDateCurrent, plusDateCurrent);
//        dayList.forEach(s -> System.out.println(s.getDay()));

        model.addAttribute("doctorDTOForm", new DoctorDTO());
        model.addAttribute("doctorWorkDates", dayList);
        return "registrations/chooseDateOfDoctorWork";
    }

    @PostMapping("/addRegistrationThree")
    public String reserveDoctorSlotByClient(@RequestParam("day") Long dayId,
                                            Model model) {
        System.out.println("in addRegistrationThree");
        System.out.println("dayId : " + dayId);

        // static dayId appropriation
        dayIdForFuture = dayId;
//        doctorSlotService.

        return "redirect:/registrations/addRegistrationFourth";
    }

    @GetMapping("/addRegistrationFourth")
    public String chooseTimeOfDayRegistration(Model model) {
//        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long userId = Long.valueOf(customUserDetails.getUserId());
//        System.out.println("userId :" + userId);

        List<Slot> freeTimeSlots = slotService.getFreeSlotsByDoctorDTOIdAndDayId(doctorDTOIdForFuture, dayIdForFuture);
//        freeTimeSlots.forEach(s -> System.out.println(s.getTimeSlot()));
        model.addAttribute("freeTimeSlots", freeTimeSlots);
        return "registrations/chooseTimeOfDayRegistration";
    }

    @PostMapping("/addRegistrationFourth")
    public String chooseTimeOfDayRegistration(@RequestParam("freeTime") Long slotId,
                                              Model model) {
        System.out.print("slotId : " + slotId);

        // static slotId appropriation
        slotIdForFuture = slotId;

        Long doctorSlotId = doctorSlotService.getDoctorSlotByDoctorAndDayAndSlot(doctorDTOIdForFuture, dayIdForFuture, slotIdForFuture);
//        System.out.println(doctorSlotDTO.getId() + " ___ " + doctorSlotDTO.getDay().getDay() + " ___ " + doctorSlotDTO.getCabinet().getCabinetNumber());
        registrationService.addRecord(doctorSlotId);
//        System.out.println(doctorSlotId);

        return "registrations/allDoneRegistration";
    }

    @GetMapping("/listAll")
    public String listAll(Model model) {
        List<RegistrationDTO> registrations = registrationService.listAll();
        model.addAttribute("registrations", registrations);
        return "registrations/listAll";
    }

    @GetMapping("myRegistrations")
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "3") int pageSize,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
//        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "isActive"));
        Page<RegistrationDTO> currentRegistrations = registrationService.listAllCurrentPaged(pageRequest);
//        final String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        if (ADMIN.equalsIgnoreCase(userName)) {
//            books = bookService.getAllBooksWithAuthors(pageRequest);
//        }
//        else {
//            books = bookService.getAllNotDeletedBooksWithAuthors(pageRequest);
//        }
        model.addAttribute("currentRegistrations", currentRegistrations);
        return "registrations/myList";
    }

//    @GetMapping("/myRegistrations")
//    public String myList(Model model) {
//        List<Registration> currentRegistrations = registrationService.listAllCurrent();
////        currentRegistrations.forEach(s -> System.out.println(s.getClient() + " " + s.getDoctorSlot() + " " + s.getResultMeet()));
//        model.addAttribute("currentRegistrations", currentRegistrations);
//        return "registrations/myList";
//    }

    @RequestMapping(value = "/deleteRecord/{id}")
    public String deleteRecordById(@PathVariable(value = "id") Long registrationId) {
//        System.out.println(registrationId);
        Long doctorSlotId = registrationService.getOne(registrationId).getDoctorSlot().getId();
//        System.out.println(doctorSlotId);
        DoctorSlotDTO updatedDoctorSlot = doctorSlotService.getOne(doctorSlotId);
        updatedDoctorSlot.setIsRegistered(false);
        doctorSlotService.update(updatedDoctorSlot);
        registrationService.delete(registrationId);

        return "redirect:/registrations/myRegistrations";
    }

}

