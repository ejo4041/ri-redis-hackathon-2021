package com.acme.caas.web.rest;

import com.acme.caas.domain.CaaSTemplate;
import com.acme.caas.domain.errors.MustNotExistException;
import com.acme.caas.domain.responses.ControllerResponse;
import com.acme.caas.domain.responses.TemplateResponse;
import com.acme.caas.domain.responses.TemplateSettingsResponse;
import com.acme.caas.domain.responses.TemplatesResponse;
import com.acme.caas.service.RedisService;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    /**
     * Attempts to create the given CaasTemplate in Redis. This endpoint will generated a new unique ID
     * for the template and use that for Redis persistence, before passing back the requested CaasTemplate with
     * a newly generated settingsId.
     * @param template a new CaasTemplate to add to redis
     * @return an AdminControllerResponse with message and updated CaasTemplate
     * @throws Exception
     */
    @PostMapping(path = "/template/create", consumes = "application/json", produces = "application/json")
    public TemplateResponse createTemplate(@RequestBody CaaSTemplate template) throws Exception {
        logger.info("/template/create");
        logger.debug("Template: " + template.getTemplateName());

        if (template.getSettingsId() != null) {
            logger.error(
                "The provided template already has a settingsId, which is ignored when creating " +
                "a new template in Redis. The creation process generates a new settingsId."
            );
        }

        var returnTemplate = redisService.createTemplate(template);

        // Respond
        TemplateResponse response = new TemplateResponse();
        response.setMessage("Template Saved!");
        response.setTemplate(returnTemplate);
        return response;
    }

    @PostMapping(path = "/template/create/{id}/templateSetting", consumes = "application/json", produces = "application/json")
    public ControllerResponse addTemplateData(
        @PathVariable("id") String settingsId,
        @RequestBody @NotNull Map.Entry<String, Object> templateSetting
    ) throws Exception, MustNotExistException {
        logger.info("/template/create/" + settingsId + "/templateSetting");
        logger.debug("Template Setting: [" + templateSetting.getKey() + "]: " + templateSetting.getValue());

        redisService.addTemplateData(settingsId, templateSetting.getKey(), templateSetting.getValue());

        // Respond
        ControllerResponse response = new ControllerResponse();
        response.setMessage("Template ");

        response.setMessage(
            "Data for template " + settingsId + " was added: [" + templateSetting.getKey() + "]: " + templateSetting.getValue()
        );
        return response;
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

    @PutMapping(path = "/template/update", consumes = "application/json", produces = "application/json")
    public TemplateResponse updateTemplate(@RequestBody CaaSTemplate template) throws Exception {
        logger.info("/template/update");
        logger.debug("Template: " + template.toString());

        if (template.getSettingsId() == null) {
            logger.error("The provided template does not have a settingsId, which is required in order to update " + "the template");
            throw new IllegalArgumentException("The CaasTemplate is missing a required settingsId value");
        }

        redisService.updateTemplate(template);

        // Respond
        TemplateResponse response = new TemplateResponse();
        response.setMessage("Template Updated!");
        response.setTemplate(template);
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

    @DeleteMapping(path = "/template/delete/{id}", produces = "application/json")
    public ControllerResponse deleteTemplate(@PathVariable("id") @NotNull String settingsId) throws Exception {
        logger.info("/template/delete");
        logger.debug("Template: " + settingsId);

        redisService.deleteTemplate(settingsId);

        // Respond
        ControllerResponse response = new ControllerResponse();
        response.setMessage("Template deleted with settingsId: " + settingsId);
        return response;
    }

    @DeleteMapping(path = "/template/delete/{id}/templateSetting/{key}", produces = "application/json")
    public ControllerResponse deleteTemplateData(
        @PathVariable("id") @NotNull String settingsId,
        @PathVariable("key") @NotNull String templateDataKey
    ) throws Exception {
        logger.info("/template/delete/" + settingsId + "/templateSetting/" + templateDataKey);
        logger.debug("Template: " + settingsId);

        redisService.deleteTemplateData(settingsId, templateDataKey);

        // Respond
        ControllerResponse response = new ControllerResponse();
        response.setMessage("Template data with key " + templateDataKey + " was removed from template " + settingsId);
        return response;
    }
}
