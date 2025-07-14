package com.shine.weather.model;

//temperature, description, icon, city, humidity, wind speed.
public class Weather {

    private String city;
    private String country;
    private Double temperature;
    private String description;
    private Integer humidity;
    private Double windSpeed;
    private Double feelsLike;
    private String icon;

    public Weather(String city, String country, Double temperature, String description, Integer humidity, Double windSpeed, Double feelsLike, String icon) {
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.description = description;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.feelsLike = feelsLike;
        this.icon = icon;
    }

    public Weather() {}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
