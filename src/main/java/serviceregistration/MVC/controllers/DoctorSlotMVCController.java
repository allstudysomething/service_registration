package serviceregistration.MVC.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import serviceregistration.dto.CustomInterfaces.CustomDoctorSpecializationDay;
import serviceregistration.dto.DateSearchDTO;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.DoctorSlotSearchAdminDTO;
import serviceregistration.model.Cabinet;
import serviceregistration.model.Day;
import serviceregistration.model.Slot;
import serviceregistration.service.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/doctorslots")
public class DoctorSlotMVCController {

    private final DoctorSlotService doctorSlotService;
    private final DoctorService doctorService;
    private final DayService dayService;
    private final SlotService slotService;
    private final CabinetService cabinetService;

    public DoctorSlotMVCController(DoctorSlotService doctorSlotService,
                                   DoctorService doctorService,
                                   DayService dayService,
                                   SlotService slotService,
                                   CabinetService cabinetService) {
        this.doctorSlotService = doctorSlotService;
        this.doctorService = doctorService;
        this.dayService = dayService;
        this.slotService = slotService;
        this.cabinetService = cabinetService;
    }

    @GetMapping("")
    public String getSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "15") int pageSize,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.ASC, "day_id");
        Page<DoctorSlotDTO> doctorSlots = doctorSlotService.listAllPaging(pageRequest);
//        Page<DoctorSlotDTO> doctorSlots = doctorSlotService.listAllPagingWOTimeSlots(pageRequest);
//        model.addAttribute("flagTime", 1);
        model.addAttribute("doctorslots", doctorSlots);
        return "doctorslots/schedule";
    }


    @PostMapping("/search")
    public String getSearchSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "15") int pageSize,
                                    @ModelAttribute("doctorslotSearchFormAdmin") DoctorSlotSearchAdminDTO doctorSlotSearchAdminDTO,
                                    Model model) {
//        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
//        Page<DoctorSlotDTO> doctorSlots = doctorSlotService.findDoctorSlotByMany(doctorSlotSearchAdminDTO, pageRequest);
        List<DoctorSlotDTO> doctorSlots = doctorSlotService.findDoctorSlotByManyNotPaging(doctorSlotSearchAdminDTO);
        model.addAttribute("currentDate", LocalDate.now());
        model.addAttribute("flagTime", 1);
        model.addAttribute("doctorslots", doctorSlots);
//        System.out.println(LocalDate.now());
//        System.out.println(doctorSlots.get(0).getDay().getDay());
        return "doctorslots/scheduleActual";
    }

    @GetMapping("/currentDays")
    public String getCurrentDays(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "20") int pageSize,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<CustomDoctorSpecializationDay> doctorSlots = doctorSlotService.listCurrentDays10(pageRequest);
        model.addAttribute("doctorslots", doctorSlots);
        return "doctorslots/scheduleForAll";
    }

