package com.acme.caas.web.rest;

import com.acme.caas.domain.CaaSTemplate;
import com.acme.caas.domain.errors.MustNotExistException;
import com.acme.caas.domain.responses.ControllerResponse;
import com.acme.caas.domain.responses.TemplateResponse;
import com.acme.caas.domain.responses.TemplateSettingsResponse;
import com.acme.caas.domain.responses.TemplatesResponse;
import com.acme.caas.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;

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

    @GetMapping(path = "/template/get", produces = "application/json")
    public TemplatesResponse getTemplates() throws Exception {
        logger.info("/template/get");

        var templates = redisService.getTemplates();

        TemplatesResponse response = new TemplatesResponse();
        response.setMessage("Templates retrieved!");
        response.setTemplateList(templates);
        return response;
    }

    @GetMapping(path = "/template/get/{id}", produces = "application/json")
    public TemplateResponse getTemplate(@PathVariable("id") String settingsId) throws Exception {
        logger.info("/template/get/" + settingsId);

        var template = redisService.getTemplate(settingsId);

        TemplateResponse response = new TemplateResponse();
        response.setMessage("Template retrieved!");
        response.setTemplate(template);
        return response;
    }

    @GetMapping(path = "/template/get/{id}/templateSetting/{settingKey}", produces = "application/json")
    public TemplateSettingsResponse getTemplateData(
        @PathVariable("id") @NotNull String settingsId,
        @PathVariable("settingKey") @NotNull String settingKey
    ) throws Exception {
        logger.info("/template/get/" + settingsId + "/templateSetting/" + settingKey);
        logger.debug("Template Setting: [" + settingKey+ "] ");

        Object settingValue = redisService.getTemplateSettings(settingsId, settingKey);

        // Respond
        TemplateSettingsResponse response = new TemplateSettingsResponse();
        response.setMessage(
            "Template setting data was retrieved: [" + settingKey + "] = '" + settingValue.toString() + "' "
        );
        response.setTemplateSettings(Map.of(settingKey, settingValue));
        return response;
    }

    @PutMapping(path = "/template/update/{id}/templateSetting", produces = "application/json")
    public ControllerResponse updateTemplateData(
        @PathVariable("id") @NotNull String settingsId,
        @RequestBody @NotNull Map.Entry<String, Object> templateSetting
    ) throws Exception {
        logger.info("/template/update/" + settingsId + "/templateSetting");
        logger.debug("Template Setting: [" + templateSetting.getKey() + "]: " + templateSetting.getValue());

        redisService.updateTemplateData(settingsId, templateSetting.getKey(), templateSetting.getValue());

        // Respond
        ControllerResponse response = new ControllerResponse();
        response.setMessage(
            "Data for template " + settingsId + " was updated: [" + templateSetting.getKey() + "]: " + templateSetting.getValue()
        );
        return response;
    }

    @PutMapping(path = "/template/update/{id}/templateName/{name}", produces = "application/json")
    public ControllerResponse updateTemplateName(
        @PathVariable("id") @NotNull String settingsId,
        @PathVariable("name") @NotNull String templateName
    ) throws Exception {
        logger.info("/template/update/templateName");
        logger.debug("Template: " + settingsId);

        redisService.updateTemplateName(settingsId, templateName);

        // Respond
        ControllerResponse response = new ControllerResponse();
        response.setMessage("Template name updated to " + templateName);
        return response;
    }



}
