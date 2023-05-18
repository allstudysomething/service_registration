package serviceregistration.MVC.controllers;

import lombok.extern.slf4j.Slf4j;
import nonapi.io.github.classgraph.json.JSONUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import serviceregistration.dto.ClientDTO;
import serviceregistration.service.ClientService;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static serviceregistration.constants.UserRolesConstants.ADMIN;

@Slf4j
@Controller
@RequestMapping("/clients")
public class ClientMVCController {

    private final ClientService clientService;

    public ClientMVCController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("clientForm", new ClientDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("clientForm") ClientDTO clientDTO, BindingResult bindingResult) {
        if (clientDTO.getLogin().equalsIgnoreCase(ADMIN) || clientService.getClientByLogin(clientDTO.getLogin()) != null) {
            bindingResult.rejectValue("login", "error.login", "Такой логин уже существует");
            return "registration";
        }
        if (clientService.getClientByEmail(clientDTO.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.email", "Такой e-mail уже существует");
            return "registration";
        }
        if (clientService.getClientByPhone(clientDTO.getPhone()) != null) {
            bindingResult.rejectValue("phone", "error.phone", "Такой telephone уже существует");
            return "registration";
        }
        if (clientService.getClientByPolicy(clientDTO.getPolicy()) != null) {
            bindingResult.rejectValue("policy", "error.policy", "Такой polis уже существует");
            return "registration";
        }

        clientService.create(clientDTO);
        return "redirect:login";
    }

    @GetMapping("/list")
    public String getAll(Model model) {
        List<ClientDTO> clients = clientService.listAll();
        model.addAttribute("clients", clients);
        return "clients/list";
    }


}
