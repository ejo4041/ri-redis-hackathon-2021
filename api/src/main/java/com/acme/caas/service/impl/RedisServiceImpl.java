package com.acme.caas.service.impl;

import com.acme.caas.domain.CaaSTemplate;
import com.acme.caas.domain.CaaSTemplateUpdate;
import com.acme.caas.domain.errors.MustExistException;
import com.acme.caas.domain.errors.MustNotExistException;
import com.acme.caas.service.CaasKeyService;
import com.acme.caas.service.LiveUpdateService;
import com.acme.caas.service.RedisService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.redislabs.modules.rejson.JReJSON;
import com.redislabs.modules.rejson.Path;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private RedisTemplate<String, String> template;

    @Autowired
    private JReJSON redisClient;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private CaasKeyService keyService;

    @Autowired
    private LiveUpdateService liveUpdateService;

    @Override
    public CaaSTemplate createTemplate(CaaSTemplate caasTemplate) throws Exception {
        logger.info("Creating template in Redis");

        Integer count = 0;
        RuntimeException exception = null;
        CaaSTemplate template = null;

        while (template == null && count < 25) {
            try {
                caasTemplate.setSettingsId(keyService.generateKey());
                redisClient.set(caasTemplate.getSettingsId(), caasTemplate, JReJSON.ExistenceModifier.NOT_EXISTS);
                template = caasTemplate;
                keyService.addKey(caasTemplate.getSettingsId());
            } catch (RuntimeException e) {
                exception = e;
            }
            count++;
        }
        if (template == null) {
            logger.error("Could not create template in Redis");
            throw new Exception(exception);
        }

        logger.info("Redis template created");
        liveUpdateService.publishFullUpdate(template.getSettingsId(), CaaSTemplateUpdate.TemplateUpdateType.CREATE);

        return template;
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
        try {
            redisClient.set(caasTemplate.getSettingsId(), caasTemplate, JReJSON.ExistenceModifier.MUST_EXIST);
        } catch (RuntimeException e) {
            logger.error("Failed to update the given CaasTemplate. Ensure the CaasTemplate already exists", e);
            throw new MustExistException(caasTemplate.getSettingsId(), e);
        }

        logger.info("Redis template updated");
        liveUpdateService.publishFullUpdate(caasTemplate.getSettingsId(), CaaSTemplateUpdate.TemplateUpdateType.UPDATE);
    }

    @Override
    public void addTemplateData(String settingsId, String settingsKey, Object settingsValue) throws Exception {
        logger.info("Adding template setting '" + settingsKey + "' in Redis");

        var path = new Path(".templateSettings." + settingsKey);
        try {
            redisClient.set(settingsId, settingsValue, JReJSON.ExistenceModifier.NOT_EXISTS, path);
        } catch (RuntimeException e) {
            throw new MustNotExistException(settingsId, path, e);
        }

        liveUpdateService.publishSettingsUpdate(settingsId,Map.of(settingsKey,settingsValue), CaaSTemplateUpdate.TemplateUpdateType.CREATE);
        logger.info("Redis template setting '" + settingsKey + "' added");
    }

    @Override
    public void updateTemplateData(String settingsId, String settingsKey, Object settingsValue) throws Exception {
        logger.info("Updating template setting '" + settingsKey + "' in Redis");

        var path = new Path(".templateSettings." + settingsKey);
        try {
            redisClient.set(settingsId, settingsValue, JReJSON.ExistenceModifier.MUST_EXIST, path);
        } catch (RuntimeException e) {
            throw new MustExistException(settingsId, path, e);
        }

        liveUpdateService.publishSettingsUpdate(settingsId,Map.of(settingsKey,settingsValue), CaaSTemplateUpdate.TemplateUpdateType.UPDATE);
        logger.info("Redis template setting '" + settingsKey + "' updated");
    }

    @Override
    public void deleteTemplateData(String settingsId, String settingsKey) throws Exception {
        logger.info("Deleting template setting '" + settingsKey + "' in Redis");

        redisClient.del(settingsId, new Path(".templateSettings." + settingsKey));

        liveUpdateService.publishSettingsUpdate(settingsId,Map.of(settingsKey,null), CaaSTemplateUpdate.TemplateUpdateType.DELETE);
        logger.info("Redis template setting '" + settingsKey + "' deleted");
    }

    @Override
    public void updateTemplateName(String settingsId, String name) throws Exception {
        logger.info("Updating template name in Redis");

        redisClient.set(settingsId, name, new Path(".templateName"));

        liveUpdateService.publishNameUpdate(settingsId, name, CaaSTemplateUpdate.TemplateUpdateType.UPDATE);
        logger.info("Redis template name updated");
    }

    @Override
    public CaaSTemplate getTemplate(String settingsId) throws Exception {
        logger.info("Loading template with id '" + settingsId + "' in Redis");

        var template = redisClient.get(settingsId, CaaSTemplate.class, Path.ROOT_PATH);
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
    public List<CaaSTemplate> getTemplates() throws Exception {
        logger.info("Loading all templates in Redis");

        List<CaaSTemplate> templates = redisClient.mget(CaaSTemplate.class, keyService.getKeys().toArray(String[]::new));

        logger.info(templates.size() + " templates loaded from Redis");
        return templates;
    }

    @Override
    public Object getTemplateSettings(String settingsId, String settingKey) throws Exception {
        logger.info("Loading setting '" + settingKey + "' in template with id '" + settingsId + "'");

        var path = new Path(".templateSettings." + settingKey);
        var setting = redisClient.get(settingsId, redisClient.type(settingsId, path), path);

        logger.info("Loaded template setting '" + settingKey + "'");
        return setting;
    }

    @Override
    public List<Object> getTemplateSettings(String settingsId, List<String> settingKeys) throws Exception {
        return settingKeys
            .stream()
            .map(
                key -> {
                    try {
                        return getTemplateSettings(settingsId, key);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            )
            .filter(entry -> entry != null)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteTemplate(CaaSTemplate template) throws Exception {
        deleteTemplate(template.getSettingsId());
    }

    @Override
    public void deleteTemplate(String settingsId) throws Exception {
        logger.info("Deleting template with id '" + settingsId + "' in Redis");

        redisClient.del(settingsId);
        keyService.deleteKey(settingsId);

        liveUpdateService.publishFullUpdate(settingsId, CaaSTemplateUpdate.TemplateUpdateType.DELETE);
        logger.info("Redis template with id '" + settingsId + "' was deleted");
    }

    @Override
    public void deleteTemplates() throws Exception {
        logger.info("Deleting all templates in Redis");

        var keys = keyService.getKeys();
        keys.forEach(
            key -> {
                redisClient.del(key);
                keyService.deleteKey(key);
            }
        );

        logger.info(keys.size() + " templates deleted from Redis");
    }
}
