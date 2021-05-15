package com.acme.caas.service.impl;

import com.acme.caas.domain.CaaSTemplateUpdate;
import com.acme.caas.domain.RedisWebSocketSession;
import com.acme.caas.service.LiveUpdateService;
import com.acme.caas.service.RedisService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

@Service
public class LiveUpdateServiceImpl implements LiveUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(LiveUpdateServiceImpl.class);

    private JedisPool jedisPool;

    private Executor asyncExecutor;

    public final String UPDATE_TOPIC;

    private Gson gson;

    private JdkSerializationRedisSerializer jdkSerializer;

    private final String SOCKET_LIST;

    private Set<WebSocketSession> sessionList;

    public LiveUpdateServiceImpl(
        @Value("${caas.redis.update_topic:update}") String updateTopic,
        @Value("${caas.redis.socket_list}") String socketList,
        Executor asyncExecutor,
        JedisPool jedisPool
    ) {
        this.asyncExecutor = asyncExecutor;
        this.jedisPool = jedisPool;
        this.UPDATE_TOPIC = updateTopic;
        this.SOCKET_LIST = socketList;
        this.gson = new Gson();
        jdkSerializer = new JdkSerializationRedisSerializer(RedisWebSocketSession.class.getClassLoader());
        sessionList = new HashSet<>();

        //subscribe to redis pub/sub updates
        this.asyncExecutor.execute(
                () -> {
                    try (Jedis jedis = jedisPool.getResource()) {
                        JedisPubSub jedisPubSub = new JedisPubSub() {
                            @Override
                            public void onMessage(String channel, String message) {
                                System.out.println("Channel " + channel + " has sent a message : " + message);
                                try {
                                    var templateUpdate = gson.fromJson(message, CaaSTemplateUpdate.class);
                                    handleUpdate(templateUpdate, message);
                                } catch (JsonSyntaxException syntaxException) {
                                    logger.error(
                                        "JSON syntax error on message received from channel [" +
                                        channel +
                                        "] with error: " +
                                        syntaxException.getMessage()
                                    );
                                    logger.error("Syntax Error Message: " + message);
                                    syntaxException.printStackTrace();
                                } catch (Exception e) {
                                    logger.error(
                                        "Error while parsing CaasTemplateUpdate from message received on channel [" +
                                        channel +
                                        "]. Error message: " +
                                        e.getMessage()
                                    );
                                }
                            }

                            @Override
                            public void onSubscribe(String channel, int subscribedChannels) {
                                logger.info("Client is Subscribed to channel : " + channel);
                                logger.info("Client is Subscribed to " + subscribedChannels + " no. of channels");
                            }

                            @Override
                            public void onUnsubscribe(String channel, int subscribedChannels) {
                                logger.info("Client is Unsubscribed from channel : " + channel);
                                logger.info("Client is Subscribed to " + subscribedChannels + " no. of channels");
                            }
                        };

                        //Subscribe to all types of template updates
                       // jedis.subscribe(jedisPubSub, UPDATE_TOPIC + ".*");
                        jedis.subscribe(jedisPubSub, UPDATE_TOPIC );
                        System.out.println("Passed subscribe");
                   }
                }
            );
    }

    private void handleUpdate(CaaSTemplateUpdate update, String updateJson){

        TextMessage message = new TextMessage(updateJson);
        getWebsocketSessions().forEach(session -> {
            List<CaaSTemplateUpdate.TemplateUpdateType> updateTypes = (List<CaaSTemplateUpdate.TemplateUpdateType>)session
                .getAttributes()
                .get(LiveUpdateService.UPDATE_TYPE);
            List<CaaSTemplateUpdate.TemplateUpdateField> updateFields = (List<CaaSTemplateUpdate.TemplateUpdateField>)session
                .getAttributes()
                .get(LiveUpdateService.UPDATE_FIELD);
            //Does this session care about this update?
            if(updateTypes.contains(update.getUpdateType()) && updateFields.contains(update.getUpdateField())){
                try {
                    session.sendMessage(message);
                } catch (IOException e) {
                    logger.error("Error Sending CaaSTemplateUpdate to WebSocket Session: " + session.getId() + " with error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void publishNameUpdate(String settingsId, String templateName, CaaSTemplateUpdate.TemplateUpdateType updateType) {
        logger.info("Updating template name [" + templateName + "] for Template [" + settingsId + "]");

        var update = CaaSTemplateUpdate
            .builder()
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

        var update = CaaSTemplateUpdate
            .builder()
            .settingsId(settingsId)
            .updateField(CaaSTemplateUpdate.TemplateUpdateField.OBJECT)
            .updateType(updateType)
            .build();

        publishUpdate(update);
    }

    @Override
    public void publishSettingsUpdate(String settingsId, Map<String, Object> templateSettings, CaaSTemplateUpdate.TemplateUpdateType updateType) {
        logger.info("Updating setting/s [" + templateSettings.keySet().stream().collect(Collectors.joining(",")) + "] for Template [" + settingsId + "]");

        var update = CaaSTemplateUpdate
            .builder()
            .settingsId(settingsId)
            .templateSettings(templateSettings)
            .updateField(CaaSTemplateUpdate.TemplateUpdateField.SETTINGS)
            .updateType(updateType)
            .build();

        publishUpdate(update);
    }

    @Override
    public void registerWebsocketSession(WebSocketSession session) {

        this.sessionList.add(session);
    }

    @Override
    public void removeWebsocketSession(WebSocketSession session) {
        this.sessionList.remove(session);
    }

    @Override
    public List<WebSocketSession> getWebsocketSessions() {
        return this.sessionList.stream().collect(Collectors.toList());
    }

    private void publishUpdate(CaaSTemplateUpdate update) {
        try (Jedis jedis = jedisPool.getResource()) {
            String updateString = gson.toJson(update);
            var channel = UPDATE_TOPIC;
            jedis.publish(channel, updateString);
        }
    }
}
