package com.acme.caas.security.jwt;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${keycloak.server.url.verifyToken}")
    private String verifyToken;

    /**
     * Call Keycloak userinfo endpoint to verify JWT Tokens.  Do no validation of token directly in this service.
     * @param authToken
     * @param request
     * @return Authentication
     */
    public Authentication validateToken(String authToken, HttpServletRequest request) {
        String userName = "";
        try {
            log.debug("Token Presented: " + authToken);
            log.debug("Verify Token: " + verifyToken);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity httpEntity = new HttpEntity(headers);
            ResponseEntity<String> response = restTemplate.postForEntity(verifyToken, httpEntity, String.class);
            // HttpStatus statusCode = response.getStatusCode();
            // if (statusCode
            String tokenResponse = response.getBody().toString();

            log.debug("Verify Token Response -> " + tokenResponse);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                log.error("Unexpected status code verifying token: " + response.getStatusCode());
                throw new Exception("Invalid JWT Token");
            }

            JsonObject jso = JsonParser.parseString(tokenResponse).getAsJsonObject();

            userName = jso.get("preferred_username").getAsString();

            log.debug("Preferred Email: " + userName);

            log.debug("Verify Token Response: " + tokenResponse);
            log.debug("HttpStatus Code -> " + response.getStatusCode());

            Collection<? extends GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

            User principal = new User(userName, "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);

            Collection<? extends GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            User principal = new User("unauth", "", authorities);
            Authentication unauth = new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
            unauth.setAuthenticated(false);
            return unauth;
        }
    }
}
