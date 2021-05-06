package com.acme.caas.web.rest;

import com.acme.caas.domain.CaaSTemplate;
import com.acme.caas.domain.responses.AdminControllerResponse;
import com.acme.caas.domain.responses.AdminTemplatesResponse;
import com.acme.caas.domain.responses.AdminTemplateResponse;
import com.acme.caas.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.MissingFormatArgumentException;

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

    /**
     * Attempts to create the given CaasTemplate in Redis. This endpoint will generated a new unique ID
     * for the template and use that for Redis persistence, before passing back the requested CaasTemplate with
     * a newly generated settingsId.
     * @param template a new CaasTemplate to add to redis
     * @return an AdminControllerResponse with message and updated CaasTemplate
     * @throws Exception
     */
    @PostMapping(path = "/template/create", consumes = "application/json", produces = "application/json")
    public AdminTemplateResponse createTemplate(@RequestBody CaaSTemplate template) throws Exception {
        logger.info("/template/create");
        logger.debug("Template: " + template.getTemplateName());

        if(template.getSettingsId() != null){
            logger.error("The provided template already has a settingsId, which is ignored when creating " +
                "a new template in Redis. The creation process generates a new settingsId.");
        }

        var returnTemplate = redisService.createTemplate(template);

        // Respond
        AdminTemplateResponse response = new AdminTemplateResponse();
        response.setMessage("Template Saved!");
        response.setTemplate(returnTemplate);
        return response;
    }

    @GetMapping(path = "/template/get", consumes = "application/json", produces = "application/json")
    public AdminTemplatesResponse getTemplates() throws Exception{
        logger.info("/template/get");

        var templates = redisService.getTemplates();

        AdminTemplatesResponse response = new AdminTemplatesResponse();
        response.setMessage("Templates retrieved!");
        response.setTemplateList(templates);
        return response;
    }

    @GetMapping(path = "/template/get/{id}", consumes = "application/json", produces = "application/json")
    public AdminTemplateResponse getTemplate(@PathVariable("id") String settingsId) throws Exception{
        logger.info("/template/get/"+settingsId);

        var template = redisService.getTemplate(settingsId);

        AdminTemplateResponse response = new AdminTemplateResponse();
        response.setMessage("Template retrieved!");
        response.setTemplate(template);
        return response;
    }

    @PutMapping(path = "/template/update", consumes = "application/json", produces = "application/json")
    public AdminTemplateResponse updateTemplate(@RequestBody CaaSTemplate template) throws Exception {
        logger.info("/template/update");
        logger.debug("Template: " + template.toString());

        if(template.getSettingsId() == null){
            logger.error("The provided template does not have a settingsId, which is required in order to update " +
                "the template");
            throw new IllegalArgumentException("The CaasTemplate is missing a required settingsId value");
        }

        redisService.updateTemplate(template);

        // Respond
        AdminTemplateResponse response = new AdminTemplateResponse();
        response.setMessage("Template Updated!");
        response.setTemplate(template);
        return response;
    }

    @PostMapping(path = "/template/update/{id}/templateName/{name}", produces = "application/json")
    public AdminControllerResponse updateTemplate(@PathVariable("id") @NotNull String settingsId,
                                                @PathVariable("name") @NotNull String templateName) throws Exception {
        logger.info("/template/update/templateName");
        logger.debug("Template: " + settingsId);

        redisService.updateTemplateName(settingsId, templateName);

        // Respond
        AdminControllerResponse response = new AdminControllerResponse();
        response.setMessage("Template name updated to " + templateName);
        return response;
    }
}
