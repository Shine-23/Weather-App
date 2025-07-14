package com.shine.weather.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    //Registers the client connection point (/ws/weather)
    public void registerStompEndpoints(StompEndpointRegistry registry) {
       registry.addEndpoint("/ws/weather")
               .setAllowedOriginPatterns("*"); // allow frontend (localhost:3000)
    }

    @Override
    //Defines prefixes
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // i) prefix for client to subscribe: /topic/weather
        registry.enableSimpleBroker("/topic");
        // ii) prefix for client to send messages to backend: /app/...
        registry.setApplicationDestinationPrefixes("/app");
    }

}
