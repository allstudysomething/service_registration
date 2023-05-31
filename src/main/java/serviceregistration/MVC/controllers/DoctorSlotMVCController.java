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
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.DoctorSlotSearchAdminDTO;
import serviceregistration.model.Cabinet;
import serviceregistration.model.Day;
import serviceregistration.model.Slot;
import serviceregistration.service.*;

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
        model.addAttribute("doctorslots", doctorSlots);
        return "doctorslots/schedule";
    }

    @PostMapping("/search")
    public String getSearchSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "15") int pageSize,
                                    @ModelAttribute("doctorslotSearchFormAdmin") DoctorSlotSearchAdminDTO doctorSlotSearchAdminDTO,
                                    Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<DoctorSlotDTO> doctorSlots = doctorSlotService.findDoctorSlotByMany(doctorSlotSearchAdminDTO, pageRequest);
        model.addAttribute("doctorslots", doctorSlots);
        return "doctorslots/schedule";
    }

    @GetMapping("/currentDays")
    public String getCurrentDays(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "4") int pageSize,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize
                //, Sort.Direction.ASC, "day_id"
                 );
        Page<CustomDoctorSpecializationDay> doctorSlots = doctorSlotService.listCurrentDays10(pageRequest);
        model.addAttribute("doctorslots", doctorSlots);
        return "doctorslots/scheduleForAll";
    }

    @GetMapping("/getActualSchedule")
    public String getActualSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "15") int pageSize,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.ASC, "day_id");
        Page<DoctorSlotDTO> doctorSlots = doctorSlotService.listAllCurrentPaging(pageRequest);
        model.addAttribute("doctorslots", doctorSlots);
        return "doctorslots/scheduleActual";
    }

//    @GetMapping("")
//    public String getSchedule(Model model) {
//        List<DoctorSlotDTO> doctorSlots = doctorSlotService.listAll();
//        model.addAttribute("doctorslots", doctorSlots);
//        return "doctorslots/schedule";
//    }

    @GetMapping ("/addSchedule")
    public String addSchedule(Model model) {
        List<DoctorDTO> doctors = doctorService.listAll();
        List<Day> days = dayService.listAll();
        List<Slot> slots = slotService.listAll();
        List<Cabinet> cabinets = cabinetService.listAll();
        model.addAttribute("doctors", doctors);
        model.addAttribute("days", days);
        model.addAttribute("slots", slots);
        model.addAttribute("cabinets", cabinets);
        model.addAttribute("scheduleForm", new DoctorSlotDTO());
        return "doctorslots/addSchedule";
    }
    @PostMapping("/addSchedule")
    public String addSchedule(@ModelAttribute("scheduleForm") DoctorSlotDTO doctorSlotDTO,
                              BindingResult bindingResult,
                              Model model) {
        addSchedule(model);
        if (doctorSlotService.getDoctorSlotByDoctorAndDay(doctorSlotDTO.getDoctor().getId(), doctorSlotDTO.getDay().getId()) != null) {
            bindingResult.rejectValue("day", "error.day", "Врач уже работает " + doctorSlotDTO.getDay().getDay());
            return "doctorslots/addSchedule";
        }
        if (doctorSlotService.getDoctorSlotByCabinetAndDay(doctorSlotDTO.getCabinet().getId(), doctorSlotDTO.getDay().getId()) != null) {
            bindingResult.rejectValue("cabinet", "error.cabinet", "В этот день кабинет занят");
            return "doctorslots/addSchedule";
        }
        doctorSlotService.addSchedule(doctorSlotDTO.getDoctor().getId(),
                doctorSlotDTO.getDay().getId(),
                doctorSlotDTO.getCabinet().getId());
        return "redirect:/doctorslots";
    }

    @GetMapping("/deleteSchedule")
    public String deleteSchedule(Model model) {
        List<DoctorDTO> doctors = doctorService.listAll();
        List<Day> days = dayService.listAll();
        model.addAttribute("doctors", doctors);
        model.addAttribute("days", days);
        return "doctorslots/deleteSchedule";
    }

    @PostMapping("/deleteSchedule")
    public String deleteSchedule(@ModelAttribute("scheduleForm") DoctorSlotDTO doctorSlotDTO) {
        doctorSlotService.deleteSchedule(doctorSlotDTO.getDoctor().getId(), doctorSlotDTO.getDay().getId());
        return "redirect:/doctorslots";
    }


//    @PostMapping("/addSchedule")
//    public String addSchedule(@ModelAttribute("scheduleForm") DoctorSlotDTO doctorSlotDTO,
//                              BindingResult bindingResult) {
//        if(!Objects.isNull(doctorSlotService.findDoctorSlotByCabinetAndDay(doctorSlotDTO.getCabinet(),
//                doctorSlotDTO.getDay()))) {
//            bindingResult.rejectValue("cabinet", "error.cabinet", "This Record of Day" +
//                    " and Cabinet are in use");
//            System.out.println("******** zapis suschestvuet *******");
////            return "doctorslots/addSchedule";
//        } else {
//            System.out.println("********** NOT PRESENT RECORD ***********");
////            return "doctorslots/addSchedule";
//        }
//
//        doctorSlotService.getSchedule(doctorSlotDTO.getDoctor().getId(),
//                doctorSlotDTO.getDay().getId(),
//                doctorSlotDTO.getCabinet().getId());
//        return "redirect:/doctorslots";
//    }

}
