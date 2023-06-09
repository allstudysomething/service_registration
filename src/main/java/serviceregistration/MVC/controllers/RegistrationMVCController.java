package serviceregistration.MVC.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.dto.RegistrationSearchAdminDTO;
import serviceregistration.dto.TempDTO;
import serviceregistration.model.*;
import serviceregistration.repository.UserRepository;
import serviceregistration.service.*;
import serviceregistration.service.userdetails.CustomUserDetails;

import java.util.List;


@Slf4j
@Controller
@RequestMapping("/registrations")
public class RegistrationMVCController {
    private final UserRepository userRepository;

    private static Specialization specializationForFuture;
    private static Long doctorDTOIdForFuture;
    private static Long dayIdForFuture;
    private static Long slotIdForFuture;

    private final RegistrationService registrationService;
    private final UserService userService;
    private final SpecializationService specializationService;
    private final DoctorSlotService doctorSlotService;
    private final DoctorService doctorService;
    private SlotService slotService;

    public RegistrationMVCController(RegistrationService registrationService, SpecializationService specializationService, DoctorSlotService doctorSlotService, DoctorService doctorService, SlotService slotService,
                                     UserRepository userRepository, UserService userService) {
        this.registrationService = registrationService;
        this.specializationService = specializationService;
        this.doctorSlotService = doctorSlotService;
        this.doctorService = doctorService;
        this.slotService = slotService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

//    @GetMapping("")
//    public String faceWindow() {
//        return "registrations/faceWindowClientRegistrations";
//    }

    @GetMapping("")
    public String listAll(@RequestParam(value = "page", defaultValue = "1") int page,
                          @RequestParam(value = "size", defaultValue = "20") int pageSize,
                          Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize
//                , Sort.by(Sort.Direction.DESC, "isActive")
        );
        Page<RegistrationDTO> registrationsPaging = registrationService.listAllCurrentPagedNotSorted(pageRequest);
        model.addAttribute("registrationsPaging", registrationsPaging);
        model.addAttribute("registrationSearchFormAdmin", new RegistrationSearchAdminDTO());
        return "registrations/listAll";
    }

    @PostMapping("/search")
    public String searchAll(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "3") int pageSize,
                            @ModelAttribute("registrationSearchFormAdmin") RegistrationSearchAdminDTO registrationSearchAdminDTO,
                            Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        model.addAttribute("registrationsPaging", registrationService.findRegistrationByMany(registrationSearchAdminDTO, pageRequest));
        return "registrations/listAll";
    }

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
    ) {

            // static specialization appropriation
            specializationForFuture = specialization;

        return "redirect:/registrations/addRegistrationSecond";
    }

    @GetMapping("/addRegistrationSecond")
    public String chooseDoctor(Model model) {
        List<DoctorDTO> doctorDTOS = doctorSlotService.findDoctorDTOBySpecializationIdAndDayBetween(specializationForFuture.getId());
        model.addAttribute("doctorDTOList", doctorDTOS);
        model.addAttribute("doctorDTOForm", new DoctorDTO());
        return "registrations/chooseDoctor";
    }

    @PostMapping("/addRegistrationSecond")
    public String chooseDoctor(@RequestParam("doctorDTO") Long doctorDTOId
            , Model model) {

        // static doctorDTOId appropriation
        doctorDTOIdForFuture = doctorDTOId;

        return "redirect:/registrations/addRegistrationThird";
    }

    @GetMapping("/redirectToThird/{doctorId}")
    public String redirectToThird(@PathVariable Long doctorId) {

        // static doctorDTOId appropriation
        doctorDTOIdForFuture = doctorId;
        specializationForFuture = specializationService.getOneByDoctorId(doctorId);

        return "redirect:/registrations/addRegistrationThird";
    }

    @GetMapping("/addRegistrationThird")
    public String chooseDay(Model model) {
        List<Day> dayList = doctorSlotService.findDaysByDoctorDTOIdAndNotRegisteredAndDateBetween(doctorDTOIdForFuture);
        model.addAttribute("doctorDTOForm", new DoctorDTO());
        model.addAttribute("doctorWorkDates", dayList);
        return "registrations/chooseDateOfDoctorWork";
    }

    @PostMapping("/addRegistrationThird")
    public String chooseDay(@RequestParam("day") Long dayId,
                                            Model model) {

        // static dayId appropriation
        dayIdForFuture = dayId;

        if(doctorSlotService.isActiveRegistrationByClientAndDayIdAndSpecializationId(specializationForFuture.getId(), dayIdForFuture)) {
            dayIdForFuture = null;
//            System.out.println("isActiveRegistrationByClientAndDayIdAndSpecializationId");
            return "redirect:/registrations/addRegistrationThird";
        }
        return "redirect:/registrations/addRegistrationFourth";
    }

    @GetMapping("/addRegistrationFourth")
    public String chooseTimeOfDay(Model model) {
        List<Slot> freeTimeSlots = doctorSlotService.getFreeSlotsByDoctorDTOIdAndDayId(doctorDTOIdForFuture, dayIdForFuture);
        model.addAttribute("freeTimeSlots", freeTimeSlots);
        return "registrations/chooseTimeOfDayRegistration";
    }

    @PostMapping("/addRegistrationFourth")
    public String chooseTimeOfDay(@RequestParam("freeTime") Long slotId,
                                              Model model) {

        // static slotId appropriation
        slotIdForFuture = slotId;

        Long doctorSlotId = doctorSlotService.getDoctorSlotByDoctorAndDayAndSlot(doctorDTOIdForFuture, dayIdForFuture, slotIdForFuture);
        RegistrationDTO registrationDTO = registrationService.addRecord(doctorSlotId);
        registrationService.sendAcceptedMeetEmail(registrationDTO);
        return "registrations/allDoneRegistration";
    }

    @GetMapping("myRegistrations")
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "16") int pageSize,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<RegistrationDTO> currentRegistrations = registrationService.listAllCurrentPagedByClient(pageRequest);
        model.addAttribute("currentRegistrations", currentRegistrations);
        return "registrations/myList";
    }

    @GetMapping("myRegistrationsAllTime")
    public String getAllAlways(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "15") int pageSize,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<RegistrationDTO> registrationsPagingAll = registrationService.listArchivePagedByClient(pageRequest);
        model.addAttribute("registrationsPagingAll", registrationsPagingAll);
        return "registrations/myListAllTime";
    }

    // Костыль метод(чтобы не плодить). При передаче из под учетки Доктора в registrationId будет передан doctorslotId
    @RequestMapping(value = "/deleteRecord/{id}")
    public String deleteRecordById(@PathVariable(value = "id") Long toDeleteId) {
        Long roleId = registrationService.getCurrentUserRoleId();
        registrationService.safeDelete(roleId, toDeleteId);
        return (roleId == 1L) ? "redirect:/registrations/myRegistrations"
            : "redirect:/doctorslots/mySchedule";
    }

}

