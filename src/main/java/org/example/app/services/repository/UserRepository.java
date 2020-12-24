package org.example.app.services.repository;

import org.apache.log4j.Logger;
import org.example.web.dto.LoginForm;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {

    private final Logger logger = Logger.getLogger(UserRepository.class);

    private final Map<String, LoginForm> repo = new HashMap<>();

    public boolean save(LoginForm credentials) {
        logger.info("create new user: " + credentials.getUsername());
        repo.put(credentials.getUsername(), credentials);
        return true;
    }

    public LoginForm retrieveCredentialsByUserName(String userName) {
        logger.info("retrieve credentials for user: " + userName);
        return repo.get(userName);
    }
}
