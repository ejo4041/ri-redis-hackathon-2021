package com.acme.caas.service.impl;

import com.acme.caas.domain.CaaSTemplateUpdate;
import com.acme.caas.service.LiveUpdateService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class LiveUpdateServiceImpl implements LiveUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(LiveUpdateServiceImpl.class);

    private JedisPool jedisPool;

    private Executor asyncExecutor;

    private PublishSubject<CaaSTemplateUpdate> templateUpdates;

    public final String UPDATE_TOPIC;

    private Gson gson;

    public LiveUpdateServiceImpl(@Value("${caas.redis.update_topic:update}") String updateTopic,
                                 Executor asyncExecutor,
                                 JedisPool jedisPool){
        this.asyncExecutor = asyncExecutor;
        this.jedisPool = jedisPool;
        this.UPDATE_TOPIC = updateTopic;
        this.gson = new Gson();
        templateUpdates = PublishSubject.create();

        //subscribe to redis pub/sub updates and pipe them to the templateUpdates Subject for reactive ingestion
        this.asyncExecutor.execute(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                JedisPubSub jedisPubSub = new JedisPubSub() {

                    @Override
                    public void onMessage(String channel, String message) {
                        System.out.println("Channel " + channel + " has sent a message : " + message );
                        try{
                            var templateUpdate = gson.fromJson(message, CaaSTemplateUpdate.class);
                        }catch(JsonSyntaxException syntaxException){
                            logger.error("JSON syntax error on message received from channel [" + channel + "] with error: " + syntaxException.getMessage());
                            logger.error("Syntax Error Message: " + message);
                            syntaxException.printStackTrace();
                        }catch(Exception e){
                            logger.error("Error while parsing CaasTemplateUpdate from message received on channel [" + channel + "]. Error message: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onSubscribe(String channel, int subscribedChannels) {
                        logger.info("Client is Subscribed to channel : "+ channel);
                        logger.info("Client is Subscribed to "+ subscribedChannels + " no. of channels");
                    }

                    @Override
                    public void onUnsubscribe(String channel, int subscribedChannels) {
                        logger.info("Client is Unsubscribed from channel : "+ channel);
                        logger.info("Client is Subscribed to "+ subscribedChannels + " no. of channels");
                    }

                };

                //Subscribe to all types of template updates
                jedis.subscribe(jedisPubSub, UPDATE_TOPIC + "*");
            }
        });
    }

    public Observable<CaaSTemplateUpdate> getTemplateUpdates(){
        return (Observable<CaaSTemplateUpdate>) this.templateUpdates;
    }

    @Override
    public void publishNameUpdate(String settingsId, String templateName, CaaSTemplateUpdate.TemplateUpdateType updateType) {

        logger.info("Updating template name [" + templateName + "] for Template [" + settingsId + "]");

        var update = CaaSTemplateUpdate.builder()
            .settingsId(settingsId)
            .name(templateName)
            .updateField(CaaSTemplateUpdate.TemplateUpdateField.NAME)
            .updateType(updateType)
            .build();

        publishUpdate(update);
    }

    @Override
    public void publishFullUpdate(String settingsId, CaaSTemplateUpdate.TemplateUpdateType updateType) {
        logger.info("Updating entire object for Template [" + settingsId + "]");

        var update = CaaSTemplateUpdate.builder()
            .settingsId(settingsId)
            .updateField(CaaSTemplateUpdate.TemplateUpdateField.OBJECT)
            .updateType(updateType)
            .build();

        publishUpdate(update);
    }

    @Override
    public void publishSettingsUpdate(String settingsId, List<String> settingKeys, CaaSTemplateUpdate.TemplateUpdateType updateType) {
        logger.info("Updating setting/s [" + settingKeys.stream().collect(Collectors.joining(",")) + "] for Template [" + settingsId + "]");

        var update = CaaSTemplateUpdate.builder()
            .settingsId(settingsId)
            .settingKeys(settingKeys)
            .updateField(CaaSTemplateUpdate.TemplateUpdateField.SETTINGS)
            .updateType(updateType)
            .build();

        publishUpdate(update);
    }

    private void publishUpdate(CaaSTemplateUpdate update){
        try (Jedis jedis = jedisPool.getResource()) {
            String updateString = gson.toJson(update);
            var channel = UPDATE_TOPIC + "." + update.getUpdateField().toString() + "." + update.getUpdateType().toString();
            jedis.publish(channel,updateString);
        }
    }
}
