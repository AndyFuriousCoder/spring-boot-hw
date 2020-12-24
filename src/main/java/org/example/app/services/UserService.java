package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.app.services.repository.UserRepository;
import org.example.web.dto.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.isEmpty;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Logger logger = Logger.getLogger(UserService.class);

    private final LoginForm root = new LoginForm("root", "123");

    public boolean createUser(LoginForm loginForm) {
        String username = loginForm.getUsername();

        if(isNull(username) || isEmpty(username)) {
            logger.info("Unable to register new user. Empty user name is not allowed");
            return  false;
        }

        if(isRoot(loginForm) || nonNull(userRepository.retrieveCredentialsByUserName(username))) {
            logger.info("Unable to register new user. User with name: " + username + " already exists");
            return  false;
        }

        return userRepository.save(loginForm);
    }

    public boolean verify(LoginForm loginForm) {
        String username = loginForm.getUsername();

        if(isNull(username) || isEmpty(username)) {
            return  false;
        }

        if(isRoot(loginForm)) {
            return true;
        }

        LoginForm credentials = userRepository.retrieveCredentialsByUserName(username);

        if(isNull(credentials)) {
            return false;
        }

        return match(credentials, loginForm);
    }

    private boolean isRoot(LoginForm loginForm) {
        return match(root, loginForm);
    }

    private boolean match(LoginForm expected, LoginForm actual) {
        return Objects.equals(expected.getUsername(), actual.getUsername())
                && Objects.equals(expected.getPassword(), actual.getPassword());
    }
}
