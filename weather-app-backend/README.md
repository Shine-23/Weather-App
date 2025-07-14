# The Weather App - Backend
The Weather App is a real-time weather tracking web application that allows users to search for a city and view up-to-date weather information using public weather APIs. 

## Features
- City-Based Weather Fetching:
REST API to get live weather details (temperature, humidity, wind, etc.) for any searched city.

- Real-Time Weather Updates:
Uses WebSocket + STOMP protocol to push periodic weather updates for subscribed cities.

- Dynamic Client Subscription:
Clients can:
  - Subscribe to live updates for a city.
  - Unsubscribe anytime to stop receiving updates.

- Session-Based Tracking:
Tracks weather subscriptions per WebSocket session to ensure personalized updates.

- Scheduled Weather Push:
Every 30 seconds, latest weather is fetched and pushed for all subscribed cities.

- Reactive & Non-Blocking:
Built with Spring WebFlux (WebClient) for non-blocking HTTP calls to the weather API.

## Technologies
- Java 21
- Spring Boot 3.5.3
- WebSocket (STOMP)
- OpenWeather API

## API Endpoints
- REST Endpoint:
`GET /api/weather?cityName=CityName`
→ Returns current weather data for the given city.
- WebSocket Endpoint:
`/ws/weather`
→ Establishes WebSocket connection.
- WebSocket Messaging:
  - `/app/subscribe` → Subscribe to updates for a city.
  - `/app/unsubscribe` → Unsubscribe from a city's updates.
- Broadcasts to `/topic/weather/{city}`.

## Architecture Diagram

                                                         +--------------------+
                                                         |   React Frontend   |
                                                         |--------------------|
                                                         | - Search City      |
                                                         | - Display Weather  |
                                                         | - WebSocket Client |
                                                         +---------+----------+
                                                                   |
                                                         WebSocket (WS + STOMP)
                                                                   |
                                                +------------------v------------------+
                                                |         Spring Boot Backend         |
                                                |-------------------------------------|
                                                | + REST Controller (/api/weather)    |
                                                | + WebSocket Endpoint (/ws/weather)  |
                                                | + WeatherWebSocketController        |
                                                |   - Subscribe / Unsubscribe         |
                                                |   - Push updates to /topic/weather/ |
                                                | + @Scheduled Weather Push           |
                                                | + SimpMessagingTemplate             |
                                                +------------------+------------------+
                                                                   |
                                                          WebClient (Reactive)
                                                                   |
                                                +------------------v------------------+
                                                |          OpenWeather API            |
                                                | Provides current weather data based |
                                                | on city name or coordinates         |
                                                +-------------------------------------+


