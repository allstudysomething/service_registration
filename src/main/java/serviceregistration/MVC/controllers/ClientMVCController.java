package serviceregistration.MVC.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import serviceregistration.dto.ClientDTO;
import serviceregistration.service.ClientService;
import serviceregistration.service.UserService;

import java.util.List;

import static serviceregistration.constants.UserRolesConstants.ADMIN;

@Slf4j
@Controller
@RequestMapping("/clients")
public class ClientMVCController {

    private final ClientService clientService;
    private final UserService userService;

    public ClientMVCController(ClientService clientService, UserService userService) {
        this.clientService = clientService;
        this.userService = userService;
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
        clientService.create(clientDTO);
        return "redirect:login";
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
