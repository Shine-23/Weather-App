package com.shine.weather.controller;

import com.shine.weather.model.Weather;
import com.shine.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class WeatherController {

    public WeatherService weatherService;

    @Autowired
    public WeatherController (WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public Mono<Weather> getWeather(@RequestParam String cityName){
        return weatherService.getWeatherByCity(cityName);
    }

}
