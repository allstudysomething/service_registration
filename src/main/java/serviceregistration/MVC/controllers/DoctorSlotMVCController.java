package serviceregistration.MVC.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.model.Cabinet;
import serviceregistration.model.Day;
import serviceregistration.model.Slot;
import serviceregistration.repository.DoctorSlotRepository;
import serviceregistration.service.*;

import java.util.List;
import java.util.Objects;

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
    public String getSchedule(Model model) {
        List<DoctorSlotDTO> doctorSlots = doctorSlotService.listAll();
        model.addAttribute("doctorslots", doctorSlots);
        return "doctorslots/schedule";
    }

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
                              BindingResult bindingResult) {
        if(!Objects.isNull(doctorSlotService.findDoctorSlotByCabinetAndDay(doctorSlotDTO.getCabinet(),
                doctorSlotDTO.getDay()))) {
            bindingResult.rejectValue("cabinet", "error.cabinet", "This Record of Day" +
                    " and Cabinet are in use");
            System.out.println("******** zapis suschestvuet *******");
//            return "doctorslots/addSchedule";
        } else {
            System.out.println("********** NOT PRESENT RECORD ***********");
//            return "doctorslots/addSchedule";
        }

        doctorSlotService.getSchedule(doctorSlotDTO.getDoctor().getId(),
                doctorSlotDTO.getDay().getId(),
                doctorSlotDTO.getCabinet().getId());
        return "redirect:/doctorslots";
    }

}
