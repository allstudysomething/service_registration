package serviceregistration.MVC.controllers;

import jakarta.security.auth.message.AuthException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import serviceregistration.constants.Errors;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSearchAllDTO;
import serviceregistration.exception.MyDeleteException;
import serviceregistration.model.Specialization;
import serviceregistration.service.DoctorService;
import serviceregistration.service.SpecializationService;
import serviceregistration.service.userdetails.CustomUserDetails;

import java.util.List;
import java.util.Objects;

import static serviceregistration.constants.UserRolesConstants.ADMIN;

@Controller
@RequestMapping("/doctors")
public class DoctorMVCController {

    // TODO продумать : нужен ли поиск по доктору/id доктора ? (doctor/{id})

    private final DoctorService doctorService;
    private final SpecializationService specializationService;

    public DoctorMVCController(DoctorService doctorService,
                               SpecializationService specializationService) {
        this.doctorService = doctorService;
        this.specializationService = specializationService;
    }

    @GetMapping("")
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "15") int pageSize,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize
                , Sort.by(Sort.Direction.ASC, "lastName")
        );
        Page<DoctorDTO> doctorDTOPage = doctorService.listAll(pageRequest);
        model.addAttribute("doctors", doctorDTOPage);
        return "doctors/list";
    }

    @PostMapping("/search")
    public String getAllSearch(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "5") int pageSize,
                         @ModelAttribute("doctorsSearchFormAll") DoctorSearchAllDTO doctorSearchAllDTO,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<DoctorDTO> doctorDTOPage = doctorService.listAllSearched(doctorSearchAllDTO, pageRequest);
        model.addAttribute("doctors", doctorDTOPage);
        return "doctors/list";
    }

    @GetMapping("/addDoctor")
    public String addDoctor(Model model) {
        List<Specialization> specializations = specializationService.listAll();
        model.addAttribute("specializations", specializations);
        model.addAttribute("doctorForm", new DoctorDTO());
        return "doctors/addDoctor";
    }

    @PostMapping("/addDoctor")
    public String addDoctor(@ModelAttribute("doctorForm") DoctorDTO doctorDTO,
                            BindingResult bindingResult,
                            Model model) {
        addDoctor(model);
        if (doctorDTO.getLogin().equalsIgnoreCase(ADMIN) || doctorService.getDoctorByLogin(doctorDTO.getLogin()) != null) {
            bindingResult.rejectValue("login", "error.login", "Этот логин уже существует");
            return "doctors/addDoctor";
        }
        doctorService.create(doctorDTO, true);
        return "redirect:/doctors";
    }

//    @GetMapping("/deleteDoctor")
//    public String deleteDoctor(Model model) {
//        List<DoctorDTO> doctors = doctorService.listAll();
//        model.addAttribute("doctors", doctors);
//        return "doctors/deleteDoctor";
//    }
//
//    @PostMapping("/deleteDoctor")
//    public String deleteDoctor(@RequestParam("doctorDelete") Long doctorDTOId) {
////        System.out.println(doctorDTOId);
//        doctorService.delete(doctorDTOId);
//        return "redirect:/doctors";
//    }

    @GetMapping("/deleteSoft/{id}")
    public String delete(@PathVariable Long id) throws MyDeleteException {
//        System.out.println(" in @GetMapping(\"/deleteSoft/{id}\") ");
//        System.out.println(id);
//        System.out.println();
        doctorService.deleteSoft(id);
        return "redirect:/doctors";
    }

    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        doctorService.restore(id);
        return "redirect:/doctors";
    }

    @GetMapping("/{id}")
    public String viewDoctor(@PathVariable Long id,
                             Model model) {
        model.addAttribute("doctor", doctorService.getOne(id));
        return "profile/viewDoctor";
    }

    @GetMapping("/profile/{id}")
    public String userProfile(@PathVariable Integer id,
//                              @ModelAttribute(value = "exception") String exception,
                              Model model) throws AuthException {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!Objects.isNull(customUserDetails.getUserId())) {
            if (!ADMIN.equalsIgnoreCase(customUserDetails.getUsername())) {
                if (!id.equals(customUserDetails.getUserId())) {
                    throw new AuthException(HttpStatus.FORBIDDEN + ": " + Errors.Users.USER_FORBIDDEN_ERROR);
                }
            }
        }
        model.addAttribute("doctorDTO", doctorService.getOne(Long.valueOf(id)));
//        model.addAttribute("exception", exception);
        return "profile/viewDoctorProfile";
    }

}
