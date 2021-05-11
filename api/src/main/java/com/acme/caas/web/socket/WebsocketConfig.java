package com.acme.caas.web.socket;

import com.acme.caas.domain.CaaSTemplateUpdate;
import com.acme.caas.service.LiveUpdateService;
import com.acme.caas.web.socket.handler.JsonWebsocketHandler;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Autowired
    private LiveUpdateService updateService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(new JsonWebsocketHandler(updateService), "/updates")
            .addInterceptors(updateInterceptor());

    }

    @Bean
    public HandshakeInterceptor updateInterceptor() {
        return new HandshakeInterceptor() {
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                           WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

                List<CaaSTemplateUpdate.TemplateUpdateType> updateTypes = Optional
                    .ofNullable(((ServletServerHttpRequest) request).getServletRequest().getParameter(LiveUpdateService.UPDATE_TYPE))
                    .map(updateType ->
                        {
                            return List.of(updateType.split(",")).stream()
                                .map(ut ->
                                    {
                                        try{
                                            return CaaSTemplateUpdate.TemplateUpdateType.valueOf(ut);
                                        }catch (Exception e){
                                            return null;
                                        }
                                    })
                                .filter(type -> type != null).collect(Collectors.toList());
                        })
                    .orElse(List.of(
                        CaaSTemplateUpdate.TemplateUpdateType.CREATE,
                        CaaSTemplateUpdate.TemplateUpdateType.UPDATE,
                        CaaSTemplateUpdate.TemplateUpdateType.DELETE));

                List<CaaSTemplateUpdate.TemplateUpdateField> updateFields = Optional
                    .ofNullable(((ServletServerHttpRequest) request).getServletRequest().getParameter(LiveUpdateService.UPDATE_FIELD))
                    .map(updateField ->
                    {
                        return List.of(updateField.split(",")).stream()
                            .map(uf ->
                            {
                                try{
                                    return CaaSTemplateUpdate.TemplateUpdateField.valueOf(uf);
                                }catch (Exception e){
                                    return null;
                                }
                            })
                            .filter(type -> type != null).collect(Collectors.toList());
                    })
                    .orElse(List.of(
                        CaaSTemplateUpdate.TemplateUpdateField.NAME,
                        CaaSTemplateUpdate.TemplateUpdateField.OBJECT,
                        CaaSTemplateUpdate.TemplateUpdateField.SETTINGS));

                String settingsId = ((ServletServerHttpRequest) request).getServletRequest().getParameter(LiveUpdateService.SETTINGS_ID);

                // This will be added to the websocket session
                attributes.put(LiveUpdateService.UPDATE_TYPE, updateTypes);
                attributes.put(LiveUpdateService.UPDATE_FIELD, updateFields);
                if(!Strings.isBlank(settingsId)){
                    attributes.put(LiveUpdateService.SETTINGS_ID, settingsId);
                }
                return true;
            }

            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Exception exception) {
                // Nothing to do after handshake
            }
        };
    }
}
