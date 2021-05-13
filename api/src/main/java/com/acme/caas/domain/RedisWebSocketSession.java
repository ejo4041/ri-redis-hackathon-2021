package com.acme.caas.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.security.Principal;
import java.util.Map;

@Data
@EqualsAndHashCode
public class RedisWebSocketSession implements Serializable {

    private HttpHeaders headers;
    private Map<String, Object> attributes;
    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;
    private Principal user;

    public RedisWebSocketSession(){

    }

    public RedisWebSocketSession(StandardWebSocketSession session){
        this.headers = session.getHandshakeHeaders();
        this.attributes = session.getAttributes();
        this.localAddress = session.getLocalAddress();
        this.remoteAddress = session.getRemoteAddress();
        this.user = session.getPrincipal();
    }

    public StandardWebSocketSession getWebSocketSession(){
        return new StandardWebSocketSession(this.headers, this.attributes, this.localAddress, this.remoteAddress, this.user);
    }

}
