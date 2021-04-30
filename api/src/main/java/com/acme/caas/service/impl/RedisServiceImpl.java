package com.acme.caas.service.impl;

import com.acme.caas.domain.CaaSTemplate;
import com.acme.caas.service.RedisService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.*;
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

    @Override
    public void createTemplate(CaaSTemplate caasTemplate) throws Exception {
        logger.info("Creating template in Redis");

        JsonObject jo = new JsonObject();
        jo.addProperty("templateName", caasTemplate.getTemplateName());
        List<HashMap<String, Object>> keyValuePairs = caasTemplate.getTemplateData();
        JsonArray jsoArr = new JsonArray();
        keyValuePairs.forEach(
            map -> {
                JsonObject jso = new JsonObject();
                String key = map.keySet().iterator().next();

                // Cast all values to Strings for now.
                // TODO: Be smarter about determining object type
                jso.addProperty(key, (String) map.get(key));
                jsoArr.add(jso);
            }
        );

        jo.add("template", jsoArr);

        //jo.addProperty("template")
        template.opsForValue().set(caasTemplate.getAdminName(), jo.toString());

        logger.info("Redis template created");
    }
}
