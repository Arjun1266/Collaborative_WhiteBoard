package com.example.whiteboard.config;

import com.example.whiteboard.handler.WhiteboardHandler;
import com.example.whiteboard.security.WebSocketAuthHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WhiteboardHandler handler;
    private final WebSocketAuthHandshake authInterceptor;

    @Autowired
    public WebSocketConfig(WhiteboardHandler handler,
                           WebSocketAuthHandshake authInterceptor) {
        this.handler = handler;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(handler, "/whiteboard")
                .addInterceptors(authInterceptor)
                .setAllowedOriginPatterns("*");
    }
}
