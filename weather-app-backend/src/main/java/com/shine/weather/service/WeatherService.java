package com.shine.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.shine.weather.exception.CityNotFoundException;
import com.shine.weather.model.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    private final WebClient webClient;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherService(WebClient.Builder webClientBuilder,
                          @Value("${weather.api.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    //base-url?city=Delhi&appId=YOUR_KEY&units=metric
    public Mono<Weather> getWeatherByCity(String city) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geo/1.0/direct")
                        .queryParam("q", city)
                        .queryParam("limit", 1)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMap(geoJson -> {
                    if (!geoJson.isArray() || geoJson.isEmpty()) {
                        return Mono.error(new CityNotFoundException("City not found: " + city));
                    }

                    JsonNode geoNode = geoJson.get(0);
                    Double lat = geoNode.get("lat").asDouble();
                    Double lon = geoNode.get("lon").asDouble();
                    String resolvedCity = geoNode.get("name").asText();
                    String country = geoNode.get("country").asText();

                    return webClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/data/2.5/weather")
                                    .queryParam("lat", lat)
                                    .queryParam("lon", lon)
                                    .queryParam("appid", apiKey)
                                    .queryParam("units", "metric")
                                    .build())
                            .retrieve()
                            .bodyToMono(JsonNode.class)
                            .map(weatherJson -> {
                                if (!weatherJson.has("main")) {
                                    throw new CityNotFoundException("Weather data not found");
                                }

                                Double temp = weatherJson.get("main").get("temp").asDouble();
                                Integer humidity = weatherJson.get("main").get("humidity").asInt();
                                Double windSpeed = weatherJson.get("wind").get("speed").asDouble();
                                Double feelsLike = weatherJson.get("main").get("feels_like").asDouble();
                                String description = weatherJson.get("weather").get(0).get("description").asText();
                                String icon = weatherJson.get("weather").get(0).get("icon").asText();

                                return new Weather(resolvedCity, country, temp, description, humidity, windSpeed, feelsLike, icon);
                            });
                });
    }

       }