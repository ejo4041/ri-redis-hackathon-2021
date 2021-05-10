package com.acme.caas.web.rest;

import com.acme.caas.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/v1/client")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private RedisService redisService;

    @Autowired
    public ClientController(RedisService _redisService) {
        this.redisService = _redisService;
    }
}
