package com.acme.caas.service;

import com.acme.caas.domain.CaaSTemplate;
import com.acme.caas.domain.CaaSTemplateUpdate;
import com.acme.caas.domain.RedisWebSocketSession;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

import java.util.List;

public interface LiveUpdateService {

    public static final String UPDATE_TYPE = "templateUpdateType";
    public static final String UPDATE_FIELD = "templateUpdateField";
    public static final String SETTINGS_ID = "settingsId";

    public void publishNameUpdate(String settingsId, String templateName, CaaSTemplateUpdate.TemplateUpdateType updateType);

    public void publishFullUpdate(String settingsId, CaaSTemplateUpdate.TemplateUpdateType updateType);

    public void publishSettingsUpdate(String settingsId, List<String> settingKeys, CaaSTemplateUpdate.TemplateUpdateType updateType);

    public void registerWebsocketSession(WebSocketSession session);

    public void removeWebsocketSession(WebSocketSession session);

    public List<WebSocketSession> getWebsocketSessions();
}
