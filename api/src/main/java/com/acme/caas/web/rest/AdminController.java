package com.acme.caas.web.rest;

import com.acme.caas.domain.AdminControllerResponse;
import com.acme.caas.domain.CaaSTemplate;
import com.acme.caas.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/v1/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private RedisService redisService;

    @Autowired
    public AdminController(RedisService _redisService) {
        this.redisService = _redisService;
    }

    @PostMapping(path = "/template/create", consumes = "application/json", produces = "application/json")
    public AdminControllerResponse createTemplate(@RequestBody CaaSTemplate template) throws Exception {
        logger.info("/template/create");
        logger.debug("Template: " + template.toString());

        redisService.createTemplate(template);

        // Respond
        AdminControllerResponse response = new AdminControllerResponse();
        response.setMessage("Template Saved!");
        return response;
    }
}
