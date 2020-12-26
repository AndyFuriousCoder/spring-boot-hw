package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.UserService;
import org.example.web.dto.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

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
        model.addAttribute("registrationForm", new RegistrationForm());
        return "registration_page";
    }

    @PostMapping("/create")
    public String createUser(@Valid RegistrationForm registrationForm, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            logger.info("Registration FAIL. Username or password is empty");
            model.addAttribute("registrationForm", registrationForm);
            return "registration_page";
        } else {
            if (userService.createUser(registrationForm)) {
                logger.info("Registration OK redirect to login page");
                return "redirect:/login";
            } else {
                logger.info("Registration FAIL redirect back to registration");
                return "redirect:/registration";
            }
        }
    }
}
