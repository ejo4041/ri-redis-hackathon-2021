package com.acme.caas.service.impl;

import com.acme.caas.domain.CaaSTemplate;
import com.acme.caas.service.RedisService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.*;
import java.util.stream.Collectors;

import com.redislabs.modules.rejson.JReJSON;
import com.redislabs.modules.rejson.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private RedisTemplate<String, String> template;

    @Autowired
    private JReJSON redisClient;

    @Override
    public void createTemplate(CaaSTemplate caasTemplate) throws Exception {
        logger.info("Creating template in Redis");

        redisClient.set(caasTemplate.getSettingsId(),caasTemplate);

        logger.info("Redis template created");
    }


    /**
     * This method will update the entire value of the provided CaaSTemplate, given that the template
     * already exists. If the template does not exist, no update will occur (use createTemplate). If you
     * need to update only the templateName or only certain templateData items, use updateTemplateData for
     * better performance
     * @param caasTemplate The ConfigAsAService template to update in redis
     * @throws Exception Failed to update template or the template does not exist already
     */
    @Override
    public void updateTemplate(CaaSTemplate caasTemplate) throws Exception {
        logger.info("Updating template in Redis");
        try{
            redisClient.set(caasTemplate.getSettingsId(),caasTemplate, JReJSON.ExistenceModifier.MUST_EXIST);
        }catch(RuntimeException e){
            logger.error("Failed to update the given CaasTemplate. Ensure the CaasTemplate already exists", e);
            throw new Exception(e);
        }

        logger.info("Redis template updated");
    }

    @Override
    public void updateTemplateData(String settingsId, String settingsKey, Object settingsValue) throws Exception {
        logger.info("Updating template setting '" + settingsKey + "' in Redis");

        redisClient.set(settingsId,settingsValue, new Path(".templateSettings."+settingsKey));

        logger.info("Redis template setting '" + settingsKey + "' updated");
    }

    @Override
    public void deleteTemplateData(String settingsId, String settingsKey) throws Exception {
        logger.info("Deleting template setting '" + settingsKey + "' in Redis");

        redisClient.del(settingsId, new Path(".templateSettings."+settingsKey));

        logger.info("Redis template setting '" + settingsKey + "' deleted");
    }

    @Override
    public void updateTemplateName(String settingsId, String name) throws Exception {
        logger.info("Updating template name in Redis");

        redisClient.set(settingsId,name, new Path(".templateName"));

        logger.info("Redis template name updated");
    }

    @Override
    public CaaSTemplate getTemplate(String settingsId) throws Exception {
        logger.info("Loading template with id '" + settingsId + "' in Redis");

        var template = redisClient.get(settingsId, CaaSTemplate.class,Path.ROOT_PATH);
//        var template = CaaSTemplate.builder()
//            .settingsId(jsonTemplate.get("settingsId").getAsString())
//            .templateName(jsonTemplate.get("templateName").getAsString())
//            .templateSettings(new ArrayList<>())
//            .build();
//        jsonTemplate.get("template")
//            .getAsJsonObject()
//            .entrySet()
//            .forEach(
//                entry -> {
//                    var clazz = redisClient.type(settingsId, new Path(".template." + entry.getKey()));
//                    template.getTemplateSettings()
//                        .add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().))
//                }
//            );

        logger.info("template with id '" + settingsId + "' loaded");
        return template;
    }

    @Override
    public Object getTemplateSettings(String settingsId, String settingKey) throws Exception {
        logger.info("Loading setting '" + settingKey + "' in template with id '" + settingsId + "'");

        var path = new Path(".templateSettings." + settingKey);
        var setting = redisClient.get(settingsId,redisClient.type(settingsId,path),path);

        logger.info("Loaded template setting '" + settingKey + "'");
        return setting;
    }

    @Override
    public List<Object> getTemplateSettings(String settingsId, List<String> settingKeys) throws Exception {
        return settingKeys.stream().map(
            key -> {
                try {
                    return getTemplateSettings(settingsId, key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        ).filter(entry -> entry != null).collect(Collectors.toList());
    }

    @Override
    public void deleteTemplate(CaaSTemplate template) throws Exception {
        deleteTemplate(template.getSettingsId());
    }

    @Override
    public void deleteTemplate(String settingsId) throws Exception{
        logger.info("Deleting template with id '" + settingsId + "' in Redis");

        redisClient.del(settingsId);

        logger.info("Redis template with id '" + settingsId + "' was deleted");
    }

}
