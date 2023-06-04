package serviceregistration.MVC.controllers;

import jakarta.security.auth.message.AuthException;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import serviceregistration.constants.Errors;
import serviceregistration.dto.ClientDTO;
import serviceregistration.mapper.ClientMapper;
import serviceregistration.repository.ClientRepository;
import serviceregistration.service.ClientService;
import serviceregistration.service.UserService;
import serviceregistration.service.userdetails.CustomUserDetails;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static serviceregistration.constants.UserRolesConstants.ADMIN;

@Slf4j
@Controller
@RequestMapping("/clients")
public class ClientMVCController {
    private final ClientRepository clientRepository;
    private final ClientService clientService;
    private final UserService userService;
    private final ClientMapper clientMapper;

    public ClientMVCController(ClientService clientService, UserService userService,
                               ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.userService = userService;
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @GetMapping("")
    public String getAll(Model model) {
        List<ClientDTO> clients = clientService.listAll();
        model.addAttribute("clients", clients);
        return "clients/list";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("clientForm", new ClientDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("clientForm") ClientDTO clientDTO,
                               BindingResult bindingResult) {
        String login = clientDTO.getLogin().toLowerCase();
        clientDTO.setLogin(login);
        if (login.equalsIgnoreCase(ADMIN) ||
                (userService.findUserByLogin(login) != null && userService.findUserByLogin(login).getLogin().equals(login))) {
            bindingResult.rejectValue("login", "login.error", "Этот логин уже существует");
            return "registration";
        }
        if (clientService.getClientByEmail(clientDTO.getEmail()) != null) {
            bindingResult.rejectValue("email", "email.error", "Этот email уже существует");
            return "registration";
        }
        clientService.create(clientDTO, true);
        return "redirect:login";
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
        model.addAttribute("clientDTO", clientService.getOne(Long.valueOf(id)));

//        System.out.println(" ****** * ** ");
//        System.out.println(clientService.getOne(Long.valueOf(id)));
//        System.out.println(" ****** * ** d");

//        model.addAttribute("exception", exception);
        return "profile/viewClient";
    }

    // TODO добавить всплывающее окно с ошибкой 403 нет пав на просмотр другого пользователя
    @GetMapping("/profile/update/{id}")
    public String updateProfile(@PathVariable Integer id,
                                Model model) throws AuthException {
        ClientDTO clientDTO = clientService.getOne(Long.valueOf(id));
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!clientDTO.getLogin().equals(customUserDetails.getUsername())) {
            throw new AuthException(HttpStatus.FORBIDDEN + ": " + Errors.Users.USER_FORBIDDEN_ERROR);
        }
        model.addAttribute("clientForm", clientDTO);
        return "profile/updateClientProfile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("clientForm") ClientDTO clientDTOFromUpdateForm,
                                BindingResult bindingResult) {
        ClientDTO userEmailDuplicated = clientService.getClientByEmail(clientDTOFromUpdateForm.getEmail());
        ClientDTO foundUser = clientService.getOne(clientDTOFromUpdateForm.getId());
        String currentPassword = foundUser.getPassword();

        System.out.println("   *               ** * * * *");
        System.out.println("clientDTOFromUpdateForm : " + clientDTOFromUpdateForm.getPassword());
        System.out.println("userEmailDuplicated : " + userEmailDuplicated.getPassword());
//        if(userEmailDuplicated != null) System.out.println(userEmailDuplicated.getEmail());
        System.out.println("foundUser : " + foundUser.getPassword());
        System.out.println("   *               ** * * * *");

        // TODO проверить в коайней версии кода у преподавателя
//        if (userEmailDuplicated != null && !Objects.equals(userEmailDuplicated.getEmail(), foundUser.getEmail())) {
//            bindingResult.rejectValue("email", "error.email", "Такой email уже существует");
//            return "profile/updateClientProfile";
//        }

        foundUser.setFirstName(clientDTOFromUpdateForm.getFirstName());
        foundUser.setLastName(clientDTOFromUpdateForm.getLastName());
        foundUser.setMidName(clientDTOFromUpdateForm.getMidName());
        foundUser.setEmail(clientDTOFromUpdateForm.getEmail());
        foundUser.setBirthDate(clientDTOFromUpdateForm.getBirthDate());
//        foundUser.setPassword(currentPassword);

        // TODO проверить на idea community edition или на другом проекте get/set Enum
//        foundUser.setGender(clientDTOFromUpdateForm.getGender());

//        int age = Period.between(LocalDate.from(clientDTOFromUpdateForm.getBirthDate()), LocalDate.now()).getYears();
//        foundUser.setAge(age);
        foundUser.setPhone(clientDTOFromUpdateForm.getPhone());
        foundUser.setAddress(clientDTOFromUpdateForm.getAddress());
        clientService.update(foundUser);
//        clientRepository.save(clientMapper.toEntity(foundUser));
//        clientService.profileUpdateBySpareTwoUsers(userEmailDuplicated, foundUser);
        return "redirect:/clients/profile/" + clientDTOFromUpdateForm.getId();
    }

    @GetMapping("/change-password")
    public String changePassword(@PathParam(value = "uuid") String uuid,
                                 Model model) {
//        Boolean checkExpiredDate = true;
        Boolean checkExpiredDate = clientService.checkExpiredDate(uuid);
        if(checkExpiredDate) {
            return "redirect:/clients/remember-password";
        } else {
        model.addAttribute("uuid", uuid);
//        return "registrations/allDoneRegistration";
        return "clients/changePassword";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@PathParam(value = "uuid") String uuid,
                                 @ModelAttribute("changePasswordForm") ClientDTO clientDTO) {
//        System.out.println(clientDTO);
        clientService.changePassword(uuid, clientDTO.getPassword());
        return "redirect:/login";
    }

    @GetMapping("/change-password/client")
    public String changePassword(Model model) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClientDTO clientDTO = clientService.getClientByLogin(customUserDetails.getUsername());
        UUID uuid = UUID.randomUUID();
        clientDTO.setChangePasswordToken(uuid.toString());
        clientService.update(clientDTO);
        model.addAttribute("uuid", uuid);
        return "clients/changePassword";
    }

    @GetMapping("/remember-password")
    public String rememberPassword() {
        return "clients/rememberPassword";
    }

    @PostMapping("/remember-password")
    public String rememberPassword(@ModelAttribute("changePasswordForm") ClientDTO clientDTO) {
        clientDTO = clientService.getClientByEmail(clientDTO.getEmail());
        if (Objects.isNull(clientDTO)) {
//            return "Error!";
            System.out.println("Objects.isNull(clientDTO) : is null");
            return "redirect:/remember-password";
        }
        else {
            System.out.println(clientDTO);
            clientService.sendChangePasswordEmail(clientDTO);
            return "redirect:/login";
        }
    }


}

//    @PostMapping("/registration")
//    public String registration(@ModelAttribute("clientForm") ClientDTO clientDTO, BindingResult bindingResult) {
//        String login = clientDTO.getLogin().toLowerCase();
//        clientDTO.setLogin(login);
//        if (clientDTO.getLogin().equalsIgnoreCase(ADMIN) || clientService.getClientByLogin(clientDTO.getLogin()) != null) {
//            bindingResult.rejectValue("login", "error.login", "Такой логин уже существует");
//            return "registration";
//        }
//        if (clientService.getClientByEmail(clientDTO.getEmail()) != null) {
//            bindingResult.rejectValue("email", "error.email", "Такой e-mail уже существует");
//            return "registration";
//        }
//        if (clientService.getClientByPhone(clientDTO.getPhone()) != null) {
//            bindingResult.rejectValue("phone", "error.phone", "Такой telephone уже существует");
//            return "registration";
//        }
//        if (clientService.getClientByPolicy(clientDTO.getPolicy()) != null) {
//            bindingResult.rejectValue("policy", "error.policy", "Такой polis уже существует");
//            return "registration";
//        }
//
//        clientService.create(clientDTO);
//        return "redirect:login";
//    }
