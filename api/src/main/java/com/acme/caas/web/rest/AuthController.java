package com.acme.caas.web.rest;

import com.acme.caas.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.service.Response;

@CrossOrigin
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/v1/users")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping(path = "/auth", consumes = "application/json", produces = "application/json")
    public User authenticateUser(@RequestBody User user) {
        logger.info("Authenticate User -> {}", user.getUsername());
        logger.info("User Password -> {}", user.getPassword());

        user.setAuthenticated(true);
        user.setJwt("35434534kjdfzgkjdfg");

        return user;
    }
}
