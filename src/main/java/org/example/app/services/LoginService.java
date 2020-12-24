package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.app.services.repository.UserRepository;
import org.example.web.dto.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserService userService;

    @Autowired
    public LoginService(UserService userService) {
        this.userService = userService;
    }

    private Logger logger = Logger.getLogger(LoginService.class);

    public boolean authenticate(LoginForm loginFrom) {
        logger.info("try auth with user-form: " + loginFrom);
        return userService.verify(loginFrom);
    }
}
