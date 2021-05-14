package com.acme.caas.web.rest;

import com.acme.caas.domain.User;
import com.acme.caas.web.rest.errors.UnauthorizedUserException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.service.Response;

@CrossOrigin
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/v1/users")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${keycloak.client.id}")
    private String keycloakClientID;

    @Value("${keycloak.server.url.auth}")
    private String keycloakAuthURL;

    @PostMapping(path = "/auth", consumes = "application/json", produces = "application/json")
    public User authenticateUser(@RequestBody User user) {
        logger.info("Authenticate User -> {}", user.getUsername());
        logger.info("User Password -> {}", user.getPassword());

        user.setAuthenticated(false);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("response_type", "json");
        map.add("grant_type", "password");
        map.add("client_id", keycloakClientID);
        map.add("username", user.getUsername());
        map.add("password", user.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(keycloakAuthURL, HttpMethod.POST, request, String.class);

            String response = responseEntity.getBody();
            logger.debug("KeyCloak Auth Response -> " + response);

            JsonObject authObj = JsonParser.parseString(response).getAsJsonObject();
            String jwtToken = authObj.get("access_token").getAsString();

            user.setPassword("");
            user.setAuthenticated(true);
            user.setJwt(jwtToken);
        } catch (Exception ex) {
            logger.error("Auth Error", ex);
            if (ex.getMessage().contains("401")) {
                throw new UnauthorizedUserException();
            } else {
                throw ex;
            }
        }

        return user;
    }
}
