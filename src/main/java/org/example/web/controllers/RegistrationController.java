package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.UserService;
import org.example.web.dto.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/registration")
public class RegistrationController {

    private final Logger logger = Logger.getLogger(RegistrationController.class);
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String registration(Model model) {
        logger.info("GET /registration returns registration_page.html");
        model.addAttribute("registrationForm", new LoginForm());
        return "registration_page";
    }

    @PostMapping("/create")
    public String createUser(LoginForm loginForm) {
        if (userService.createUser(loginForm)) {
            logger.info("Registration OK redirect to login page");
            return "redirect:/login";
        } else {
            logger.info("Registration FAIL redirect back to registration");
            return "redirect:/registration";
        }
    }
}
