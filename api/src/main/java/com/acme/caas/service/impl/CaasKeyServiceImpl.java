package com.acme.caas.service.impl;


import com.acme.caas.service.CaasKeyService;
import com.redislabs.modules.rejson.JReJSON;
import com.redislabs.modules.rejson.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CaasKeyServiceImpl implements CaasKeyService {

    private static final Logger logger = LoggerFactory.getLogger(CaasKeyServiceImpl.class);

    private JReJSON redisClient;

    public final String KEYS_KEY;

    public final String KEY_PREFIX;

    public CaasKeyServiceImpl(JReJSON redisClient,
                              @Value("${caas.redis.key_prefix}") String keyPrefix,
                              @Value("${caas.redis.key_object}") String keysKey){
        this.redisClient = redisClient;
        this.KEY_PREFIX = keyPrefix;
        this.KEYS_KEY = keysKey;
        try{
            redisClient.set(this.KEYS_KEY, new Object(), JReJSON.ExistenceModifier.NOT_EXISTS);
        }catch(Exception e){
        }
    }

    @Override
    public void addKey(String key) throws Exception {
        boolean success = false;
        Integer count = 0;
        while(!success && count < 2){
            try{
                redisClient.set(this.KEYS_KEY,key, JReJSON.ExistenceModifier.NOT_EXISTS,new Path("."+key));
            }catch(RuntimeException e){
                //create the initial KEYS container object if needed

            }
            count++;
        }
    }

    @Override
    public void deleteKey(String key) {
        logger.info("Delete key: " + key);
        redisClient.del(this.KEYS_KEY, new Path("." + key));
    }

    @Override
    public List<String> getKeys() {
        logger.info("Getting CaasTemplate keys");
        Map<String, String> keys = redisClient.get(this.KEYS_KEY);
        return keys.keySet().stream().collect(Collectors.toList());
    }

    @Override
    public String generateKey() {
        return KEY_PREFIX + UUID.randomUUID().toString().replace("-","_");
    }
}
