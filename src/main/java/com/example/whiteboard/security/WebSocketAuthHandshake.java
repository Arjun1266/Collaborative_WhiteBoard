package com.example.whiteboard.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

@Component
public class WebSocketAuthHandshake implements HandshakeInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        URI uri = request.getURI();
        String query = uri.getQuery();

        System.out.println("WS QUERY = " + query);

        if (query == null || !query.contains("token=")) {
            System.out.println("NO TOKEN FOUND");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        String token = query.substring(query.indexOf("token=") + 6);

        System.out.println("TOKEN RECEIVED = " + token);

        try {
            String username = jwtUtil.validateToken(token);
            System.out.println("TOKEN VALID → USER = " + username);

            attributes.put("username", username);
            return true;
        } catch (Exception e) {
            System.out.println("TOKEN ERROR → " + e.getMessage());
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {}
}
