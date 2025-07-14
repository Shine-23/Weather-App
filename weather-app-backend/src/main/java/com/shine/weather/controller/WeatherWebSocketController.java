package com.shine.weather.controller;

import com.shine.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Controller
public class WeatherWebSocketController {

    private final WeatherService weatherService;
    private final SimpMessagingTemplate messagingTemplate; //To send updates to /topic/weather/{city}

    // Stores which cities are being subscribed to
    private final Map<String, Set<String>> sessionSubscriptions = new ConcurrentHashMap<>();
    Set<String> subscribedCity = sessionSubscriptions.values().stream()
            .flatMap(Set::stream)
            .collect(Collectors.toSet());

    @Autowired
    public WeatherWebSocketController(WeatherService weatherService, SimpMessagingTemplate messagingTemplate) {
        this.weatherService = weatherService;
        this.messagingTemplate = messagingTemplate;
    }

    //Called when a client sends a message to /subscribe with the city name
    @MessageMapping("/subscribe")
    public void subscribeToCity(String city, @Header("simpSessionId") String sessionId) {
        String normalizedCity = city.trim().toLowerCase();

        sessionSubscriptions.computeIfAbsent(sessionId, k -> ConcurrentHashMap.newKeySet()).add(normalizedCity);
        System.out.println("Session " + sessionId + " subscribed to city: " + normalizedCity);
    }


    @MessageMapping("/unsubscribe")
    public void unsubscribeFromCity(String city, @Header("simpSessionId") String sessionId) {
        String normalizedCity = city.trim().toLowerCase();

        Set<String> userCities = sessionSubscriptions.get(sessionId);
        if (userCities != null && userCities.remove(normalizedCity)) {
            System.out.println("Session " + sessionId + " unsubscribed from city: " + normalizedCity);
        }
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        sessionSubscriptions.remove(sessionId);
        System.out.println("Session " + sessionId + " disconnected. Removed their subscriptions.");
    }

    // This method runs every 60 seconds and sends updates to all subscribed clients
    @Scheduled(fixedRate = 30000)
    public void pushWeatherUpdates() {
        Set<String> uniqueCities = sessionSubscriptions.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        for (String city : uniqueCities) {
            try {
                weatherService.getWeatherByCity(city)
                        .subscribe(weather ->
                                messagingTemplate.convertAndSend("/topic/weather/" + city, weather)
                        );
                System.out.println("Pushed weather for city: " + city);
            } catch (Exception e) {
                System.out.println("Failed to fetch/send weather for city: " + city);
            }
        }
    }

}
