# The Weather App - Frontend
A React.js frontend application to search, view, and track real-time weather updates for cities worldwide, powered by a Spring Boot backend and WebSocket updates.

##  Features
- City Search:
Search for any city and view current weather (temperature, feels like, humidity, wind, description, icon).

- Real-Time Updates:
Weather information updates automatically in real-time via WebSocket connection — no need to refresh!

- Multiple Cities Tracking:
Subscribe to multiple cities at once and view their weather side-by-side.

- Unsubscribe Option:
Remove any city's card to unsubscribe from live updates for that city.

- Responsive & Animated UI:
  - Cards laid out responsively in a grid.
  - Live update animation on each card.
  - Search bar transitions from center to top after first search.
 
## Technologies
- React.js + Vite
- Bootstrap + Custom CSS
- Axios
- STOMP.js + WebSocket

## Backend Integration
- Initial Weather Data:
`GET http://localhost:8080/api/weather?cityName=CityName`

- WebSocket Connection:
Connects to `http://localhost:8080/ws/weather` using STOMP protocol.

- Subscribed Updates:
Receives data via `/topic/weather/{city}` channels.

