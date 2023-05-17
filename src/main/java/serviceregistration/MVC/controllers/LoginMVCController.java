package serviceregistration.MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginMVCController {
    @GetMapping("")
    public String login() {
        return "login";
    }

}