//    @GetMapping("/makeMeet")
//    public String getCurrentDaysPlus(@RequestParam(value = "page", defaultValue = "1") int page,
//                                 @RequestParam(value = "size", defaultValue = "20") int pageSize,
//                                 Model model) {
//        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
//        Page<MyUniversalQueryModel> doctorslotsMUQM = doctorSlotService.getCurrentDaysPlus(pageRequest);
////        doctorSlots.forEach(s -> System.out.println(s.toString()));
//        model.addAttribute("doctorslotsMUQM", doctorslotsMUQM);
//        return "doctorslots/makeMeet";
//    }

    @GetMapping("/getActualSchedule")
    public String getActualSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "15") int pageSize,
                              Model model) {
//        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.ASC, "day_id");
//        Page<DoctorSlotDTO> doctorSlots = doctorSlotService.listAllCurrentPaging(pageRequest);
        List<DoctorSlotDTO> doctorSlots = doctorSlotService.listAllCurrent();
//        System.out.println(doctorSlots.get(0));
        model.addAttribute("doctorslots", doctorSlots);
        return "doctorslots/scheduleActual";
    }

    @GetMapping ("/addSchedule")
    public String addSchedule(Model model,
                              @ModelAttribute("exception") final String exception) {
        List<DoctorDTO> doctors = doctorService.listAllSorted();
        List<Day> days = dayService.getcurrent10days();
        List<Slot> slots = slotService.listAll();
        List<Cabinet> cabinets = cabinetService.listAll();
        model.addAttribute("doctors", doctors);
        model.addAttribute("days", days);
        model.addAttribute("slots", slots);
        model.addAttribute("cabinets", cabinets);
        model.addAttribute("scheduleForm", new DoctorSlotDTO());
        model.addAttribute("exception", exception);
        return "doctorslots/addSchedule";
    }

    @PostMapping("/addSchedule")
    public String addSchedule(@ModelAttribute("scheduleForm") DoctorSlotDTO doctorSlotDTO,
                              BindingResult bindingResult,
                              Model model) {
        if (doctorSlotService.getDoctorSlotByDoctorAndDay(doctorSlotDTO.getDoctor().getId(), doctorSlotDTO.getDay().getId()) != null) {
            bindingResult.rejectValue("day", "error.day", "Врач уже работает " + doctorSlotDTO.getDay().getDay());
            System.out.println(" in bindingResult Врач уже работает ");
//            throw new MyDeleteException("Врач уже работает в этот день");
            return "doctorslots/addSchedule";
        }
        if (doctorSlotService.getDoctorSlotByCabinetAndDay(doctorSlotDTO.getCabinet().getId(), doctorSlotDTO.getDay().getId()) != null) {
            bindingResult.rejectValue("cabinet", "error.cabinet", "В этот день кабинет номер "
                    + doctorSlotDTO.getCabinet().getCabinetNumber() +  " занят");
            System.out.println(" in bindingResult В этот день кабинет занят");
//            throw new MyDeleteException("В этот день кабинет занят");
            return "doctorslots/addSchedule";
        }
        doctorSlotService.addSchedule(doctorSlotDTO.getDoctor().getId(),
                doctorSlotDTO.getDay().getId(),
                doctorSlotDTO.getCabinet().getId());
        return "redirect:/doctorslots/getActualSchedule";
    }

    //TODO make soft delete (if needed)
    @GetMapping("/deleteSchedule")
    public String deleteSchedule(Model model,
                                 @ModelAttribute("exception") final String exception) {
        List<DoctorDTO> doctors = doctorService.listAllSorted();
        List<Day> days = dayService.getcurrent10days();
        model.addAttribute("doctors", doctors);
        model.addAttribute("days", days);
        model.addAttribute("scheduleForm", new DoctorSlotDTO());
        model.addAttribute("exception", exception);
        return "doctorslots/deleteSchedule";
    }

    @PostMapping("/deleteSchedule")
    public String deleteSchedule(@ModelAttribute("scheduleForm") DoctorSlotDTO doctorSlotDTO,
                                 BindingResult bindingResult) {
        if (doctorSlotService.getDoctorSlotByDoctorAndDay(doctorSlotDTO.getDoctor().getId(), doctorSlotDTO.getDay().getId()) == null) {
            bindingResult.rejectValue("day", "error.day", "Врач "
                    + doctorSlotDTO.getDoctor().getLastName() + " "
                    + doctorSlotDTO.getDoctor().getFirstName().charAt(0) + "."
                    + doctorSlotDTO.getDoctor().getMidName().charAt(0) + "."
                    + " не работает " + doctorSlotDTO.getDay().getDay());
//            bindingResult.rejectValue("doctor", "error.doctor", "Врач не работает " + doctorSlotDTO.getDay().getDay());
//            System.out.println(" in bindingResult Врач уже работает ");
//            throw new MyDeleteException("Врач уже работает в этот день");
            return "doctorslots/deleteSchedule";
        }

        doctorSlotService.deleteSchedule(doctorSlotDTO.getDoctor().getId(), doctorSlotDTO.getDay().getId());
        return "redirect:/doctorslots/getActualSchedule";
    }

    @GetMapping ("/mySchedule")
    public String mySchedule(Model model) {
        List<DoctorSlotDTO> doctorslots = doctorSlotService.getMySchedule();
        model.addAttribute("doctorslots", doctorslots);
        return "doctorslots/mySchedule";
    }

    // TODO продумать как реализовать чтобы после поиска можно было удалить запись и остаться на поиске
    @PostMapping ("/myScheduleSearch")
    public String myScheduleSearch(Model model,
                                   @ModelAttribute("doctorslotSearchFormDoctor") DateSearchDTO dateSearchDTO) {
        System.out.println(" in     @PostMapping (\"/myScheduleSearch\")");
        System.out.println(dateSearchDTO.getRegistrationDay());
        List<DoctorSlotDTO> searchDoctorslots = doctorSlotService.getMySearchedSchedule(dateSearchDTO.getRegistrationDay());
        model.addAttribute("doctorslots", searchDoctorslots);
        return "doctorslots/mySchedule";
    }
}
