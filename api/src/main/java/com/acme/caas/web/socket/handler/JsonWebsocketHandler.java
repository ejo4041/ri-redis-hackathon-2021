package com.acme.caas.web.socket.handler;

import com.acme.caas.service.LiveUpdateService;
import com.acme.caas.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class JsonWebsocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(JsonWebsocketHandler.class);

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    private LiveUpdateService updateService;

    public JsonWebsocketHandler(LiveUpdateService updateService){
        this.updateService = updateService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if(session.getAttributes().containsKey(LiveUpdateService.SETTINGS_ID)){
            updateService.registerWebsocketSession(session);
            super.afterConnectionEstablished(session);
        }else{
            session.close(CloseStatus.BAD_DATA);
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        updateService.removeWebsocketSession(session);
        super.afterConnectionClosed(session, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        //Do nothing. We don't accept messages currently
    }
}
